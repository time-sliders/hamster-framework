package com.noob.storage.test;

import java.math.BigDecimal;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.01
 */
public class TestMain {

    public static void main(String[] args) {
        String s;
        System.out.println(s = reverseWords("Let's take LeetCode contest"));
        System.out.println(s.equals("s'teL ekat edoCteeL tsetnoc"));
    }

    static class Entry {
        char currentChar;
        Entry preEntry;

        String print() {
            if (preEntry != null) {
                return currentChar + preEntry.print();
            } else {
                return (String.valueOf(currentChar));
            }
        }
    }

    private static String reverseWords(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        Entry lastEntry = null;
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == ' ' && lastEntry != null) {
                sb.append(lastEntry.print()).append(' ');
                lastEntry = null;
                continue;
            }
            if (lastEntry == null) {
                lastEntry = new Entry();
                lastEntry.currentChar = c;
            } else {
                Entry entry = new Entry();
                entry.currentChar = c;
                entry.preEntry = lastEntry;
                lastEntry = entry;
            }
        }
        if (lastEntry != null) {
            sb.append(lastEntry.print());
        }
        return sb.toString();
    }
}
