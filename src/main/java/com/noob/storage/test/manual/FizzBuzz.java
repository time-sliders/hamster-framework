package com.noob.storage.test.manual;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;

/**
 * @author 卢云(luyun)
 * @version app786
 * @since 2019.10.16
 */
public class FizzBuzz {

    private int n;

    public FizzBuzz(int n) {
        this.n = n;
    }

    private AtomicBoolean isFinished = new AtomicBoolean(false);
    private Semaphore sa = new Semaphore(1);
    private Semaphore s3 = new Semaphore(0);
    private Semaphore s5 = new Semaphore(0);
    private Semaphore s35 = new Semaphore(0);

    public void fizz(Runnable printFizz) throws InterruptedException {
        while (!isFinished.get()) {
            s3.acquire();
            if (isFinished.get()) {
                break;
            }
            printFizz.run();
            sa.release();
        }
    }

    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (!isFinished.get()) {
            s5.acquire();
            if (isFinished.get()) {
                break;
            }
            printBuzz.run();
            sa.release();
        }
    }

    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (!isFinished.get()) {
            s35.acquire();
            if (isFinished.get()) {
                break;
            }
            printFizzBuzz.run();
            sa.release();
        }
    }

    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            sa.acquire();
            if ((i % 3 == 0) && (i % 5 == 0)) {
                s35.release();
            } else if (i % 3 == 0) {
                s3.release();
            } else if (i % 5 == 0) {
                s5.release();
            } else {
                printNumber.accept(i);
                sa.release();
            }
        }

        isFinished.set(true);
        s3.release();
        s5.release();
        s35.release();
    }

}
