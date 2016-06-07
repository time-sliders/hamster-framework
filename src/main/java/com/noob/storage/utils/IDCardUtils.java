package com.noob.storage.utils;

/**
 * 身份证相关的工具类.
 */
public abstract class IDCardUtils {

    /**
     * 根据身份证获取性别, 如果非 18 位, 则返回默认值
     *
     * @param IDCard       身份证
     * @param defaultValue 默认值
     * @return 性别:1为男,0为女.
     */
    public static int getGender(String IDCard, int defaultValue) {
        // 第17位代表性别，奇数为男，偶数为女。
        if (IDCard.length() == 18) {
            char c = IDCard.charAt(16);
            return Integer.parseInt(String.valueOf(c)) % 2;
        } else if (IDCard.length() == 15) {
            char c = IDCard.charAt(14);
            return Integer.parseInt(String.valueOf(c)) % 2;
        } else {
            return defaultValue;
        }
    }

    /**
     * 根据身份证获取生日, 格式为 yyyyMMdd,
     *
     * @return 生日
     */
    public static String getBirthday(String IDCard) {
        if (IDCard.length() == 18) {
            return IDCard.substring(6, 14);
        } else if (IDCard.length() == 15) {
            return "19" + IDCard.substring(6, 12);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
