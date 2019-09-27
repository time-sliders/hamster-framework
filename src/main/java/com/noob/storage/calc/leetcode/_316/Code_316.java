package com.noob.storage.calc.leetcode._316;

/**
 * @author 卢云(luyun)
 * @version game over
 * @since 2019.09.26
 */
public class Code_316 {

    public static String removeDuplicateLetters(String s) {
        int[] charArray = new int[26];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            charArray[c - 'a'] = 1;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] > 0) {
                sb.append((char) ('a' + i));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(removeDuplicateLetters("aaabbbadvrgssd"));
    }

}
