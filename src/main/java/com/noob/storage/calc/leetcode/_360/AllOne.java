package com.noob.storage.calc.leetcode._360;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 卢云(luyun)
 * @version game over
 * @since 2019.09.27
 */
public class AllOne {

    /**
     * Inc(key) - 插入一个新的值为 1 的 key。或者使一个存在的 key 增加一，保证 key 不为空字符串。
     * Dec(key) - 如果这个 key 的值是 1，那么把他从数据结构中移除掉。否者使一个存在的 key 值减一。如果这个 key 不存在，这个函数不做任何事情。key 保证不为空字符串。
     * GetMaxKey() - 返回 key 中值最大的任意一个。如果没有元素存在，返回一个空字符串""。
     * GetMinKey() - 返回 key 中值最小的任意一个。如果没有元素存在，返回一个空字符串""
     */
    Map<String, Integer> keyToValueMap;
    List<Entry> sortedEntryList;

    class Entry {
        Integer cnt;
        String key;
    }

    /**
     * Initialize your data structure here.
     */
    public AllOne() {
        keyToValueMap = new HashMap<>();
        sortedEntryList = new LinkedList<>();
    }

    /**
     * Inserts a new key <Key> with value 1. Or increments an existing key by 1.
     */
    public void inc(String key) {

    }

    /**
     * Decrements an existing key by 1. If Key's value is 1, remove it from the data structure.
     */
    public void dec(String key) {

    }

    /**
     * Returns one of the keys with maximal value.
     */
    public String getMaxKey() {
        Entry e = sortedEntryList.size() > 0
                ? sortedEntryList.get(sortedEntryList.size() - 1) : null;
        if (e == null) {
            return "";
        }
        return e.key;
    }

    /**
     * Returns one of the keys with Minimal value.
     */
    public String getMinKey() {
        Entry e = sortedEntryList.size() > 0
                ? sortedEntryList.get(0) : null;
        if (e == null) {
            return "";
        }
        return e.key;
    }
/**
 * Your AllOne object will be instantiated and called as such:
 * AllOne obj = new AllOne();
 * obj.inc(key);
 * obj.dec(key);
 * String param_3 = obj.getMaxKey();
 * String param_4 = obj.getMinKey();
 */

}
