package com.noob.storage;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.12.29
 */
public class BootStrap {

    public static void main(String[] args) {
        FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext("classpath:spring/spring-context.xml");
        System.out.println(111);

    }
}
