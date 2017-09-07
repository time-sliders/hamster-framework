package com.noob.storage.thread.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程文件数据处理任务<br/>
 * <p>
 * 按行读取一个文件,当数据量大的时候,单线程任务无法满足时间限制,需要多个线程来提升效率.
 * <p>
 * 由于是多线程执行,使用时必须确认文件中的数据可以是处理无序的.
 * <p>
 * 由一个单独的读取线程从文件中读取数据,将数据读取到内存中的阻塞队列。由多个消费线程从阻塞队列中拿数据,并对数据做业务处理。
 * 读取线程跟消费线程之间可以互相切换,以使所有线程都尽量保持运行状态,同一时间只能存在一个读取线程。
 *
 * @author luyun
 * @see FileDataConsumerTask
 */
public abstract class MultiThreadFileSchedulerTask extends MultiThreadTask {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadFileSchedulerTask.class);

    //默认的文件数据缓冲队列长度
    private static final int DEFAULT_CACHE_SIZE = 500;
    //共享Reader
    private BufferedReader reader;
    //文件数据是否已经全部处理完
    volatile boolean isAllDataRead;
    //文件数据缓冲队列的长度
    private int cacheSize;
    //文件数据缓冲队列
    Queue<String> dataBuffer;
    private ReentrantLock lock = new ReentrantLock();
    //是否允许消费线程开始消费
    private Condition consumeCondition = lock.newCondition();

    /**
     * @param file 需要处理的文件
     */
    public MultiThreadFileSchedulerTask(File file) {
        this(file, Runtime.getRuntime().availableProcessors() - 1);
    }

    /**
     * @param file      需要处理的文件
     * @param threadNum 需要几个线程处理,线程数不允许超过最大CPU核数
     */
    public MultiThreadFileSchedulerTask(File file, int threadNum) {
        this(file, threadNum, DEFAULT_CACHE_SIZE, null);
    }

    /**
     * @param file      需要处理的文件
     * @param threadNum 需要几个线程处理,线程数不允许超过最大CPU核数
     * @param cacheSize 缓冲队列大小
     * @param context   上下文,共享参数
     */
    public MultiThreadFileSchedulerTask(File file,
                                        int threadNum,
                                        int cacheSize,
                                        ConcurrentMap<String, Object> context) {

        super(context);

        if (file == null || !file.exists() || !file.isFile()) {
            throw new RuntimeException("invalid file!");
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.threadNum = threadNum <= 1 || threadNum > availableProcessors ? availableProcessors : threadNum;
        this.threadNum = this.threadNum < 2 ? 2 : threadNum;
        this.cacheSize = cacheSize < 10 || cacheSize > 1000 ? DEFAULT_CACHE_SIZE : cacheSize;
        dataBuffer = new LinkedBlockingQueue<String>(cacheSize);

        try {
            this.reader = new BufferedReader(new FileReader(file));
            /*
             * 初始时:
             * 1个读取线程，读取线程并［不是］启动组件的主线程
             * threadNum-1个消费线程，新建的消费线程保存在subThreadTaskList中
             * 读写线程之间的切换机制保证运行
             */
            int consumerThreadNum = this.threadNum - 1;//消费线程数
            for (int i = 0; i < consumerThreadNum; i++) {
                addSubTask(createConsumerTask(context));
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建消费任务
     */
    protected abstract FileDataConsumerTask createConsumerTask(ConcurrentMap<String, Object> context);

    /**
     * 开始多线程任务
     */
    protected void startAllSubTask() {

        lock.lock();
        try {

            /*
             * 初始化一个数据读取线程
             * 这一行代码必须要在lock锁里面,否则子线程先signal,主线程再
             * await 会出现主线程无法结束
             */
            startReadThread();

            /*
             * 等待文件中第一行数据被读取
             * 这个控制是为了防止出现空文件时,消费线程已经进入take()等待
             * 但是提供线程尚未开始读文件,当提供线程发现文件为空,并且设置
             * isFinished状态位,已经无法结束掉消费线程的等待,从而造成主
             * 线程无法结束。
             */
            consumeCondition.await();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isLocked()) lock.unlock();
        }

        //启动数据消费线程
        for (SubTask subTask : subThreadTaskList) {
            /*
             * 如果在启动线程的过程中,由于任务较少,
             * 数据被很快的处理完,则后续线程就不需要在被启动了
             */
            if (isAllDataRead && dataBuffer.isEmpty()) {
                super.afterSubTaskFinish();
                break;
            }

            subTask.start();
        }
    }

    // 初始化并启动一个数据读取线程
    private void startReadThread() throws IllegalAccessException, InstantiationException {
        FileDataConsumerTask readTask = createConsumerTask(context);
        readTask.setMainTask(this);
        readTask.modeSwitch();//切换到读模式
        readTask.start();
    }

    /**
     * 添加一个子线程任务
     * 重写父类方法，不再累加线程数
     */
    public void addSubTask(SubTask task) {

        if (task == null) {
            throw new NullPointerException("task must not be null!");
        }

        if (isStarted.get()) {
            throw new IllegalStateException("already started");
        }

        if (subThreadTaskList == null) {
            subThreadTaskList = new LinkedList<SubTask>();
        }

        task.setMainTask(this);
        subThreadTaskList.add(task);
    }

    // 从数据缓存队列中获取一行数据
    synchronized String take() {
        //TODO delete ?
        if (isAllDataRead) return null;
        return dataBuffer.poll();
    }

    //**********************异步文件读取线程**********************
    //文件中第一行数据是否已经被读取
    private AtomicBoolean isFirstLineRead = new AtomicBoolean(false);
    //是否存在读取线程,同一时间只允许存在一个读取线程
    AtomicBoolean isHasReadThread = new AtomicBoolean(false);

    void read() {
        lock.lock();
        try {
            while (!isAllDataRead) {
                String s = reader.readLine();

                if (s == null) {
                    isAllDataRead = true;
                } else {
                    dataBuffer.offer(s);
                }

                // 第一行数据读取完通知主线程
                if (isFirstLineRead.compareAndSet(false, true)) {
                    consumeCondition.signalAll();
                }

                // 如果缓存队列即将满容,则中断读取
                if (cacheSize - dataBuffer.size() <= threadNum) {
                    return;
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            isHasReadThread.compareAndSet(true, false);
            if (lock.isLocked()) lock.unlock();
        }
    }

    @Override
    protected void destroy() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

}
