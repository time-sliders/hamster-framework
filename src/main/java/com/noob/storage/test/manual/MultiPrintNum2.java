package com.noob.storage.test.manual;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 卢云(luyun)
 * @version app786
 * @since 2019.10.16
 */
public class MultiPrintNum2 {

    public MultiPrintNum2() {

    }

    private CountDownLatch firstLatch = new CountDownLatch(1);
    private CountDownLatch secondLatch = new CountDownLatch(1);

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        firstLatch.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        firstLatch.await();
        printSecond.run();
        secondLatch.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        secondLatch.await();
        printThird.run();
    }
}
