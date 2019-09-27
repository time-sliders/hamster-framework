package com.noob.storage.calc.sort;

/**
 * 冒泡排序
 * <p>
 * 时间复杂度 O(n^2)
 * <p>
 * 1. 比较相邻的元素。如果第一个比第二个大，就交换他们两个
 * 2. 对每一对相邻元素做同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数
 * 3. 针对所有的元素重复以上的步骤，除了最后一个
 * 4. 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较
 *
 * @author 卢云(luyun)
 * @version app784
 * @since 2019.09.24
 */
public class BubbleSort {

    public static void sort(int[] array, int start, int end) {
        for (int i = end; i > 0; i--) {
            for (int j = start; j < i; j++) {
                if (array[j] > array[j + 1]) {
                    int tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
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
