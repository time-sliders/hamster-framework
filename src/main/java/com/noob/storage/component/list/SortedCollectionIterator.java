package com.noob.storage.component.list;

import com.noob.storage.component.list.model.DistinctAndCompareModel;
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
 * @param <S> 拿来做排序的字段类型 sort Compare Value
 * @param <D> 拿来做去重的字段类型 distinct Compare Value
 * @param <V> 最终需要转换成的返回数据类型 View/Result Type
 * @author luyun
 * @see SortedCollectionsGather
 * @since 2018.11.13 17:00
 */
public abstract class SortedCollectionIterator<E, Q, R,
        S extends Comparable<S>, D extends Comparable<D>, V extends DistinctAndCompareModel<S>> {

    /**
     * 是否数据源的所有数据都已经被查出来了
     */
    private boolean isAllDataQueried = false;

    /**
     * 当前数据源数据是否全部被处理过了
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
    protected E lastConsumedElement;

    /**
     * 私有查询参数
     */
    private Q privateQuery;

    /**
     * 获取数据源数据
     */
    protected abstract List<E> query(Q query, R request);

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

        boolean isFindValidData = false;
        int emptyLoopCount = 0; //轮空次数
        int lastQueryCount = 0; //上一次查询到的数据总量

        //noinspection ConditionalBreakInInfiniteLoop
        while (true) {

            if (privateQuery == null) {
                privateQuery = initPrivateQueryParam(r);
            } else {
                if (isAllDataQueried) {
                    isEnd = true;
                    return;
                }
                if (!buildNextPageQueryParam(r, privateQuery, bufferList, emptyLoopCount)) {
                    isEnd = true;
                    return;
                }

                if (!needNextPage) {
                    isInterruptByLeastMode.set(true);
                    return;
                }
            }

            bufferList = query(privateQuery, r);
            if (CollectionUtils.isEmpty(bufferList)) {
                bufferIterator = null;
                isEnd = true;
                return;
            }

            if (privateQuery != null
                    && isAllDataQueried(privateQuery, bufferList)) {
                isAllDataQueried = true;
            }

            /*
             * 这里是为了避免查询过多的数据到 Mem 导致 OOM
             * 这一机制可能会导致数据不能被查尽！
             */
            if (emptyLoopCount > 25) {
                isEnd = true;
                return;
            }

            if (emptyLoopCount > 0
                    && bufferList.size() == lastQueryCount) {
                isEnd = true;
                return;
            }

            lastQueryCount = bufferList.size();
            bufferIterator = bufferList.iterator();
            currentElement = bufferIterator.next();

            while (true) {

                if (isValidData(r, currentElement)) {
                    isFindValidData = true;
                    break;
                }

                if (bufferIterator.hasNext()) {
                    currentElement = bufferIterator.next();
                    continue;
                }
                break;
            }

            if (isFindValidData) {
                break;
            }
            emptyLoopCount++;
        }
    }

    /**
     * 根据上一次查询的数据列表 以及 上一次的查询参数 判断数据源是否已经耗尽
     *
     * @param privateQuery 上一次的查询参数
     * @param bufferList   上一次查询的数据列表
     * @return true if All data is queried
     */
    protected abstract boolean isAllDataQueried(Q privateQuery, List<E> bufferList);

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
    protected abstract boolean isValidData(R r, E e);

    /**
     * 获取 currentElement 的排序比较值
     */
    S getCurrentElementSortCompareValue() {
        return getSortCompareValue(currentElement);
    }

    public abstract S getSortCompareValue(E e);

    public abstract String getDistinctId(E e);

    public abstract int getDistinctSource();

    /**
     * 获取currentElement 的去重比较值
     */
    public abstract D getCurrentElementDistinctCompareValue();

    /**
     * 构建初始化查询参数
     */
    protected abstract Q initPrivateQueryParam(R r);

    /**
     * 构建下一页数据的查询参数
     *
     * @param prePageQueryParam 上一页的查询参数
     * @param prePageDataList   上一页的数据
     * @param emptyLoopCount    轮空次数
     * @return 是否有必要查询下一页
     */
    protected abstract boolean buildNextPageQueryParam(
            R r, Q prePageQueryParam, List<E> prePageDataList,
            int emptyLoopCount);

    /**
     * 将 currentElement 转换为最终需要返回出去的 VO
     */
    protected abstract V convertToResultModel(R r, E e);

    /**
     * 将 currentElement 转换为最终需要返回出去的 VO
     */
    V convertCurrentValueToResultModel(R r) {
        V v = convertToResultModel(r, currentElement);
        v.setDistinctId(getDistinctId(currentElement));
        v.setDistinctSource(getDistinctSource());
        v.setSortCompareValue(getSortCompareValue(currentElement));
        return v;
    }

    /**
     * 当前数据源是否耗尽
     */
    boolean isEnd() {
        return isEnd;
    }

    public abstract void afterFinished(R request);
}