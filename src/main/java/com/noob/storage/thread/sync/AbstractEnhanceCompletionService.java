package com.noob.storage.thread.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link ExecutorCompletionService} 的一个抽象增强服务
 *
 * @author luyun
 * @version APP 6.6 (Fund aip)
 * @since 2017.04.28
 */
public abstract class AbstractEnhanceCompletionService<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractEnhanceCompletionService.class);

    /**
     * 执行任务的完成服务
     */
    private ExecutorCompletionService<V> ecs;

    /**
     * 是否所有任务已经全部提交到线程池
     */
    private AtomicBoolean isAllTaskSubmitted = new AtomicBoolean(false);

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 消费任务是否允许开始消费
     */
    private Condition consumerCondition = lock.newCondition();

    /**
     * 还在处理的任务总量
     */
    private AtomicInteger dealingTaskCount = new AtomicInteger(0);

    public AbstractEnhanceCompletionService(Executor executor) {
        this.ecs = new ExecutorCompletionService<V>(executor, new LinkedBlockingDeque<Future<V>>(1000));
    }

    /**
     * 提交单个任务到完成服务
     */
    protected void submit(Callable<V> task) {
        ecs.submit(task);
        dealingTaskCount.incrementAndGet();
        notifyFutureConsumerThread();
    }

    /**
     * 提交任务到完成服务<br/>
     * <p>
     * 提交单个任务时需要使用 {@link AbstractEnhanceCompletionService#submit(Callable)} 方法<br/>
     * 该方法允许分页查询,并不影响消费线程消费结果
     */
    protected abstract void submitTask();

    /**
     * 消费任务执行结果
     *
     * @param v 执行结果
     */
    protected abstract void consumerFuture(V v);

    /**
     * 重置
     */
    private void reset() {
        isAllTaskSubmitted.compareAndSet(true, false);
    }

    public synchronized void start() {

        reset();

        /*
         * 启动消费任务
         */
        Thread futureConsumerThread = new CompletionQueueConsumerTask();
        futureConsumerThread.start();

        try {
            /*
             * start 方法在调用程序主线程中运行
             * 即提交任务线程
             */
            submitTask();

            isAllTaskSubmitted.compareAndSet(false, true);

            notifyFutureConsumerThread();

            /*
             * 等待结果消费线程被回收
             */
            futureConsumerThread.join();

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            notifyFutureConsumerThread();
        }
    }

    /**
     * 唤醒结果消费线程的等待状态
     */
    private void notifyFutureConsumerThread() {
        lock.lock();
        try {
            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 处理结果队列消费任务
     */
    class CompletionQueueConsumerTask extends Thread {

        @Override
        public void run() {


            while (true) {

                if (isAllTaskSubmitted.get() && dealingTaskCount.get() <= 0) {
                    break;
                }

                /*
                 * 1.第一个任务提交过慢问题:
                 * 消费任务是在最先启动的,该任务只会在任务开始提交或者任务已经全
                 * 部提交完毕之后开始正式消费
                 *
                 * 2.最后一个任务过快执行问题:
                 * 假设:最后一个任务提交完之后,结果被很快消费处理完
                 * isAllTaskSubmitted 字段在 CompletionQueueConsumerTask 进入下
                 * 一次while循环判断的时候,甚至还没来的及置为 true
                 * 就会导致CompletionQueueConsumerTask 一直等待在take方法,从而死锁在这
                 */
                lock.lock();
                try {
                    if (!isAllTaskSubmitted.get() && dealingTaskCount.get() <= 0) {
                        consumerCondition.await();
                    }
                } catch (InterruptedException e) {
                    logger.warn("CompletionQueueConsumerTask增强服务结果消费线程在消费等待时发生InterruptedException", e);
                } finally {
                    lock.unlock();
                }

                int count = dealingTaskCount.get();

                for (int i = 0; i < count; i++) {

                    try {

                        V v = ecs.take().get();

                        dealingTaskCount.decrementAndGet();

                        consumerFuture(v);

                    } catch (Throwable e) {
                        logger.warn(e.getMessage(), e);
                    }
                }

            }
        }
    }
}