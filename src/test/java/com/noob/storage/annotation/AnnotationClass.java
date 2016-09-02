package com.noob.storage.annotation;

import java.lang.reflect.Method;

/**
 * @author luyun
 * @since app6.1
 */
public class AnnotationClass {

    @PerformanceLog
    public void sleep() {
    }

    public static void main(String[] args) throws Exception {
        Method method = AnnotationClass.class.getMethod("sleep");
        if (method.isAnnotationPresent(PerformanceLog.class)) {
            System.out.println(method + " is annotation with:" + PerformanceLog.class);
        }
    }

}
