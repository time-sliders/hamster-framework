package com.noob.storage.business;

import com.noob.storage.business.properties.PropertiesHolder;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringPropertiesTest {

    public static void main(String[] args) {
        FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext(
                "classpath:spring/spring-context-ForPropertiesLoad.xml");
        PropertiesHolder p = (PropertiesHolder) ac.getBean("propertiesHolder");
        System.out.println("Url: " + p.getDbUrl());
    }
}