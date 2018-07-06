package com.noob.storage.utils.send;

/**
 * @author LuYun
 * @since 2018.05.10
 */
public interface BucketInterface<E> {

    void send(E e) throws InterruptedException;

}
