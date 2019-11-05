package com.noob.storage.test.manual;

import java.util.Arrays;

/**
 * @author 卢云(luyun)
 * @since 2019.10.22
 */
public class MyArrayList<E extends Comparable<E>> {

    private Object[] table;
    private static final double loadFactor = 0.75;
    private int size;
    private int capacity;

    public void MyArrayList(int capacity) {
        table = new Object[capacity];
        this.capacity = capacity;
    }

    public boolean put(E e) {
        ensureTableSize(size + 1);
        table[size++] = e;
        return true;
    }

    @SuppressWarnings("unchecked")
    public E get(int i) {
        if (i > capacity) {
            throw new IndexOutOfBoundsException("index out if bound:" + i);
        }
        return (E) table[i];
    }

    private void ensureTableSize(int size) {
        if (((double) size) / capacity < loadFactor) {
            return;
        }
        int oldCapacity = capacity;
        int newCapacity = oldCapacity + (oldCapacity >> 1);// +50%
        table = Arrays.copyOf(table, newCapacity);
        capacity = newCapacity;
    }
}
