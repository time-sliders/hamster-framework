package com.noob.storage.test;

import com.noob.storage.business.model.UserInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 卢云(luyun)
 * @since 2019.11.19
 */
public class TestReflection {
    public static void main(String[] args)
            throws ClassNotFoundException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class clazz = Class.forName("com.noob.storage.business.model.UserInfo");
        Constructor[] constructors = clazz.getConstructors();
        UserInfo userInfo = (UserInfo) constructors[0].newInstance(null);
        userInfo.setId(1L);
        System.out.println(userInfo);
    }
}
