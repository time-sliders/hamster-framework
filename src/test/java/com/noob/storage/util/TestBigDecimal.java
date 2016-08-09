package com.noob.storage.util;

import java.math.BigDecimal;

/**
 * @author luyun
 * @since app5.9
 */
public class TestBigDecimal {

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal("1.1054");
        System.out.println(b.setScale(2,BigDecimal.ROUND_DOWN));
    }
}
