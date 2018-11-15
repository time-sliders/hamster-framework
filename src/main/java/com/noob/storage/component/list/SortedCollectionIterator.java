package com.noob.storage.component.list;

import org.apache.commons.collections.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 有序集合迭代器
 *
 * @param <E> 当前集合处理的数据类型 Element
 * @param <Q> 私有的查询参数 Query
 * @param <R> 公共查询请求 Request
 * @param <C> 拿来做排序的字段类型 Compare Value
 * @param <V> 最终需要转换成的返回数据类型 View/Result Type
 * @author luyun
 * @see SortedCollectionsGather
 * @since 2018.11.13 17:00
 */
public abstract class SortedCollectionIterator<E, Q, R, C extends Comparable<C>, V> {

    /**
     * 当前数据源数据是否耗尽
     */
    private boolean isEnd;

    /**
     * 内存临时的，有序的，数据缓冲列表
     */
    private List<E> bufferList;

    /**
     * Iterator of bufferList
     */
    private Iterator<E> bufferIterator;

    /**
     * 当前正在处理的 value
     */
    protected E currentElement;

    /**
     * 最后一次被消费的元素
     */
    private E lastConsumedElement;

    /**
     * 私有查询参数
     */
    private Q privateQuery;

    /**
     * 当排序值存在大量重复值的时候，需要进行已显示数据的过滤操作
     */
    private boolean isFirstIgnoreChecked = false;

    /**
     * 获取数据源数据
     */
    protected abstract List<E> query(Q query);

    /**
     * 获取下一个数据到 currentElement
     */
    void dragNext(R r, boolean needNextPage, AtomicBoolean isInterruptByLeastMode) {

        if (isEnd || currentElement != null) {
            return;
        }

        if (bufferIterator != null && bufferIterator.hasNext()) {
            currentElement = bufferIterator.next();
            return;
        }

        if (!needNextPage) {
            isInterruptByLeastMode.set(true);
            return;
        }

        /*
         * 如果内存 buffer 没有数据，则构建数据库下一页的查询参数
         * 并查询下一页的数据
         */
        while (true) {
            if (privateQuery == null) {
                privateQuery = initPrivateQueryParam(r);
            } else {
                buildNextPageQueryParam(privateQuery, bufferList);
            }
            bufferList = query(privateQuery);
            if (CollectionUtils.isEmpty(bufferList)) {
                isEnd = true;
                return;
            }

            bufferIterator = bufferList.iterator();
            if (isFirstIgnoreChecked) {
                currentElement = bufferIterator.next();
                break;
            }

            while (true) {
                if (!bufferIterator.hasNext()) {
                    break;
                }
                currentElement = bufferIterator.next();
                if (checkValidation(r, currentElement)) {
                    continue;
                }
                isFirstIgnoreChecked = true;
                break;
            }
            if (isFirstIgnoreChecked) {
                break;
            }
        }
    }

    /**
     * 消费掉 currentElement 位置的元素
     */
    void consumeCurrentValue() {
        lastConsumedElement = currentElement;
        currentElement = null;
    }

    /**
     * 指定数据是否已经在页面显示过了
     * 如果已经显示过了，则直接忽略掉，不再显示
     */
    protected abstract boolean checkValidation(R r, E e);

    /**
     * 获取 currentElement 的排序比较值
     */
    public abstract C getCurrentCompareValue();

    /**
     * 构建初始化查询参数
     */
    protected abstract Q initPrivateQueryParam(R r);

    /**
     * 构建下一页数据的查询参数
     *
     * @param prePageQueryParam 上一页的查询参数
     * @param prePageDataList   上一页的数据
     */
    protected abstract void buildNextPageQueryParam(
            Q prePageQueryParam, List<E> prePageDataList);

    /**
     * 将 currentElement 转换为最终需要返回出去的 VO
     */
    public abstract V convertCurrentValueToResultModel();

    /**
     * 当前数据源是否耗尽
     */
    boolean isEnd() {
        return isEnd;
    }

    /**
     * 获取当前 Iterator 最近一次被消费的元素
     * 主要用于前段做页面状态保持
     */
    public E getLastConsumedElement() {
        return lastConsumedElement;
    }
}
