package com.noob.storage.concurrent;

import com.noob.storage.thread.ob.Counter;
import com.noob.storage.thread.sync.AbstractEnhanceCompletionService;
import com.noob.storage.utils.DateUtil;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author luyun
 * @version APP 6.6 (Fund aip)
 * @since 2017.05.03
 */
public class TestEnhanceService {

    public static void main(String[] args) {

        System.out.println(Runtime.getRuntime().availableProcessors());

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

        AbstractEnhanceCompletionService<Boolean> ecs = new AbstractEnhanceCompletionService<Boolean>(tpe) {

            @Override
            protected void pushTasks() {
                Random random = new Random();
                for (int i = 0; i < 200; i++) {
                    submit(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            Thread.sleep(random.nextInt(300));
                            System.out.println(DateUtil.format(new Date(), DateUtil.DEFAULT_FORMAT) + "_" + Thread.currentThread().getName());
                            return true;
                        }
                    });
                }
            }

            @Override
            protected void dealResult(Boolean isSuccess) {
                if (isSuccess) {
                    counter.successPlus();
                }
            }
        };

        ecs.start();

        System.out.println(counter.toString());

    }


}
