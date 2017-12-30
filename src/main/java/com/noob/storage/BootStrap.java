package com.noob.storage;

import com.noob.storage.business.cmp.UserManagerComponent;
import com.noob.storage.business.model.UserInfo;
import com.noob.storage.business.service.UserInfoService;
import com.noob.storage.common.Millisecond;
import com.noob.storage.component.RedisComponent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import sun.reflect.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.12.29
 */
public class BootStrap {

    private static final Logger logger = LoggerFactory.getLogger(BootStrap.class);

    public static void main(String[] args) {
        FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext("classpath:spring/spring-context.xml");
        envCheck(ac);
        UserManagerComponent userManagerComponent = null;
        UserInfoService userInfoService = null;

        try {
            userManagerComponent = ac.getBean("userManagerComponent", UserManagerComponent.class);
            userManagerComponent.noTransaction();
            System.out.println();
        } catch (Throwable e) {
            logger.info(e.getMessage(), e);
        }

        envCheck(ac);

        try {
            userInfoService = ac.getBean("userInfoServiceImpl", UserInfoService.class);
            userInfoService.noTransaction();
            System.out.println();
        } catch (Throwable e) {
            logger.info(e.getMessage(), e);
        }
        envCheck(ac);
        System.out.println();
    }


    private static void envCheck(ApplicationContext ac) {
        RedisComponent redisComponent = null;
        boolean isLocked = false;
        String lockKey = "LOCK_TEST";

        try {
            redisComponent = ac.getBean("redisComponent", RedisComponent.class);
            isLocked = redisComponent.acquireLock(lockKey, Millisecond.TEN_MINUS);
            System.out.println("redisComponent:check_result>" + isLocked);

            UserInfoService userInfoService = (UserInfoService) ac.getBean("userInfoServiceImpl");
            UserInfo userInfo = userInfoService.findById(1L);
            System.out.println("mysql_select:check_result>" + userInfo.getName());
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (isLocked) {
                redisComponent.releaseLock(lockKey);
            }
        }
    }
}
