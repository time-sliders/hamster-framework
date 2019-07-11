package com.noob.storage.pattern.create;

public class Singleton {

    // 单例实例
    private static final Singleton instance = new Singleton();

    // 构造器私有化
    private Singleton() {
    }

    public static Singleton getInstance() {
        return instance;
    }
}
