package com.noob.storage.test.manual;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * @author 卢云(luyun)
 * @version app786
 * @since 2019.10.16
 */
public class H2O {

    public H2O() {

    }

    private Semaphore h = new Semaphore(2);
    private Semaphore o = new Semaphore(1);
    private CyclicBarrier b = new CyclicBarrier(2);

    /**
     * H
     */
    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        h.acquire();
        releaseHydrogen.run();
        if (h.availablePermits() == 0) {
            try {
                b.await();
                h.release(2);
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * O
     */
    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        o.acquire();
        releaseOxygen.run();
        try {
            b.await();
            o.release(1);
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}
