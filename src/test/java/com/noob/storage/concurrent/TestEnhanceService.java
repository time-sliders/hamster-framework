package com.noob.storage.concurrent;

import com.noob.storage.thread.ob.Counter;
import com.noob.storage.thread.sync.AbstractResultConsumer;
import com.noob.storage.thread.sync.AbstractTaskProvider;
import com.noob.storage.thread.sync.EnhanceCompletionService;

import java.util.concurrent.*;

/**
 * @author luyun
 * @version APP 6.6 (Fund aip)
 * @since 2017.05.03
 */
public class TestEnhanceService {

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10), new RejectedExecutionHandler() {
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

        Counter counter = new Counter();
        AbstractResultConsumer<Boolean, Counter> resultHandler = new AbstractResultConsumer<Boolean, Counter>() {
            @Override
            public synchronized void consume(Boolean isSuccess) {
                counter.allPlus();
                if (isSuccess) {
                    counter.successPlus();
                }
            }
            @Override
            public Counter getResult() {
                return counter;
            }
        };

        EnhanceCompletionService<Boolean, Counter> ecs = new EnhanceCompletionService<Boolean, Counter>(tpe, resultHandler);
        AbstractTaskProvider provider = new AbstractTaskProvider(ecs) {
            @Override
            public void offerTasks() {
                for (int i = 0; i < 1000; i++) {
                    ecs.submit(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return true;
                        }
                    });
                }
            }
        };

        System.out.println(ecs.execute(provider));
        Thread.sleep(100);


    }


}
