package com.write.concurrent;

import java.util.concurrent.*;

/**
 * @author luyun
 * @since app5.9
 */
public class FutureTask<V> implements RunnableFuture<V> {

    private volatile int state;
    private static final int NEW = 0;
    private static final int COMPLETING = 1;
    private static final int NORMAL = 2;
    private static final int EXCEPTIONAL = 3;
    private static final int CANCELED = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED = 6;

    private Callable<V> callable;
    private Object outcome;
    private volatile Thread runner;

    @SuppressWarnings("unchecked")
    public V report(int s) throws ExecutionException {
        Object x = outcome;
        if (state == NORMAL)
            return (V) x;
        if (state >= CANCELED)
            throw new CancellationException();
        throw new ExecutionException((Throwable) x);
    }

    public FutureTask(Callable<V> callable) {
        if (callable == null)
            throw new NullPointerException();
        this.callable = callable;
        this.state = NEW;
    }

    public FutureTask(Runnable runnable, V result) {
        this.callable = Executors.callable(runnable, result);
        this.state = NEW;
    }

    @Override
    public void run() {
        if (state != NEW ||
                !UNSAFE.compareAndSwapObject(this, runnerOffset,
                        null, Thread.currentThread()))
            return;

        try {
            Callable<V> c = callable;
            if (c != null && state == NEW) {
                V result;
                boolean ran;
                try {
                    result = c.call();
                    ran = true;
                } catch (Throwable ex) {
                    result = null;
                    ran = false;
                    //do Exception
                }

                if (ran && UNSAFE.compareAndSwapObject(this,state,NEW,COMPLETING)){
                    outcome = result;
                    UNSAFE.putOrderedInt(this,stateOffset,NORMAL);
                }

            }
        } finally {
            runner = null;
            int s = state;
            if (s >= INTERRUPTING)
                System.out.println();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return state >= CANCELED;
    }

    @Override
    public boolean isDone() {
        return state != NEW;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public V get(Long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = FutureTask.class;
            stateOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("runner"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }

    }

}
