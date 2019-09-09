package com.noob.storage.calc.leetcode._1004;

/**
 * @author 卢云(luyun)
 * @since 2019.08.21
 * <p>
 * 给定一个由若干 0 和 1 组成的数组 A，我们最多可以将 K 个值从 0 变成 1 。
 * <p>
 * 返回仅包含 1 的最长（连续）子数组的长度。
 * 示例 1：
 * 输入：A = [1,1,1,0,0,0,1,1,1,1,0], K = 2
 * 输出：6
 * 解释：
 * [1,1,1,0,0,1,1,1,1,1,1]
 * 粗体数字从 0 翻转到 1，最长的子数组长度为 6。
 * <p>
 * 示例 2：
 * 输入：A = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], K = 3
 * 输出：10
 * 解释：
 * [0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1]
 * 粗体数字从 0 翻转到 1，最长的子数组长度为 10。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/max-consecutive-ones-iii
 */
public class LeeCode1004_3 {

    public static int longestOnes(int[] A, int K) {
        int max = 0, cur = 1, left = 0, right = 0;
        K = A[0] == 0 ? K - 1 : K;
        while (right < A.length - 1) {
            if (A[right + 1] == 1) {
                right++;
                cur++;
            } else if (K > 0) {
                right++;
                cur++;
                K--;
            } else if (A[left] == 0) {
                left++;
                cur--;
                K++;
            } else {
                left++;
                cur--;
            }
            if (cur > max) {
                max = cur;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int n = 0;
        System.out.println((n = longestOnes(new int[]{0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1}, 3)) + "\t" + (n == 10));
    }
}
