package com.noob.storage.calc.sort;

/**
 * 选择排序
 * <p>
 * 选择排序（Selection sort）是一种简单直观的排序算法。它的工作原理是：第一次从
 * 待排序的数据元素中选出最小（或最大）的一个元素，存放在序列的起始位置，然后再从剩
 * 余的未排序元素中寻找到最小（大）元素，然后放到已排序的序列的末尾。以此类推，直到
 * 全部待排序的数据元素的个数为零。选择排序是不稳定的排序方法。
 *
 * @author 卢云(luyun)
 * @version app784
 * @since 2019.09.24
 */
public class SelectionSort {

    public static void sort(int[] array, int start, int end) {
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j <= end; j++) {
                if (array[i] > array[j]) {
                    int tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] array = new int[]{8, 9, 4, 1, 7, 6, 3, 5, 0};
        sort(array, 0, 8);
        System.out.println();
    }

}
