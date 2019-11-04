package com.noob.storage.calc.string;

/**
 * @author luyun
 * @version version 1.0
 * @since 2019.11.04 15:29
 */
public class BitAdd {

    public static void main(String[] args) {
        /*
         * 11010 00110 11010 01001
         * 01000 10010 11010 01110
         */
        System.out.println(bitAdd("11010001101101001001", "1000100101101001110"));
    }

    @SuppressWarnings("SameParameterValue")
    private static String bitAdd(String s1, String s2) {
        int l1 = s1.length();
        int l2 = s2.length();
        int max = Math.max(l1, l2);
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (int i = 1; i <= max; i++) {
            char c1 = l1 >= i ? s1.charAt(l1 - i) : '0';
            char c2 = l2 >= i ? s2.charAt(l2 - i) : '0';
            char c;
            if (c1 == '1' && c2 == '1') {
                c = flag ? '1' : '0';
                flag = true;
            } else if (c1 == '1' || c2 == '1') {
                c = flag ? '0' : '1';
            } else {
                c = flag ? '1' : '0';
                flag = false;
            }
            sb.append(c);
        }
        if (flag) {
            sb.append('1');
        }
        return sb.reverse().toString();
    }

}
