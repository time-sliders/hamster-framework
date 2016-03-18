package com.noob.storage.resource.base;

/**
 * 可初始化的资源
 */
public interface InitAble {

    /**
     * 初始化
     *
     * @return true 初始化成功
     */
    boolean init();

    /**
     * 销毁该资源，如关闭连接等
     */
    void destroy();

}
