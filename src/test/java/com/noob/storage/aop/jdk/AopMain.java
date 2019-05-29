package com.noob.storage.aop.jdk;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 卢云(luyun)
 * @version TPro
 * @since 2019.05.20
 */
public class AopMain {
    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext f = new ClassPathXmlApplicationContext("spring-aop.xml");
        IBusinessService businessService = (IBusinessService) f.getBean("businessService");
        businessService.executeBusinessA();
    }
}
