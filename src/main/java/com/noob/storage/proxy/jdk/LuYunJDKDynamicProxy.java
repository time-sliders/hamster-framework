package com.noob.storage.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LuYunJDKDynamicProxy implements InvocationHandler {

    private Object object;

    public LuYunJDKDynamicProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("luyun see shanshan first time !");
        method.invoke(object, args);
        System.out.println("luyun take care of shanshan a whole lifetime~");
        return null;
    }

}
