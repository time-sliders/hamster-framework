package com.noob.storage.calc.leetcode;

public class StringToInteger {

    public static void main(String[] args) {
        String s = "%#@#$张啊收到——0";
        byte[] bytes = s.getBytes();
        int i = 0;
        int x = 0;
        int n = 0;
        for (byte b : bytes) {
            int m = (((int) b) << (x));
            System.out.println("m\t" + m + "\t:" + getIntBinaryStr(m));
            System.out.println("i\t" + i + "\t:" + getIntBinaryStr(i));
            i = i | m;
            System.out.println("i|m\t" + i + "\t:" + getIntBinaryStr(i));
            x = x + 8;
            if (++n > 3) {
                break;
            }
        }
        i &= (1 << 31) - 1;
        System.out.println("result=" + i);
    }

    private static String getIntBinaryStr(int i) {
        StringBuilder sb = new StringBuilder();
        int idx = 31;
        while (idx-- > 0) {
            sb.append(((i >> idx) & 1) == 0 ? "0" : "1");
        }
        return sb.toString();
    }
}