package com.noob.storage.annotation;

import java.lang.annotation.*;

/**
 * 打印方法调用的耗时信息
 *
 * @author luyun
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PerformanceLog {

    /**
     * 打印日志的级别
     * <li>1 debug</li>
     * <li>2 info</li>
     * <li>3 warn</li>
     * <li>4 error</li>
     */
    int getLogLevel() default 1;

    String name = null;

}
