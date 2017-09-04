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
 * @param <V> 单条数据处理结果类型
 * @param <S> 所有V的汇总结果
 * @author luyun
 * @version APP 6.6 (Fund aip)
 * @see AbstractTaskProvider
 * @see AbstractResultHandler
 * @since 2017.04.28
 */
public class EnhanceCompletionService<V, S> {

    private static final Logger logger = LoggerFactory.getLogger(EnhanceCompletionService.class);

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

    /**
     * 结果处理器
     */
    private AbstractResultHandler<V, S> resultHandler;

    public EnhanceCompletionService(Executor executor, AbstractResultHandler<V, S> resultHandler) {
        this.ecs = new ExecutorCompletionService<V>(executor, new LinkedBlockingDeque<Future<V>>(1000));
        this.resultHandler = resultHandler;
    }

    /**
     * 提交单个任务到完成服务
     */
    public void submit(Callable<V> task) {
        ecs.submit(task);
        dealingTaskCount.incrementAndGet();
        notifyFutureConsumerThread();
    }

    /**
     * 重置
     */
    private void reset() {
        isAllTaskSubmitted.compareAndSet(true, false);
    }

    /**
     * 开始任务并执行
     *
     * @param taskProvider 任务提供者
     */
    public synchronized S start(AbstractTaskProvider taskProvider) {

        reset();

        /*
         * 启动消费任务
         */
        Thread futureConsumerThread = new CompletionQueueConsumerTask(resultHandler);
        futureConsumerThread.start();

        try {
            /*
             * start 方法在调用程序主线程中运行
             * 即提交任务线程
             */
            taskProvider.offerTasks();

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

        return resultHandler.getResult();
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

        private AbstractResultHandler<V, S> resultHandler;

        public CompletionQueueConsumerTask(AbstractResultHandler<V, S> resultHandler) {
            this.resultHandler = resultHandler;
        }

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

                        resultHandler.consume(v);

                    } catch (Throwable e) {
                        logger.warn(e.getMessage(), e);
                    }
                }

            }
        }
    }
}
