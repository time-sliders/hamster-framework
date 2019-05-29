package com.noob.storage;

import java.util.concurrent.ThreadLocalRandom;

class Solver {

    public static void main(String[] args) throws InterruptedException {

    }

    static final int MAXIMUM_CAPACITY = 1 << 30; // 最大容量
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        System.out.println(n >>> 1);
        System.out.println(n |( n >>> 1));
        n |= n >>> 1;
        System.out.println(n >>> 2);
        System.out.println(n |( n >>> 2));
        n |= n >>> 2;
        System.out.println(n >>> 4);
        System.out.println(n |( n >>> 4));
        n |= n >>> 4;
        System.out.println(n >>> 8);
        System.out.println(n |( n >>> 8));
        n |= n >>> 8;
        System.out.println(n >>> 16);
        System.out.println(n |( n >>> 16));
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
