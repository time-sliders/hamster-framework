package com.noob.storage;

/**
 * @author luyun
 * @version smart linear programming algorithm
 * @since 2019.04.24 10:23
 */
public class TestSynchronized {

    public synchronized void s1(){
        try {
            System.out.println("s1 start");
            Thread.sleep(10000L);
            System.out.println("s1 end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void s2(){
        System.out.println("s2.invoked");
    }

    public static void main(String[] args) {
        TestSynchronized s = new TestSynchronized();
        new Thread(new Runnable() {
            @Override
            public void run() {
                s.s1();
            }
        }).start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                s.s2();
            }
        }).start();
    }
}
