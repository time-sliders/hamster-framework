package com.noob.storage.test.manual;

public class HashMap<K, V> {

    Entry<K, V>[] table;

    private int calc(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n < 0 ? 16 : n > 1 << 30 ? 1 << 30 : n;
    }

    class Entry<K, V> {
        K k;
        V v;
        Entry<K, V> next;
        long hash;

        public Entry(K k, V v, long hash) {
            this.k = k;
            this.v = v;
            this.hash = hash;
        }
    }

    public void put(K k, V v) {
        int hash = k.hashCode();
        int idx = indexOf(hash);
        Entry<K, V> tab;
        if (table == null || table.length == 0
                || table.length > 24) {
            // TODO resize();
        }

        if ((tab = table[idx]) == null) {
            table[idx] = new Entry<>(k, v, hash);
        } else {
            Entry<K, V> next;
            for (; ; ) {
                if (tab.hash == hash
                        && (tab.k == k || k.equals(tab.k))) {
                    table[idx] = new Entry<>(k, v, hash);
                    break;
                }
                if ((next = tab.next) == null) {
                    tab.next = new Entry<>(k, v, hash);
                    break;
                } else {
                    tab = next;
                }
            }
            // treeNode
            // treeify
        }
    }

    private int indexOf(long hash) {
        return (int) hash % table.length;
    }
}