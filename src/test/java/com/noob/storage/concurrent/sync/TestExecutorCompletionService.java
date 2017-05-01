package com.noob.storage.concurrent.sync;

import java.util.concurrent.*;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.04.27
 */
public class TestExecutorCompletionService {

    public static void main(String[] args) {

        /*
         * 线程池
         */
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 2, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1000),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        try {
                            executor.getQueue().put(r);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        tpe.allowCoreThreadTimeOut(true);


        ExecutorCompletionService<String> ecs = new ExecutorCompletionService<String>(tpe, new LinkedBlockingDeque<Future<String>>());

    }

}
