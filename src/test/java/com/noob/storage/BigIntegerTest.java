package com.noob.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luyun
 * @since 2016.05.17
 */
public class BigIntegerTest {

    static AtomicInteger atomicInteger = new AtomicInteger(0);

    static ExecutorService pool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        List<Future<?>> futureList = new ArrayList<Future<?>>();

        for (int i = 0; i < 5; i++) {
            Future<?> future = pool.submit(new aThread());
            futureList.add(future);
        }

        for (Future<?> future : futureList) {
            future.get();
        }
        
        System.out.println(atomicInteger.intValue());
    }

    static class aThread extends Thread {
        @Override
        public void run() {
            int i = 0;
            while (i++ < 10000) {
                atomicInteger.incrementAndGet();

            }
        }
    }
}
