package com.noob.storage.calc.leetcode._136;

/**
 * @author 卢云(luyun)
 * @version game over
 * @since 2019.09.24
 */
public class LeetCode_136 {

    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int i : nums) {
            result ^= i;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(singleNumber(new int[]{1, 2, 5, 2, 5, 7, 9, 1, 7}));
    }
}
