package com.noob.storage.cglib;

import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Field;

/**
 * 测试类
 *
 * @author luyun
 * @version 1.0
 * @since 2017.12.30
 */
public class TestCglib {

    public static void main(String[] args) {

        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/zhangwei/Downloads/cglib/");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibTarget.class);
        enhancer.setCallback(new TargetInterceptor());
        CglibTarget cglibTarget = (CglibTarget) enhancer.create();

        cglibTarget.targetMethod();
        System.out.println(cglibTarget.getClass().getName());
        System.out.println(cglibTarget.getClass().getSuperclass().getName());

        Field[] fields = cglibTarget.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        System.out.println();
    }
}
