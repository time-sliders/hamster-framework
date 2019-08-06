package com.noob.storage.calc;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.06
 */
public class StringCharCalculator {

    /**
     * 计算在一个字符串中，出现次数最多的字符（如果多个字符出现次数相同，则返回第一个字符）
     */
    public static char findFirstOccurMaxTimesChar(String src) {

        String lowerCaseSrc = src.toLowerCase();
        /*
         * 使用 int[] 而不是 map 更节省内存开销，提升性能
         */
        int[] times = new int[26];          // 26 个英文字母的出现次数
        int[] firstIndex = new int[26];     // 26 个字母的第一次出现的位置

        int maxTimes = -1, minIndex = -1;
        char result = 0;
        // 一次循环即得出结论
        for (int i = 0; i < lowerCaseSrc.length(); i++) {
            char c = lowerCaseSrc.charAt(i);
            int c_idx = c - 'a';
            if (c_idx > 25 || c_idx < 0)
                throw new RuntimeException(
                        "param's char must in a - z or A - Z, src = " + src);
            int c_occurTimes = ++times[c_idx];
            int c_firstIndex = firstIndex[c_idx];
            if (c_firstIndex == 0) {
                firstIndex[c_idx] = i;
                c_firstIndex = i;
            }
            if (c_occurTimes > maxTimes) {
                maxTimes = c_occurTimes;
                minIndex = c_firstIndex;
                result = c;
            }
            if (c_occurTimes == maxTimes
                    && c_firstIndex <= minIndex) {
                minIndex = c_firstIndex;
                result = c;
            }
        }
        System.out.println("the fist occur max times char is :" + result
                + ", occur times:" + maxTimes
                + ", first occur:" + (minIndex + 1));
        return result;
    }

}
