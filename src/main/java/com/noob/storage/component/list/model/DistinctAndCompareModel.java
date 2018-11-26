package com.noob.storage.component.list.model;

/**
 * 可去重并且可排序的对象
 *
 * @author luyun
 * @since 2018.11.20 14:51
 */
public class DistinctAndCompareModel<S extends Comparable<S>>
        implements Comparable<DistinctAndCompareModel<S>> {

    /**
     * 来源
     */
    private transient int distinctSource;

    /**
     * id
     */
    private transient String distinctId;

    /**
     * 排序比较值
     */
    private transient S sortCompareValue;

    @Override
    public int compareTo(DistinctAndCompareModel<S> o) {
        return this.sortCompareValue.compareTo(o.sortCompareValue);
    }

    public int getDistinctSource() {
        return distinctSource;
    }

    public void setDistinctSource(int distinctSource) {
        this.distinctSource = distinctSource;
    }

    public String getDistinctId() {
        return distinctId;
    }

    public void setDistinctId(String distinctId) {
        this.distinctId = distinctId;
    }

    public S getSortCompareValue() {
        return sortCompareValue;
    }

    public void setSortCompareValue(S sortCompareValue) {
        this.sortCompareValue = sortCompareValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistinctAndCompareModel<?> that = (DistinctAndCompareModel<?>) o;

        if (distinctSource != that.distinctSource) return false;
        return distinctId.equals(that.distinctId);
    }

    @Override
    public int hashCode() {
        int result = distinctSource;
        result = 31 * result + distinctId.hashCode();
        return result;
    }
}
