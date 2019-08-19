package com.noob.storage.test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 卢云(luyun)
 * @since 2019.08.13
 */
public class TestBigDecimal {

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal("10");
        BigDecimal e = new BigDecimal("7");
        System.out.println(b.divide(e, RoundingMode.HALF_UP)); // 1
        System.out.println(b.divide(e, BigDecimal.ROUND_HALF_UP));// 1
        System.out.println(b.divide(e.setScale(2,BigDecimal.ROUND_HALF_UP), BigDecimal.ROUND_HALF_UP));// 1
        System.out.println(b.setScale(2,BigDecimal.ROUND_HALF_UP).divide(e, BigDecimal.ROUND_HALF_UP));// 1.43
        System.out.println(b.divide(e, 2,BigDecimal.ROUND_HALF_UP));//1.43
        System.out.println(b.setScale(6, BigDecimal.ROUND_HALF_UP).divide(e, 6, BigDecimal.ROUND_HALF_UP));//1.428571
        System.out.println(b.setScale(6, BigDecimal.ROUND_HALF_UP).divide(e, BigDecimal.ROUND_HALF_UP));// 1.428571
    }
}
