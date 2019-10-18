package com.noob.storage.calc.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 卢云(luyun)
 * @since 2019.10.18
 */
public class Solutions {

    public static void main(String[] args) {
        Object obj = threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        System.out.println();
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        if (nums[0] * nums[nums.length - 1] >= 0) { // 无解
            return res;
        }
        for (int i = 1; i < nums.length - 2; i++) {
            int left = 0;
            int right = nums.length - 1;
            do {
                int n = nums[i] + nums[left] + nums[right];
                if (n == 0) {
                    List<Integer> l = new ArrayList<>();
                    l.add(nums[i]);
                    l.add(nums[left]);
                    l.add(nums[right]);
                    res.add(l);
                } else if (n > 0) {
                    do {
                        right--;
                    } while ((nums[right] == nums[right - 1]));
                } else {
                    do {
                        left++;
                    } while ((nums[left] == nums[left + 1]));
                }
            } while (left < right || right * left <= 0);
        }
        return res;
    }

}
