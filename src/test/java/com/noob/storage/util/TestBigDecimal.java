package com.noob.storage.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author luyun
 * @since app5.9
 */
public class TestBigDecimal {
    private final static String DATE_FORMAT = "yyyMMddHHmmssSSS";

    private TestBigDecimal() {
    }

    public static void main(String[] args) {
        System.out.println(createMessageId());
    }

    public static final String createMessageId() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date()) + getRandom();
    }

    private static String getRandom() {
        String ret = "";
        String randomNum = String.valueOf(new Random().nextInt(999));
        for (int i = randomNum.length(); i < 3; i++) {
            ret += "0";
        }
        ret += randomNum;
        return ret;
    }

}
