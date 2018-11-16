package com.noob.storage.component.list;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 有序列表集合的聚合组件
 *
 * @param <R> 公共查询请求 Request
 * @param <V> 最终需要转换成的返回数据类型 View/Result Type
 * @param <C> 拿来做排序的字段类型 Compare Value
 * @author luyun
 * @see SortedCollectionIterator
 * @since 2018.11.13 16:59
 */
public abstract class SortedCollectionsGather<R, V, C extends Comparable<C>> {

    private List<SortedCollectionIterator<?, ?, R, C, V>> iterators = null;

    /**
     * 排序模式
     *
     * @see SortMode
     */
    private int sortMode = SortMode.ASC;

    /**
     * 聚合模式
     *
     * @see GatherMode
     */
    private int gatherMode = GatherMode.DEFAULT;

    /**
     * 是否需要去重
     */
    private boolean distinct = false;

    /**
     * 多数据源列表数据聚合
     *
     * @param request 多个数据源公共的查询请求参数
     * @return 多个数据源数据整合之后的有序的数据列表
     */
    public List<V> gather(R request) {

        if (CollectionUtils.isEmpty(iterators)) {
            return null;
        }

        List<V> resultList = new ArrayList<V>();
        // 是否已经拿到了有效数据
        boolean isFetchedValidData = false;
        // 是否是<最少查询模式>
        boolean isLeastQueryMode = gatherMode == GatherMode.LEAST_QUERY_MODE;
        // 是否被中断模式中断了
        AtomicBoolean isInterruptByLeastMode = new AtomicBoolean(false);
        // 当某一个数据源当前页数据耗尽时，是否需要查询下一页数据
        boolean needNextPage;
        // 上一个被 Consume 的数据的 Compare Value (页面显示的上一个数不作处理)
        C lastConsumeValue = null;

        while (true) {

            needNextPage = !(isFetchedValidData && isLeastQueryMode);

            for (SortedCollectionIterator<?, ?, R, C, V> i : iterators) {
                i.dragNext(request, needNextPage, isInterruptByLeastMode);
                if (isInterruptByLeastMode.get()) {
                    break;
                }
            }

            if (isInterruptByLeastMode.get()) {
                break;
            }

            SortedCollectionIterator<?, ?, R, C, V> max
                    = Collections.max(iterators, comparator);
            if (max == null || max.isEnd()) {
                break;
            }

            // 去重
            C currentCompareValue = max.getCurrentCompareValue();
            if (distinct
                    && lastConsumeValue != null
                    && isRepeatingValue(lastConsumeValue, currentCompareValue)) {
                max.consumeCurrentValue();
                lastConsumeValue = currentCompareValue;
                continue;
            }

            V v = max.convertCurrentValueToResultModel();
            resultList.add(v);
            isFetchedValidData = true;

            max.consumeCurrentValue();
            lastConsumeValue = currentCompareValue;

            if (isFinished(resultList, request)) {
                break;
            }
        }

        return resultList;
    }

    /**
     * 判断2个值是否重复，这里不直接用 comparator 是因为排序去重可能与业务去重的需求不一致
     */
    protected abstract boolean isRepeatingValue(C o1, C o2);

    private Comparator<SortedCollectionIterator<?, ?, R, C, V>> comparator =
            new Comparator<SortedCollectionIterator<?, ?, R, C, V>>() {
                @Override
                public int compare(SortedCollectionIterator<?, ?, R, C, V> o1,
                                   SortedCollectionIterator<?, ?, R, C, V> o2) {
                    if (o1.isEnd() && o2.isEnd()) {
                        return 0;
                    }
                    if (o1.isEnd()) {
                        return -1;
                    }
                    if (o2.isEnd()) {
                        return 1;
                    }
                    if (sortMode == SortMode.ASC) {
                        return o2.getCurrentCompareValue()
                                .compareTo(o1.getCurrentCompareValue());
                    } else {
                        return o1.getCurrentCompareValue()
                                .compareTo(o2.getCurrentCompareValue());
                    }
                }
            };

    /**
     * 是否已经达到结束条件
     *
     * @param resultList 结果列表
     * @param request    请求对象
     */
    protected abstract boolean isFinished(List<V> resultList, R request);

    public void addIterator(SortedCollectionIterator<?, ?, R, C, V> i) {
        if (iterators == null) {
            iterators = new ArrayList<SortedCollectionIterator<?, ?, R, C, V>>();
        }
        iterators.add(i);
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode == SortMode.DESC ? sortMode : SortMode.ASC;
    }

    public void setGatherMode(int gatherMode) {
        this.gatherMode = gatherMode;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
}
