package com.noob.storage.proxy.javassist;

public class SuperClass {
    public void say() {
        System.out.println("SuperClass say:" + this.getClass().getSimpleName());
    }
}