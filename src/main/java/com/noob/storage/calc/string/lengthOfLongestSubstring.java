package com.noob.storage.calc.string;

/**
 * @author luyun
 * @version version 1.0
 * @since 2019.11.04 17:19
 */
public class lengthOfLongestSubstring {

    public static void main(String[] args) {
        System.out.println(subMax("abcdefghijklmnbopqrstuvwbxyz"));
    }

    /**
     * [a,b,c,d....]
     * [11,34,12,5....]
     */
    public static int subMax(String s) {
        // 存放字母 a+n 最近一次出现的下标 index
        int[] idx = new int[]{-1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int max = 0;
        int current_max = 0;
        int ml = 0, mr = 0;
        int l = 0, r = 0;
        while (r < s.length()) {
            char c = s.charAt(r);
            int leftMoveTo;
            if ((leftMoveTo = idx[c - 'a']) == -1) {
                if (++current_max > max) {
                    max = current_max;
                    mr = r;
                    ml = l;
                }
            } else {
                for (int i = 0; i < idx.length; i++) {
                    int n;
                    if ((n = idx[i]) != -1 && n <= leftMoveTo) {
                        idx[i] = -1;// reset
                        current_max--;
                    }
                }
                l = leftMoveTo + 1;
            }
            idx[c - 'a'] = r;
            r++;
        }
        System.out.println(s.substring(ml, mr + 1) + " " + max);
        return max;
    }
}
