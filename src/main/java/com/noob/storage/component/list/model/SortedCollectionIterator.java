package com.noob.storage.component.list.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.Iterator;
import java.util.List;

/**
 * 有序集合 Iterator
 *
 * @param <E> 集合数据类型 Element
 * @param <R> 公共查询请求 Request
 * @param <Q> 私有的查询参数 Query
 * @author luyun
 * @since 2018.11.13 17:00
 */
public abstract class SortedCollectionIterator<E, Q, R> {

    /**
     * 当前数据源数据是否耗尽
     */
    private boolean isEnd;

    /**
     * 内存临时缓冲列表
     */
    private List<E> bufferList;

    /**
     * 内存数据迭代器
     */
    private Iterator<E> bufferListIterator;

    /**
     * 当前正在处理的 value
     */
    private E currentElement;

    /**
     * 私有查询参数
     */
    private Q privateQuery;

    /**
     * 当前所在 slot 的 index
     */
    private int currentSlotIndex;

    /**
     * 将公共查询参数转换为私有的查询参数
     */
    protected abstract Q converterReqParam(R r);

    /**
     * 获取数据源数据
     */
    protected abstract List<E> query(Q query);

    /**
     * 获取当前数据
     */
    public E getCurrentValue(R r) {

        if (currentElement != null) {
            return currentElement;
        }

        if (bufferListIterator != null && bufferListIterator.hasNext()) {
            currentElement = bufferListIterator.next();
            return currentElement;
        }

        if (privateQuery == null) {
            privateQuery = converterReqParam(r);
        } else {
            privateQuery = buildNextPageQueryParam(privateQuery, bufferList);
        }

        bufferList = query(privateQuery);

        if (CollectionUtils.isEmpty(bufferList)) {
            isEnd = true;
            return null;
        }

        bufferListIterator = bufferList.iterator();
        currentElement = bufferListIterator.next();
        return currentElement;
    }

    public void dragNextValueIfConsumed() {
        if (isEnd || currentElement != null) {
            return;
        }

    }


    /**
     * 构建下一页数据的查询参数
     *
     * @param prePageQueryParam 上一页的查询参数
     * @param prePageDataList   上一页的数据
     */
    protected abstract Q buildNextPageQueryParam(Q prePageQueryParam, List<E> prePageDataList);


}
