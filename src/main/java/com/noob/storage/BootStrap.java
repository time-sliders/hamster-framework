package com.noob.storage;
import com.noob.storage.test.TestJDBCTransactional;
import com.noob.storage.test.TestRedisComponent;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BootStrap {

    public static void main(String[] args) {
        FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext("classpath:spring/spring-context.xml");
        TestJDBCTransactional.testJDBCTrans();
        TestRedisComponent.TestRedis(ac);
        System.out.println("system successfully finished");
    }
}