package com.noob.storage.calc.array;

/**
 * @author luyun
 * @version version 1.0
 * @since 2019.11.04 16:00
 */
public class TwoNumberSum {

    public static void main(String[] args) {
        int[] param = new int[]{4, 13, 8, 12, 3, 9, 6, 11, 5};
        int[] arr = noNegativeSum(param, 10);
        for (int anArr : arr) {
            System.out.print(anArr + ",");
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static int[] noNegativeSum(int[] param, int sum) {
        int[] result = new int[2];
        int[] ids = new int[sum];
        for (int i = 1; i < result.length; i++) {
            int n = param[i];
            if (n > sum || n < 0) {
                continue;
            }
            System.out.println(i + " " + n + " " + ids[n]);
            if (ids[n] > 0) {
                result[0] = ids[n];
                result[1] = i;
                return result;
            }
            ids[sum - n] = i;
        }
        return result;
    }
}