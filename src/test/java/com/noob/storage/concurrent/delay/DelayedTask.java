package com.noob.storage.concurrent.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author luyun
 * @since app5.9
 */
public class DelayedTask implements Delayed {

    private long delayedTime;

    public DelayedTask(long delayedTime) {
        this.delayedTime = System.currentTimeMillis() + delayedTime;
    }

    public long howLong(long start) {
        return System.currentTimeMillis() - start;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayedTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return delayedTime - o.getDelay(TimeUnit.MILLISECONDS) > 0 ? 1 : -1;
    }
}
