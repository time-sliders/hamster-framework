package com.noob.storage.rpc.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK 代理<br/>
 * JDK的动态代理依靠接口实现，如果有些类并没有实现接口，则不能使用JDK代理
 *
 * @author luyun
 * @since 2016.03.24
 */
public class RemoteFacadeProxy implements InvocationHandler{

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy code");
        return null;
    }

}
