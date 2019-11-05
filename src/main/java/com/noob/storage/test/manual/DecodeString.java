package com.noob.storage.test.manual;

/**
 * 说明：给定一个编码字符，按编码规则进行解码，输出字符串
 * 编码规则是`count[letter]`，将letter的内容count次输出，count是0或正整数，letter是区分大小写的纯字母
 * 示例：
 * const s = '3[a]2[bc]'; decodeString(s); // 返回'aaabcbc'
 * const s = '3[a2[c]]'; decodeString(s); // 返回'accaccacc'
 * const s = '2[abc]3[cd]ef'; decodeString(s); // 返回'abcabccdcdcdef'
 */
public class DecodeString {

    private static final char LEFT = '[', RIGHT = ']';

    public static void main(String[] args) {
        String s = "3[a]2[bc]";
//        System.out.println(decodeString(s, 0, s.length())); // 返回'aaabcbc'
//        s = "3[a2[c]]";
//        System.out.println(decodeString(s, 0, s.length())); // 返回'accaccacc'
        s = "2[abc]3[cd]ef";
        System.out.println(decodeString(s, 0, s.length())); // 返回'abcabccdcdcdef'
    }

    public static String decodeString(String s, int from, int end) {
        StringBuilder result = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        StringBuilder num = new StringBuilder();
        for (int i = from; i < end; i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                sb.append(c);
            } else if (c == LEFT) {
                String inner = decodeString(s, i + 1, s.indexOf(']', i));
                i += s.indexOf(']', i) - i;
                String currentStr = decodeOne(num, inner);
                result.append(sb).append(currentStr);
                sb.delete(0, sb.length());
                num.delete(0, num.length());
            } else {
                num.append(c);
            }
        }
        if(sb.length()>0){
            result.append(sb.toString());
        }
        return result.toString();
    }

    private static String decodeOne(StringBuilder num, String inner) {
        int nums = Integer.valueOf(num.toString());
        if (nums == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < nums; i++) {
            result.append(inner);
        }
        return result.toString();
    }

}
