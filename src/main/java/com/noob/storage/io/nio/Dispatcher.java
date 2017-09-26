package com.noob.storage.io.nio;

/**
 * 转发器 接口
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public interface Dispatcher<T> {

    void dispatch(T t);
}
