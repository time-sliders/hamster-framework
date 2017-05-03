package com.noob.storage.thread.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
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

    //执行任务的线程池
    private ExecutorCompletionService<V> ecs;

    /**
     * 是否所有任务已经全部提交到线程池
     */
    private volatile boolean isAllTaskSubmitted = false;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 消费任务是否允许开始消费
     *
     * 消费任务是在最先启动的,该任务会在
     */
    private Condition consumerCondition = lock.newCondition();

    //还在处理的任务总量
    protected AtomicInteger dealingTaskCount = new AtomicInteger(0);

    public AbstractEnhanceCompletionService(Executor executor) {
        this.ecs = new ExecutorCompletionService<V>(executor, new LinkedBlockingDeque<Future<V>>(1000));
    }

    /**
     * 提交所有任务到线程池
     */
    protected void submit(Callable<V> task) {
        ecs.submit(task);
        dealingTaskCount.incrementAndGet();
        notifyFutureConsumerThread();
    }

    protected abstract void pushTasks();

    protected abstract void dealResult(V v);

    public void start() {

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
            pushTasks();

            isAllTaskSubmitted = true;

            notifyFutureConsumerThread();

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

            lock.lock();
            try {
                logger.info("CompletionQueueConsumerTask wait for consumer condition");
                consumerCondition.await();
            } catch (InterruptedException e) {
                logger.warn("CompletionQueueConsumerTask增强服务结果消费线程在消费等待时发生InterruptedException", e);
            } finally {
                lock.unlock();
            }

            logger.info("CompletionQueueConsumerTask start consumer");
            while (true) {

                if (isAllTaskSubmitted && dealingTaskCount.get() <= 0) {
                    break;
                }

                try {
                    V v = ecs.take().get();

                    dealingTaskCount.decrementAndGet();

                    dealResult(v);

                } catch (InterruptedException e) {
                    logger.warn(e.getMessage(), e);
                } catch (ExecutionException e) {
                    logger.warn(e.getMessage(), e);
                    dealingTaskCount.decrementAndGet();
                }
            }

            logger.info("CompletionQueueConsumerTask end consumer");
        }

    }
}
