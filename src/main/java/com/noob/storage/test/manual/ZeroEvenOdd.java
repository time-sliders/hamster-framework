package com.noob.storage.test.manual;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

/**
 * @author 卢云(luyun)
 * @version app786
 * @since 2019.10.16
 */
public class ZeroEvenOdd {

    private int n;

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    private volatile AtomicInteger g = new AtomicInteger(1);
    private Semaphore s_0 = new Semaphore(1);
    private Semaphore s_1 = new Semaphore(0);
    private Semaphore s_2 = new Semaphore(0);

    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            s_0.acquire();
            printNumber.accept(0);
            if ((g.get() % 2) != 0) {
                s_1.release();
            } else {
                s_2.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            s_2.acquire();
            printNumber.accept(i);
            g.incrementAndGet();
            s_0.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            s_1.acquire();
            printNumber.accept(i);
            g.incrementAndGet();
            s_0.release();
        }
    }

}
