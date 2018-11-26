package com.noob.storage.component.list;

/**
 * 去重模式
 *
 * @author luyun
 * @since 2018.11.19 16:42
 */
public interface DistinctMode {

    /**
     * 不进行去重
     */
    int NO_DISTINCT = 0;

    /**
     * 只对上一个元素做去重
     * 相比较全局去重模式，该模式去重【性能更好】，但是需要确保用来去重字段 与 排序字段是同一个字段
     */
    int ONLY_LAST_VALUE_DISTINCT = 1;

    /**
     * 不分页, 全局去重
     * 该模式下，不支持分页查询数据【性能较差】
     */
    int NO_PAGE_GLOBAL_DISTINCT = 2;

}
