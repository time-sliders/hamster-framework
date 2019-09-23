package com.noob.storage.calc.sort;

/**
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
            for (int c = j - 1; c >= start; c--) {
                if (array[c] > key) {
                    array[c + 1] = array[c];
                    array[c] = key;
                } else {
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
