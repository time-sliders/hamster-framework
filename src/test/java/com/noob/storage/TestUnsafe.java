package com.noob.storage;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class TestUnsafe {

    public static void main(String[] args) throws Exception {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        Thread t = new Thread(() -> {
            System.out.println("t1\t" + System.currentTimeMillis());
            unsafe.park(false, 0L);
            System.out.println("t2\t" + System.currentTimeMillis());
        });
        t.start();
        Thread.sleep(5000L);
        System.out.println("m1\t" + System.currentTimeMillis());
        unsafe.unpark(t);
        System.out.println("m2\t" + System.currentTimeMillis());
    }
}

