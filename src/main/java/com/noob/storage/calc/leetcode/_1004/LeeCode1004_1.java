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
public class LeeCode1004_1 {

    public static int longestOnes(int[] A, int K) {
        int step;
        int longest = 0;
        for (int i = 0; i < A.length; i += step) {
            if (A[i] == 0) {
                step = 1;
                continue;
            }

            for (int leftCost = K; leftCost >= 0; leftCost--) {
                int curLongest = getLongestWithLeftCost(A, K, leftCost, i);
                System.out.println(i + " " + leftCost + " " + curLongest);
                if (curLongest > longest) {
                    longest = curLongest;
                }
            }

            step = getCurrentContinueOne(A, i);
        }
        return longest;
    }

    private static int getLongestWithLeftCost(int[] A, int K, int leftCost, int index) {
        int currentContinueOne = getCurrentContinueOne(A, index);
        int num = currentContinueOne;
        int leftUsed = 0;
        for (int lOffset = 1; leftUsed <= leftCost; lOffset++) {
            if (index - lOffset < 0) {
                break;
            }
            if (A[index - lOffset] == 0) {
                if (leftUsed < leftCost) {
                    leftUsed++;
                } else {
                    break;
                }
            }
            num++;
        }
        if (leftUsed < K) {
            int remain = K - leftUsed;
            for (int rOffset = 1; remain >= 0; rOffset++) {
                if (index + currentContinueOne - 1 + rOffset >= A.length) {
                    break;
                }
                if (A[index + currentContinueOne - 1 + rOffset] == 0) {
                    if (remain > 0) {
                        remain--;
                    } else {
                        break;
                    }
                }
                num++;
            }
        }
        return num;
    }

    private static int getCurrentContinueOne(int[] A, int index) {
        int num = 1;
        for (int i = 1; i < (A.length - index); i++) {
            if (A[index + i] == 1) {
                num++;
                continue;
            }
            break;
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
