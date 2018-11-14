package com.noob.storage.component.list;

import com.noob.storage.component.list.model.SortedCollectionIterator;
import com.noob.storage.component.list.model.SortedSlotTable;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 有序列表聚合组件
 *
 * @param <R> 公共查询请求 Request
 * @param <V> 公共查询返回数据类型 VO
 * @author luyun
 * @since 2018.11.13 16:59
 */
public abstract class SortedCollectionGather<R, V> {

    private List<SortedCollectionIterator<?, ?, R>> iterators = null;

    public void addIterator(SortedCollectionIterator<?, ?, R> i) {
        if (iterators == null) {
            iterators = new ArrayList<SortedCollectionIterator<?, ?, R>>();
        }
        iterators.add(i);
    }

    public List<V> gather(R r) {

        if (CollectionUtils.isEmpty(iterators)) {
            return null;
        }

        List<V> resultList = new ArrayList<V>();
        int initialCapacity = iterators.size() * 4 / 3 + 1;
        // 有序槽表 存储所有 Iterator
        SortedSlotTable sortedSlotTable = new SortedSlotTable(initialCapacity);

        while (true) {

            for (SortedCollectionIterator<?, ?, R> i : iterators) {
                i.dragNextValueIfConsumed();
            }



            nextValue = sortedSlotTable.removeFirstValue();

            // 所有槽都已取尽
            if (nextValue == null) {
                break;
            }

            // 是否已经达到目标
            if (isFinished(resultList, r)) {
                break;
            }
        }
        return resultList;
    }

    /**
     * 是否已经达到目标数据
     *
     * @param resultList 结果列表
     * @param request    请求对象
     */
    protected abstract boolean isFinished(List<V> resultList, R request);
}
