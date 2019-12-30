package com.noob.storage.calc.bit;

/**
 * @author 卢云(luyun)
 * @since 2019.10.24
 */
public class StringBitAdd {

    public static void main(String[] args) {

    }

    public static String add2(String s1, String s2) {
        int l1 = s1.length();
        int l2 = s2.length();
        int max = Math.max(l1, l2);
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < max; i++) {
            char c1 = l1 - i > 0 ? s1.charAt(i) : '0';
        }

        return null;
    }


    /**
     * @param s1 011001001
     * @param s2 100000000
     * @return 111001001
     */
    public static String add(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        int l1 = s1.length();
        int l2 = s2.length();
        int max = Math.max(l1, l2);
        boolean flag = false;
        for (int i = 0; i < max; i++) {
            char c1 = l1 - i > 0 ? s1.charAt(i) : '0';
            char c2 = l2 - i > 0 ? s2.charAt(i) : '0';
            char c;
            if (c1 == 1 && c2 == 1) {
                c = flag ? '1' : 0;
                flag = true;
            } else if (c1 == 1 || c2 == 1) {
                c = flag ? '0' : '1';
            } else {
                c = flag ? '1' : '0';
                flag = false;
            }
            sb.append(c);
        }
        return sb.reverse().toString();
    }

}
