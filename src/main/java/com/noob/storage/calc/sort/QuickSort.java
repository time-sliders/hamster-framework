package com.noob.storage.calc.sort;

/**
 * 快速排序
 * 1. 首先设定一个分界值，通过该分界值将数组分成左右两部分
 * 2. 将大于或等于分界值的数据集中到数组右边，小于分界值的数据集中到数组的左边。
 * 此时，左边部分中各元素都小于或等于分界值，而右边部分中各元素都大于或等于分
 * 界值
 * 3. 然后，左边和右边的数据可以独立排序。对于左侧的数组数据，又可以取一个分界值，
 * 将该部分数据分成左右两部分，同样在左边放置较小值，右边放置较大值。右侧的数
 * 组数据也可以做类似处理
 * 4. 重复上述过程，可以看出，这是一个递归定义。通过递归将左侧部分排好序后，再递
 * 归排好右侧部分的顺序。当左、右两个部分各数据排序完成后，整个数组的排序也就完成了
 * ～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～
 * 原理
 * 设要排序的数组是A[0]……A[N-1]，首先任意选取一个数据（通常选用数组的第一个数）
 * 作为关键数据，然后将所有比它小的数都放到它左边，所有比它大的数都放到它右边，这
 * 个过程称为一趟快速排序。值得注意的是，快速排序不是一种稳定的排序算法，也就是说，
 * 多个相同的值的相对位置也许会在算法结束时产生变动。
 * ～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～～
 * 一趟快速排序的算法是：
 * 1. 设置两个变量i、j，排序开始的时候：i=0，j=N-1；
 * 2. 以第一个数组元素作为关键数据，赋值给key，即key=A[0]；
 * 3. 从j开始向前搜索，即由后开始向前搜索(j--)，找到第一个小于key的值A[j]，将
 * A[j]和A[i]的值交换；
 * 4. 从i开始向后搜索，即由前开始向后搜索(i++)，找到第一个大于key的A[i]，将
 * A[i]和A[j]的值交换；
 * 5. 重复第3、4步，直到i=j
 * <p>
 * 3,4步中，没找到符合条件的值，即3中A[j]不小于
 * key,4中A[i]不大于key的时候，改变j、i的值，使得j=j-1，i=i+1，直至找到为
 * 止。找到符合条件的值，进行交换的时候i， j指针位置不变。另外，i==j这一过程
 * 一定正好是i+或j-完成的时候，此时令循环结束
 *
 * @author 卢云(luyun)
 * @version app784
 * @since 2019.09.23
 */
public class QuickSort {

    /**
     * 快速排序
     *
     * @param array 要排序的数组
     * @param start 起始下标
     * @param end   结束下标
     */
    public static void sort(int[] array, int start, int end) {

        int i = start, j = end;
        int key = array[start];

        while (i < j) {

            // 从左向右找到第一个 >=key 的值index: i
            while (i < j && array[i] < key) {
                i++;
            }

            // 从右向左找到第一个 <=key 的值index: j
            while (i < j && array[j] > key) {
                j--;
            }

            if (array[i] == array[j] && i < j) {
                i++;
            } else {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        if (i - 1 > start) sort(array, start, i - 1);
        if (j + 1 < end) sort(array, j + 1, end);
    }

    public static void main(String[] args) {
        int[] array = new int[]{8, 9, 4, 1, 7, 6, 3, 5, 0};
        sort(array, 0, 8);
        System.out.println();
    }

}
