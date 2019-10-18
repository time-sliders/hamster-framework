package com.noob.storage.calc.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 卢云(luyun)
 * @since 2019.10.17
 */
public class Solution_001 {

    public static void main(String[] args) {
        System.out.println(twoSum(new int[]{3,3},6));
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> hash = new HashMap<Integer,Integer>();
        for(int i = 0;i < nums.length; i++) {
            int left = target - nums[i];
            if (hash.containsKey(left)) {
                return new int[]{hash.get(nums[i]),i};
            } else {
                hash.put(left,i);
            }
        }
        return null;
    }
}
