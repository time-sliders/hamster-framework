package com.noob.storage.file;

import com.noob.storage.common.Millisecond;
import com.noob.storage.thread.sync.MultiThreadTask;
import com.noob.storage.thread.sync.SubTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 单个文件多线程读取任务<br/>
 * <p>
 * <p>按行读取一个文件,当数据量大的时候,单线程任务无法满足时间限制,需要多个线程来提升效率.</p>
 * <p>由于是多线程执行,使用时必须确认文件中的数据可以是处理无序的.</p>
 * <p>由一个单独的读取线程从文件中读取数据,将数据读取到内存中的阻塞队列</p>
 * <p>由多个消费线程从阻塞队列中拿数据,并对数据做业务处理</p>
 *
 * @author luyun
 * @see FileDataProcessTask
 * @since app6.1
 */
public class MultiThreadFileReaderTask<T extends MultiThreadFileReaderTask.FileDataProcessTask> extends MultiThreadTask {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadFileReaderTask.class);

    //共享Reader TODO close
    private BufferedReader reader;
    //文件数据是否已经全部处理完
    private volatile boolean isAllDataRead = false;
    private static final int DEFAULT_CACHE_SIZE = 100;
    //文件数据缓冲队列的长度
    private int cacheSize = DEFAULT_CACHE_SIZE;
    //文件数据缓冲队列
    private BlockingQueue<String> lineDataBuffer = new LinkedBlockingQueue<String>(cacheSize);
    private ReentrantLock lock = new ReentrantLock();
    //第一行数据是否已经被读取
    private Condition firstLineReadCondition = lock.newCondition();
    private Class<T> clazz;
    private int threadNum;

    /**
     * @param file      需要处理的文件
     * @param threadNum 需要几个线程处理
     * @param context   上下文,共享参数
     */
    public MultiThreadFileReaderTask(File file,
                                     int threadNum,
                                     Class<T> clazz,
                                     ConcurrentMap<String, Object> context) {
        super(context);

        if (file == null || !file.exists() || !file.isFile()) {
            throw new RuntimeException("invalid file!");
        }

        this.threadNum = threadNum <= 1 || threadNum > 10 ? 3 : threadNum;

        try {
            this.reader = new BufferedReader(new FileReader(file));
            this.clazz = clazz;
            /*
             * 初始时:1个读取线程,n-1 个处理线程
             */
            for (int i = 0; i < this.threadNum - 1; i++) {
                T readTask = clazz.newInstance();
                readTask.setMode(Mode.Execute);
                addSubTask(readTask);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setMaxWaitMillis(Millisecond.TEN_MINUS);
    }

    /**
     * 开始多线程任务
     */
    public void startAllSubTask() {

        lock.lock();
        try {

            /*
             * 文件数据读取线程
             * 这一行代码必须要在lock锁里面,否则子线程先signal,主线程再
             * await 会出现主线程无法结束
             */
            T readTask = clazz.newInstance();
            readTask.setMode(Mode.Read);
            readTask.start();

            /*
             * 等待文件中第一行数据被读取
             * 这个控制是为了防止出现空文件时,消费线程已经进入take()等待
             * 但是提供线程尚未开始读文件,当提供线程发现文件为空,并且设置
             * isFinished状态位,已经无法结束掉消费线程的等待,从而造成主
             * 线程无法结束。
             *
             * 这里的await()方法会释放掉lock的锁定,消费者线程可以拿到锁
             */
            firstLineReadCondition.await();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isLocked()) lock.unlock();
        }

        //Queue中的数据消费线程
        for (SubTask subTask : subThreadTaskList) {
            subTask.start();
        }
    }

    // 从数据缓存队列中获取一行数据
    private synchronized String take() {

        if (isAllDataRead && lineDataBuffer.isEmpty()) return null;

        try {
            return lineDataBuffer.take();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    //**********************异步文件读取线程**********************
    //处理到的文件数据行数
    private int dealCount = 0;
    //是否存在读取线程,同一时间只允许存在一个读取线程
    private AtomicBoolean isHasReadThread = new AtomicBoolean(true);

    private void read() {

        if (!isHasReadThread.compareAndSet(false, true)) {
            return;
        }

        lock.lock();
        try {
            while (!isAllDataRead) {
                String s = reader.readLine();

                if (s == null) {
                    isAllDataRead = true;
                } else {
                    lineDataBuffer.put(s);
                }

                // 第一行数据读取完通知主线程
                if (dealCount++ == 0) {
                    signalAndReleaseLock();
                }

                // 如果缓存队列即将满容,则中断读取
                if (cacheSize - lineDataBuffer.size() <= threadNum) {
                    return;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            isHasReadThread.compareAndSet(true, false);
            signalAndReleaseLock();
        }
    }

    private void signalAndReleaseLock() {
        if (lock.isLocked()) {
            firstLineReadCondition.signal();
            lock.unlock();
        }
    }

    /**
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~子线程任务~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    abstract class FileDataProcessTask extends SubTask {

        /**
         * 当前任务的模式:{@link Mode}
         */
        private volatile int mode = Mode.Execute;

        public void doBusiness(ConcurrentMap<String, Object> context) {

            while (!(isAllDataRead && CollectionUtils.isEmpty(lineDataBuffer))
                    && modeSwitch()) {

                if (Mode.Read == mode) {
                /*
                 * 当为读取模式时,线程负责从文件中读取数据到共享缓存队列中
                 */
                    read();
                } else if (Mode.Execute == mode) {
                /*
                 * 当为执行模式时,线程负责从共享缓存队列中取数据并进行处理
                 */
                    execute();
                } else {
                    //dead code
                    throw new RuntimeException("Task Mode Error:" + mode);
                }
            }
        }

        /**
         * 执行模式处理数据
         */
        private void execute() {

            String lineData;//文件中的一行数据

            while (lineDataBuffer.size() > 0) {

                lineData = take();

                if (StringUtils.isNotBlank(lineData)) {
                    continue;
                }

                try {
                    processLineData(lineData, context);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }

        /**
         * 处理一行数据
         *
         * @param lineData 文件中的一行数据
         * @param context  多线程上下文
         */
        public abstract void processLineData(String lineData, ConcurrentMap<String, Object> context);

        void setMode(int mode) {
            this.mode = mode;
        }

        /**
         * 模式切换，将任务模式切换到另一种状态
         */
        private boolean modeSwitch() {

            if (this.mode == Mode.Execute && !isHasReadThread.get()) {
                // 执行模式-->读取模式
                this.mode = Mode.Read;
            } else if (this.mode == Mode.Read) {
                // 读取模式-->执行模式
                this.mode = Mode.Execute;
            }

            return true;
        }

    }

    /**
     * 子任务模式
     */
    interface Mode {
        /**
         * 读取模式
         */
        int Read = 0;
        /**
         * 处理模式
         */
        int Execute = 1;
    }

}
