package com.noob.storage.calc.string;

/**
 * @author luyun
 * @version app789
 * @since 2019.11.06 14:31
 */
public class Palindrome {

    public static void main(String[] args) {
        System.out.println(isPalindrome(121));
    }

    public static boolean isPalindrome(int x) {
        String s = String.valueOf(x);
        int l = 0, r = s.length() - 1;
        while (l < r && r > 0) {
            if (s.charAt(l++) != s.charAt(r--)) {
                return false;
            }
        }
        return true;
    }

}
