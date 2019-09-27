package com.noob.storage.calc.sort;

/**
 * 直接插入排序
 *
 * 时间复杂度 O(n^2)
 *
 * @author 卢云(luyun)
 * @version app784
 * @since 2019.09.23
 */
public class DirectInsertSort {

    /**
     * 默认前 N 个元素是有序的，将N+1 之后的无须元素一次插入有序中的合适位置
     */
    public static void sort(int[] array, int start, int end) {
        for (int i = start; i < end; i++) {
            int j = i + 1;
            int key = array[j];
            // 前i位为有序队列，将第 i+1 位置的元素插入到合适的位置即可。
            for (int c = j - 1; c >= start; c--) {
                // 如果比i+1大，则后移
                if (array[c] > key) {
                    array[c + 1] = array[c];
                    array[c] = key;
                } else {
                    // 已经找到合适的位置了，结束遍历，继续处理 i + 1 位置的数据
                    break;
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
