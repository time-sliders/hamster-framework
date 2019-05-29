package com.noob.storage.aop.cglib;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.cglib.core.DebuggingClassWriter;

/**
 * @author 卢云(luyun)
 * @version 1.0
 * @since 2019.05.22
 */
public class TestCglib {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/zhangwei/Downloads/");
        //实例化一个增强器，也就是cglib中的一个class generator
        Enhancer enhancer = new Enhancer();
        //设置目标类
        enhancer.setSuperclass(Target.class);
        // 设置拦截对象
        enhancer.setCallback(new Interceptor());
        // 生成代理类并返回一个实例
        Target t = (Target) enhancer.create();
        t.A();
    }
}
