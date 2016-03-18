package com.noob.storage.thread.ob;

/**
 * 计数器
 * <p>
 * 子线程成多线程操作该对象，该对象的属性值变更必须 <strong><code>synchronized</code></strong>，
 * 如果需要增加其他字段，可以新建子类处理
 * </p>
 */
public class Counter {

    private int success = 0;
    private int fail = 0;
    private int all = 0;

    public synchronized void successPlus() {
        success++;
    }

    public int getSuccess() {
        return success;
    }

    public synchronized void failPlus() {
        fail++;
    }

    public int getFail() {
        return fail;
    }

    public int getAll() {
        return all;
    }

    public synchronized void allPlus() {
        all++;
    }

    public boolean isFinish() {
        synchronized (this) {
            return (success + fail) >= all;
        }
    }

    @Override
    public String toString() {
        return "Counter [success=" + success + ", fail=" + fail + ", all="
                + all + "]";
    }

}
