package com.noob.storage.test;

import java.util.Base64;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.01
 */
public class TestMain {


    public static void main(String[] args) throws InterruptedException {
        System.out.println(new String(Base64.getDecoder().decode("cHJpbnRhYmxl")));
    }


}
