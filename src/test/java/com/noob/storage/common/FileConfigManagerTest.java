package com.noob.storage.common;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author luyun
 * @since 2016.05.12
 */
public class FileConfigManagerTest {

    static ReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock readLock = lock.readLock();
    static Lock writeLock = lock.writeLock();

    public static void main(String[] args) throws IOException {
        new WriteThread().start();
        new WriteThread().start();
        new ReadThread().start();
        new ReadThread().start();
    }

    static class WriteThread extends Thread{
        @Override
        public void run() {
            while(true){
                writeLock.lock();
                System.out.println(Thread.currentThread().getId()+" write locked");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                } finally {
                    writeLock.unlock();
                    System.out.println(Thread.currentThread().getId()+" write unlock");
                }
            }
        }
    }

    static class ReadThread extends Thread{
        @Override
        public void run() {
            while(true){
                readLock.lock();
                System.out.println(Thread.currentThread().getId()+" read locked");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                } finally {
                    readLock.unlock();
                    System.out.println(Thread.currentThread().getId()+" read unlock");
                }
            }
        }
    }


}
