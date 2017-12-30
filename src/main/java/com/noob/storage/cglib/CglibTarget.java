package com.noob.storage.cglib;

/**
 * 需要被代理的类
 *
 * @author luyun
 * @version 1.0
 * @since 2017.12.30
 */
public class CglibTarget {
    public void targetMethod() {
        System.out.println("CglibTarget#targetMethod invoked!");
    }

    public static void main(String[] args) {

    }
}
