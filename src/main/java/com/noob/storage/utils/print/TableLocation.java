package com.noob.storage.utils.print;

import java.lang.annotation.*;

/**
 * @author luyun
 * @version 理财计划
 * @since 2017.11.03
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableLocation {

    /**
     * 当前属性的名称
     * 会自动在当前td前面自动新增一个td，并写入propertyName的值
     */
    String propertyName() default "";

    /**
     * 区域标题
     * 会自动在当前行上自动新增一行，并写入fieldTitle的值
     */
    String fieldTitle() default "";

    /**
     * 第几行(仅仅只是排序)
     */
    int row() default 1;

    /**
     * 第几列(仅仅只是排序)
     */
    int col() default 1;

}
