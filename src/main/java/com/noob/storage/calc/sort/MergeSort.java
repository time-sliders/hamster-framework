package com.noob.storage.calc.sort;

/**
 * 归并排序
 * <p>
 * 时间复杂度 O(n * log ^ n ) ?
 * 空间复杂度 n + log ^ n ?
 *
 * @author 卢云(luyun)
 * @version game over
 * @since 2019.09.24
 */
public class MergeSort {

    public static int[] sort(int[] array, int start, int end) {
        if (start == end) {
            return new int[]{array[start]};
        }
        int mid = start + (end - start) / 2;
        int[] left = sort(array, start, mid);// 左半部分排序
        int[] right = sort(array, mid + 1, end);// 右半部分排序
        int[] result = new int[left.length + right.length];
        // 归并
        int m = 0, i = 0, j = 0;
        while (i < left.length && j < right.length) {
            result[m++] = left[i] < right[j] ? left[i++] : right[j++];
        }
        // 剩余部分直接追加到尾部
        while (i < left.length) {
            result[m++] = left[i++];
        }
        while (j < right.length) {
            result[m++] = right[j++];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] array = sort(new int[]{8, 9, 4, 1, 7, 6, 3, 5, 0}, 0, 8);
        System.out.println(array);
    }


}
