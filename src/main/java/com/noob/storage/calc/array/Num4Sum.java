package com.noob.storage.calc.array;


import java.util.*;

/**
 * @author 卢云(luyun)
 * @since 2019.11.15
 */
public class Num4Sum {

    public static void main(String[] args) {
        int[] is = new int[]{-5, 5, 4, -3, 0, 0, 4, -2};
        List<List<Integer>> r = new Num4Sum().fourSum(is, 4);
        for (List<Integer> l : r) {
            for (int i : l) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    public List<List<Integer>> fourSum(int[] nums, int target) {
        Set<Integer> s = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int remain = target - nums[i];
            List<List<Integer>> subResult
                    = calc(nums, i + 1, remain, 3);
            if (subResult.size() > 0) {
                for (List<Integer> sub : subResult) {
                    sub.add(0, nums[i]);
                    Collections.sort(sub);
                    int c = calcCompareValue(sub);
                    System.out.println(c);
                    if (!s.contains(c)) {
                        s.add(c);
                        result.add(sub);
                    }
                }
            }
        }
        return result;
    }

    public int calcCompareValue(List<Integer> l) {
        return l.get(0) * 10000 + l.get(1) * 1000 + l.get(2) * 100 + l.get(3);
    }

    private List<List<Integer>> calc(
            int[] nums, int startIndex, int target, int num) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = startIndex; i < nums.length; i++) {
            int remain = target - nums[i];
            if (num > 1) {
                List<List<Integer>> subResult
                        = calc(nums, i + 1, remain, num - 1);
                for (List<Integer> sub : subResult) {
                    sub.add(0, nums[i]);
                    result.add(sub);
                }
            } else if (remain == 0) {
                List<Integer> l = new ArrayList<>(1);
                l.add(nums[i]);
                result.add(l);
            }
        }
        return result;
    }
}
