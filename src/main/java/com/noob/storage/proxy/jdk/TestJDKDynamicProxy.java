package com.noob.storage.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class TestJDKDynamicProxy {
    public static void main(String[] args) {
        LuYun luYun = new LuYun();
        InvocationHandler invocationHandler = new LuYunJDKDynamicProxy(luYun);
        Genius proxy = (Genius) Proxy.newProxyInstance(Genius.class.getClassLoader(),
                new Class[]{Genius.class}, invocationHandler);
        proxy.publish();
    }
}
