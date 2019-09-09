package com.noob.storage;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author 卢云(luyun)
 * @since 2019.09.06
 */
public class TestFastJsonBUG {
    private final String DEATH_STRING = "{\"a\":\"\\x";
    @Test
    public void test_OOM() throws Exception {
        try {
            Object obj = JSON.parse(DEATH_STRING);
            System.out.println(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
