package com.noob.storage.util;

/**
 * @author luyun
 * @since 2017.01.18 (fund portfolio pre)
 */
public class ThreadLocalTest {


    public static void main(String[] args) {
        ThreadLocal<String> tl1 = new ThreadLocal<>();
        ThreadLocal<String> tl2 = new ThreadLocal<>();
        tl1.set("zhwwashere");
        System.out.println(tl2.get());
    }

}
