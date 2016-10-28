package jdk1_8_new.lock;

public class DeadLockTest {

    private static final Object o1 = new Object();

    private static final Object o2 = new Object();

    public static void main(String[] args) {
        new aThread().start();
        new bThread().start();
    }

    static class aThread extends Thread{

        @Override
        public void run() {
            try {
                synchronized (o1){
                    System.out.println("aThread 获取 o1 锁!");
                    Thread.sleep(1000);
                    System.out.println("aThread 申请 o2 锁....");

                    synchronized (o2){
                        System.out.println("aThread 获取 o2 锁!");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class bThread extends Thread{
        @Override
        public void run() {
            try {
                synchronized (o2){
                    System.out.println("bThread 获取 o2 锁!");
                    Thread.sleep(1000);
                    System.out.println("bThread 申请 o1 锁....");
                    synchronized (o1){
                        System.out.println("bThread 获取 o1 锁!");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
