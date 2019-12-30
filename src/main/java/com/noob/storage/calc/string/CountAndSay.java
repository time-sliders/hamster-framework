package com.noob.storage.calc.string;

/**
 * @author luyun
 * @version app789
 * @since 2019.11.06 13:55
 */
public class CountAndSay {

    public static void main(String[] args) {
        System.out.println(countAndSay(1));
    }

    public static String countAndSay(int n) {
        String s = "1";
        for (int i = 1; i < n; i++) {
            s = cntAndSay(s);
            System.out.println(i + "\t" + s);
        }
        return s;
    }

    public static String cntAndSay(String s) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        char c = 'a';
        for (int i = 0; i < s.length(); i++) {
            char ci = s.charAt(i);
            if (cnt == 0) {
                c = ci;
                cnt = 1;
            } else if (c != ci) {
                sb.append(cnt).append(c);
                cnt = 1;
                c = ci;
            } else {
                cnt++;
            }
        }
        sb.append(cnt).append(c);
        return sb.toString();
    }

}
