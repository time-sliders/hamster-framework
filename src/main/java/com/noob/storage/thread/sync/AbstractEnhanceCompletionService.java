package com.noob.storage.thread.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    protected ExecutorCompletionService<V> ecs;

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
    }

    protected abstract void pushTasks();

    protected abstract void dealResult(V v);

    public void start() {

        /*
         * 启动消费任务
         */
        CompletionQueueConsumerTask consumer = new CompletionQueueConsumerTask();
        consumer.start();

        try {
            /*
             * start 方法在调用程序主线程中运行
             * 即提交任务线程
             */
            pushTasks();

            /*
             * 等待消费线程结束
             */
            consumer.join();

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 处理结果队列消费任务
     */
    class CompletionQueueConsumerTask extends Thread {

        @Override
        public void run() {

            while (dealingTaskCount.get() > 0) {

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
        }

    }
}
