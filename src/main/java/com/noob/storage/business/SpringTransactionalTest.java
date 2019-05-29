package com.noob.storage.business;

import com.noob.storage.business.cmp.TransactionalInterface;
import com.noob.storage.business.cmp.UserManagerComponent;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringTransactionalTest {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/zhangwei/Downloads/");
        FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext(
                "classpath:spring/spring-context-ForTransactionalTest.xml");
        if (UserManagerComponent.class.getInterfaces().length != 0) {
            TransactionalInterface cmp = (TransactionalInterface) ac.getBean("userManagerComponent");
            cmp.noTransaction();
        } else {
            UserManagerComponent cmp = (UserManagerComponent) ac.getBean("userManagerComponent");
            cmp.transaction();
        }
        System.out.println("system successfully finished");
    }
}