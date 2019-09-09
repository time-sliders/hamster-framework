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
public class LeeCode1004_2 {

    public static int longestOnes(int[] A, int K) {
        int[] s = new int[A.length];
        int cnt = 0, cv = A[0];
        boolean s0 = cv == 0;
        int si = 0;
        for (int v : A) {
            if (cv == v) {
                cnt++;
            } else {
                s[si++] = cnt;
                cnt = 1;
                cv = v;
            }
        }
        s[si++] = cnt;

        int max = 0;
        int pre1 = s0 ? 0 : s[0];
        int startIdx = s0 ? 0 : 1;
        for (int i = startIdx; i <= si; i += 2) {
            int cMax = pre1;
            int remain = K;
            if (i < si) {
                for (int j = startIdx; j < si && remain > 0; j += 2) {
                    if (remain >= s[j]) {
                        cMax = cMax + s[j] + s[j + 1];
                        remain -= s[j];
                    } else {
                        cMax += remain;
                        remain = 0;
                    }
                }
            }
            if (remain > 0) {
                cMax += remain;
            }
            if (cMax > A.length) {
                cMax = A.length;
            }
            if (cMax > max) {
                max = cMax;
            }
            pre1 = s[startIdx + 1];
            startIdx = startIdx + 2;
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(longestOnes(new int[]{1, 1, 1, 0, 0, 0, 1, 1, 1, 1}, 0));
    }
}
