package com.noob.storage.test;

import java.math.BigDecimal;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.01
 */
public class TestMain {

    public static void main(String[] args) {
        String a = "363";
        String b = "365";
        BigDecimal c = new BigDecimal(a).divide(new BigDecimal(b),6,BigDecimal.ROUND_DOWN);
        System.out.println(c);
    }
}
