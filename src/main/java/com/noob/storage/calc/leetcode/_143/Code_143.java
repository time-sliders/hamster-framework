package com.noob.storage.calc.leetcode._143;

/**
 * @author 卢云(luyun)
 * @version game over
 * @since 2019.09.24
 */
public class Code_143 {

    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        int[] value = new int[n];
        for (int i = 0; i < n; i++) {
            int v = nums[i];
            if (v < nums.length) {
                value[v] = v;
            }
        }
        for (int i = 0; i < n; i++) {
            if (value[i] <= 0) {
                return i;
            }
        }
        return 0;
    }

}
