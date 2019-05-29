package com.noob.storage;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.06.05
 */
public class MainTest {

    public static void main(String[] args) {
        Integer i = 0;
        BigDecimal b = BigDecimal.ZERO;
        sum(i, b);
        System.out.println(i);
        System.out.println(b);
    }

    private static void sum(Integer i, BigDecimal b) {
        i = i + 1;
        b = b.add(BigDecimal.ONE);
    }
}
