package com.noob.storage.lock;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author luyun
 * @since 2016.05.12
 */
public class ReadWriteLock2Test {

    static int x = 0;

    private static ReentrantLock lock = new ReentrantLock(false);

    static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 100; i++) {
            new ReadThread().start();
        }

        Callable<String> callable1 = new WriteThread(1);
        Callable<String> callable2 = new WriteThread(0);

        for (int i = 0; i < 100; i++) {
            System.out.println("--------------------");

            Future<String> future1 = pool.submit(callable1);
            Future<String> future2 = pool.submit(callable2);

            try {
                future1.get();
                future2.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("--------------------" + x);
        }
        System.exit(0);
    }

    private static class WriteThread implements Callable<String> {

        int value;

        public WriteThread(int value) {
            this.value = value;
        }

        public String call() {
            lock.lock();
            int org = x;
            try {
                x = value;
                return org + ">" + value;
            } finally {
                lock.unlock();
                System.out.println(org + ">" + value);
            }
        }
    }


    private static class ReadThread extends Thread {
        @Override
        public void run() {
            while (true) {
                lock.lock();
                lock.unlock();
            }
        }
    }


}
