package com.noob.storage.test.manual;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 卢云(luyun)
 * @version app786
 * @since 2019.10.16
 */
public class MultiPrintNum {

    public MultiPrintNum() {

    }

    private ReentrantLock lock = new ReentrantLock();
    private volatile boolean firstFinished = false;
    private volatile boolean secondFinished = false;
    private Condition secondCondition = lock.newCondition();
    private Condition thridCondition = lock.newCondition();

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        firstFinished = true;
        lock.lock();
        try {
            secondCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        while (!firstFinished) {
            lock.lock();
            try {
                secondCondition.await();
            } finally {
                lock.unlock();
            }
        }
        printSecond.run();
        secondFinished = true;
        lock.lock();
        try {
            thridCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        while (!secondFinished) {
            lock.lock();
            try {
                thridCondition.await();
            } finally {
                lock.unlock();
            }
        }
        printThird.run();
    }
}
