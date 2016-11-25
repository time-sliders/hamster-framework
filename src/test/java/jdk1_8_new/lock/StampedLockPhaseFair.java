package jdk1_8_new.lock;

import java.util.concurrent.locks.StampedLock;

public class StampedLockPhaseFair {

    private static StampedLock lock = new StampedLock();

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
                long stamp = lock.writeLock();
                System.out.println(Thread.currentThread().getId()+" write locked");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                } finally {
                    lock.unlock(stamp);
                    System.out.println(Thread.currentThread().getId()+" write unlock");
                }
            }
        }
    }

    private static class ReadThread extends Thread{
        @Override
        public void run() {
            while(true){
                long stamp = lock.readLock();
                System.out.println(Thread.currentThread().getId()+" read locked");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                } finally {
                    lock.unlock(stamp);
                    System.out.println(Thread.currentThread().getId()+" read unlock");
                }
            }
        }
    }


}
