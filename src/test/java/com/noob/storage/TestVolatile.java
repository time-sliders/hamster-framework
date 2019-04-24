package com.noob.storage;

/**
 * @author luyun
 * @version smart linear programming algorithm
 * @since 2019.04.24 16:03
 */
public class TestVolatile {

    private static int s = 10000;
    static  int i = 0;


    static Thread A = new Thread() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    };

    static Thread B = new Thread() {
        @Override
        public void run() {
            int x = i;
            System.out.println(i);
            System.out.println(x);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
            System.out.println(x);
        }
    };

    public static void main(String[] args) {
        A.start();
        B.start();
    }

}
