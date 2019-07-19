package com.noob.storage.test;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.07.17
 */
public class TestSpringMVC {

    public static void main(String[] args) {
        FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext(
                "classpath:spring/spring-context-ForPropertiesLoad.xml");
    }
}
