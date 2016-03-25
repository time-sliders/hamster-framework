package com.noob.storage.rpc.proxy.jdk;

/**
 * @author luyun
 * @since 2016.03.24
 */
public class RemoteFacadeImpl implements RemoteFacade{

    public Object execute(Object request) throws Throwable {
        return String.format("class: %s. param: %s.",this.getClass().getCanonicalName(),request);
    }
}
