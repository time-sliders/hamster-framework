package com.write.concurrent;

/**
 * @author luyun
 * @since app5.9
 */
public interface RunnableFuture<V> extends Runnable, Future<V> {

    void run();
}
