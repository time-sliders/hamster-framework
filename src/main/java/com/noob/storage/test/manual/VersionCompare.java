package com.noob.storage.test.manual;

/**
 * 说明：实现一个方法，用于比较两个版本号（version1、version2）
 * 如果version1 > version2，返回1；
 * 如果version1 < version2，返回-1，其他情况返回0
 * 版本号规则`x.y.z`，xyz均为大于等于0的整数，至少有x位
 * 示例：
 * compareVersion("0.1", "1.1.1"); // 返回-1
 * compareVersion("13.37", "1.2 "); // 返回1
 * compareVersion("1.1", "1.1.0"); // 返回0
 */
public class VersionCompare {

    public static void main(String[] args) {
        System.out.println(versionCompare("0.1", "1.1.1"));
        System.out.println(versionCompare("13.37", "1.2 "));
        System.out.println(versionCompare("1.1", "1.1.0"));
    }

    public static int versionCompare(String v1, String v2) {
        return versionToInteger(v1).compareTo(versionToInteger(v2));
    }

    public static Integer versionToInteger(String version) {
        String[] arr = version.split("\\.");
        int factor = 1000000;
        int versionInt = 0;
        for (String s : arr) {
            versionInt += (Integer.valueOf(s.trim()) * factor);
            factor /= 1000;
        }
        System.out.println(version + "\t" + versionInt);
        return versionInt;
    }
}
