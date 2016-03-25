package com.noob.storage.rpc.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * @author luyun
 * @since 2016.03.24
 */
public class Test {

    public static void main(String[] args) throws Throwable {

        RemoteFacade remoteFacade = (RemoteFacade) Proxy.newProxyInstance(RemoteFacadeImpl.class.getClassLoader(),
                RemoteFacadeImpl.class.getInterfaces(), new RemoteFacadeProxy());

        System.out.println(remoteFacade.execute("zhw"));
    }

}
