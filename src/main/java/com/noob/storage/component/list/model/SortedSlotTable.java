package com.noob.storage.component.list.model;

/**
 * @author luyun
 * @since 2018.11.13 18:02
 */
public class SortedSlotTable<R> {

    private int size;

    private SortedCollectionIterator[] slotTable;

    public SortedSlotTable(int size) {
        this.size = size;
        this.slotTable = new SortedCollectionIterator[size];
    }

    public SortedCollectionIterator removeFirstValue() {
        return null;
    }

    class Entry {
        private SortedCollectionIterator iterator;
    }
}
