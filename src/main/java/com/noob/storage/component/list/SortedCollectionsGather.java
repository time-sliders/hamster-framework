package com.noob.storage.component.list;

import com.noob.storage.component.list.model.DistinctAndCompareModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 有序列表集合的聚合组件
 *
 * @param <R> 公共查询请求 Request
 * @param <V> 最终需要转换成的返回数据类型 View/Result Type
 * @param <S> 拿来做排序的字段类型 sort Compare Value
 * @param <D> 拿来做去重的字段类型 distinct Compare Value
 * @author luyun
 * @see SortedCollectionIterator
 * @since 2018.11.13 16:59
 */
public abstract class SortedCollectionsGather<R, V extends DistinctAndCompareModel<S>,
        S extends Comparable<S>, D extends Comparable<D>> {

    private static final Logger logger = LoggerFactory.getLogger(SortedCollectionsGather.class);

    private List<SortedCollectionIterator<?, ?, R, S, D, V>> iterators = null;

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
    private int distinctMode = DistinctMode.NO_DISTINCT;

    /**
     * 是否所有数据源数据都耗尽
     */
    private boolean isEnd = true;

    /**
     * 全局去重时，已经获取到的值列表
     */
    private Set<D> globalDistinctValueSet = null;

    /**
     * 最大的需要被保留的去重数据量
     */
    private int maxRemainIgnoredDataSize = 0;

    /**
     * 记录的被去重的数据，记录数据总量不会超过 maxRemainIgnoredDataSize
     */
    private List<V> distinctValues = null;

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
        // 上一个被 Consume 的数据的 distinct Compare Value (页面显示的上一个数不作处理)
        D lastDcv = null;

        while (true) {

            needNextPage = !(isFetchedValidData && isLeastQueryMode);

            for (SortedCollectionIterator<?, ?, R, S, D, V> i : iterators) {
                i.dragNext(request, needNextPage, isInterruptByLeastMode);
                if (isInterruptByLeastMode.get()) {
                    break;
                }
            }

            if (isInterruptByLeastMode.get()) {
                break;
            }

            SortedCollectionIterator<?, ?, R, S, D, V> max
                    = Collections.max(iterators, comparator);
            if (max == null || max.isEnd()) {
                break;
            }

            // 去重
            D dcv = max.getCurrentElementDistinctCompareValue();
            if (distinctMode == DistinctMode.ONLY_LAST_VALUE_DISTINCT
                    && lastDcv != null
                    && isLastRepeatingValue(lastDcv, dcv)) {
                saveDistinctData(max, request);
                max.consumeCurrentValue();
                lastDcv = dcv;
                continue;
            }
            if (distinctMode == DistinctMode.NO_PAGE_GLOBAL_DISTINCT) {
                if (isGlobalRepeatingValue(dcv)) {
                    saveDistinctData(max, request);
                    max.consumeCurrentValue();
                    continue;
                } else {
                    globalDistinctValueSet.add(dcv);
                }
            }

            V v = null;
            try {
                v = max.convertCurrentValueToResultModel(request);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }

            max.consumeCurrentValue();

            if (v != null) {
                resultList.add(v);
                isFetchedValidData = true;
                lastDcv = dcv;
            }

            if (isFinished(resultList, request)) {
                break;
            }
        }

        // 查询结束之后的处理 一般用于处理页面状态保持
        afterFinished(request);

        return resultList;
    }

    private void saveDistinctData(SortedCollectionIterator<?, ?, R, S, D, V> max, R r) {
        if (maxRemainIgnoredDataSize <= 0) {
            return;
        }
        if (distinctValues == null) {
            distinctValues = new ArrayList<V>(maxRemainIgnoredDataSize * 4 / 3 + 1);
        }
        if (distinctValues.size() < maxRemainIgnoredDataSize) {
            distinctValues.add(max.convertCurrentValueToResultModel(r));
        }
    }

    /**
     * 判断当前元素是否与上一个被消费的元素重复
     */
    abstract boolean isLastRepeatingValue(D o1, D o2);

    private boolean isGlobalRepeatingValue(D distinctCompareValue) {
        if (globalDistinctValueSet == null) {
            globalDistinctValueSet = new HashSet<D>();
            return false;
        }
        return globalDistinctValueSet.contains(distinctCompareValue);
    }

    private void afterFinished(R request) {
        for (SortedCollectionIterator<?, ?, R, S, D, V> i : iterators) {
            isEnd = isEnd && i.isEnd();
            i.afterFinished(request);
        }
    }

    private Comparator<SortedCollectionIterator<?, ?, R, S, ?, V>> comparator =
            new Comparator<SortedCollectionIterator<?, ?, R, S, ?, V>>() {
                @Override
                public int compare(SortedCollectionIterator<?, ?, R, S, ?, V> o1,
                                   SortedCollectionIterator<?, ?, R, S, ?, V> o2) {
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
                        return o2.getCurrentElementSortCompareValue()
                                .compareTo(o1.getCurrentElementSortCompareValue());
                    } else {
                        return o1.getCurrentElementSortCompareValue()
                                .compareTo(o2.getCurrentElementSortCompareValue());
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

    public void addIterator(SortedCollectionIterator<?, ?, R, S, D, V> i) {
        if (iterators == null) {
            iterators = new ArrayList<SortedCollectionIterator<?, ?, R, S, D, V>>();
        }
        iterators.add(i);
    }

    public void setSortMode(int sortMode) {
        this.sortMode = sortMode == SortMode.DESC ? sortMode : SortMode.ASC;
    }

    public void setGatherMode(int gatherMode) {
        this.gatherMode = gatherMode;
    }

    public void setDistinctMode(int distinctMode) {
        this.distinctMode = distinctMode;
    }

    public void setMaxRemainIgnoredDataSize(int maxRemainIgnoredDataSize) {
        this.maxRemainIgnoredDataSize = maxRemainIgnoredDataSize;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public List<V> getDistinctValues() {
        return distinctValues;
    }
}