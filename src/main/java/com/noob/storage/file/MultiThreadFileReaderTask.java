package com.noob.storage.file;

import com.noob.storage.thread.sync.MultiThreadTask;
import com.noob.storage.thread.sync.SubTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
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
public class MultiThreadFileReaderTask extends MultiThreadTask {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadFileReaderTask.class);
    private static final long ONE_HOUR = 1000 * 60 * 60;

    //共享Reader
    private BufferedReader reader;
    //文件数据是否已经全部处理完
    private volatile boolean isAllDataRead = false;
    //文件数据缓冲行
    private BlockingQueue<String> lineDataBuffer = new LinkedBlockingQueue<String>(10);

    private ReentrantLock lock = new ReentrantLock();
    //第一行数据是否已经被读取
    private Condition firstLineReadCondition = lock.newCondition();

    /**
     * @param file      需要处理的文件
     * @param threadNum 需要几个线程处理
     * @param clazz     处理类
     * @param context   上下文,共享参数
     * @param <T>       文件处理子类类型
     */
    public <T extends FileDataProcessTask> MultiThreadFileReaderTask(File file,
                                                                     int threadNum,
                                                                     Class<T> clazz,
                                                                     ConcurrentMap<String, Object> context) {
        super(context);

        if (file == null || !file.exists() || !file.isFile()) {
            throw new RuntimeException("invalid file!");
        }

        threadNum = threadNum <= 0 || threadNum > 10 ? 3 : threadNum;

        try {
            this.reader = new BufferedReader(new FileReader(file));
            for (int i = 0; i < threadNum; i++) {
                T task = clazz.newInstance();
                task.setReader(this);
                addSubTask(task);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setMaxWaitMillis(ONE_HOUR);
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
            new FileReaderTask().start();

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

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isLocked()) lock.unlock();
        }

        //Queue中的数据消费线程
        for (SubTask subTask : subThreadTaskList) {
            subTask.start();
        }
    }

    //同步读取
    synchronized String take() {

        if (isAllDataRead && lineDataBuffer.isEmpty()) return null;

        try {
            return lineDataBuffer.take();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    //**********************异步文件读取线程**********************
    private class FileReaderTask extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                int i = 0;
                while (!isAllDataRead) {
                    String s = reader.readLine();

                    if (s == null) {
                        isAllDataRead = true;
                    } else {
                        lineDataBuffer.put(s);
                    }

                    // 第一行数据读取完通知主线程
                    if (i++ == 0) {
                        signalAndReleaseLock();
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                /*
                 * 容错处理,防止死锁
                 */
                isAllDataRead = true;
                signalAndReleaseLock();
            }
        }

        void signalAndReleaseLock() {
            if (lock.isLocked()) {
                firstLineReadCondition.signal();
                lock.unlock();
            }
        }
    }
}
