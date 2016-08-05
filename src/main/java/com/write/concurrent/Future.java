package com.write.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @param <V> the result type returned by this Future's {@code get} method;
 * @author luyun
 * @since app5.9
 */
public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCanceled();

    boolean isDone();

    V get() throws InterruptedException, ExecutionException;

    V get(Long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException;
}
