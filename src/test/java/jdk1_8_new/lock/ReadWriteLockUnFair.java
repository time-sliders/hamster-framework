package jdk1_8_new.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 */
public class ReadWriteLockUnFair {

    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static Lock readLock = lock.readLock();
    private static Lock writeLock = lock.writeLock();

    public static void main(String[] args) throws Exception {
        Thread w = new WriteThread();
        w.setName("写");
        w.start();

        //new WriteThread().start();
        Thread r = new ReadThread();
        r.setName("读");
        r.start();
        //new ReadThread().start();
    }

    private static class WriteThread extends Thread{
        @Override
        public void run() {
            while(true){
                writeLock.lock();
                System.out.println(Thread.currentThread().getId()+" write locked");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                } finally {
                    writeLock.unlock();
                    System.out.println(Thread.currentThread().getId()+" write unlock");
                }
            }
        }
    }

    private static class ReadThread extends Thread{
        @Override
        public void run() {
            while(true){
                readLock.lock();
                System.out.println(Thread.currentThread().getId()+" read locked");
                try {
                    Thread.sleep(3000);
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
