package com.noob.storage.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 目标拦截器
 *
 * @author luyun
 * @version 1.0
 * @since 2017.12.30
 */
public class TargetInterceptor implements MethodInterceptor {

    /**
     * @param o           由CGLib动态生成的代理类实例
     * @param method      实体类所调用的被代理的方法引用
     * @param objects     参数值列表
     * @param methodProxy 代理类对方法的代理引用
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("TargetInterceptor#intercept invoke start!");

        Object result = methodProxy.invokeSuper(o, objects);

        System.out.println("TargetInterceptor#intercept invoke end!");

        return result;
    }
}
