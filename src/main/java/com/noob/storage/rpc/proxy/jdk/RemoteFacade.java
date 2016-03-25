package com.noob.storage.rpc.proxy.jdk;

/**
 * 远程服务接口
 *
 * @author luyun
 * @since 2016.03.24
 */
public interface RemoteFacade {

    /**
     * 服务端的子类接口
     *
     * @param request 请求参数
     * @return 调用返回结果
     */
    Object execute(Object request) throws Throwable;

}
