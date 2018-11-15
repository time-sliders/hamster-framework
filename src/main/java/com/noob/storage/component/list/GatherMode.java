package com.noob.storage.component.list;

/**
 * 聚合模式
 *
 * @author luyun
 * @since 2018.11.15 15:44
 */
public interface GatherMode {

    /**
     * 默认模式
     * 只有达到结束条件 或者所有数据源数
     * 据全部耗尽的时候，才会结束
     */
    int DEFAULT = 1;

    /**
     * 最少查询模式
     * 最少查询模式条件下，第一次至少查询 N 次（N为数据源数量，每个数据源至少查询一次）
     * 如果第一遍的查询获取到了有效结果集，则在任意一个数据源准备查询下一页数据时，结束查询
     * 注意此时可能查询条数可能会不满足页面查询的 Rows 要求
     */
    int LEAST_QUERY_MODE = 2;
}
