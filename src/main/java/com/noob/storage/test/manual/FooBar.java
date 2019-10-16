package com.noob.storage.test.manual;

import java.util.concurrent.Semaphore;

/**
 * 两个不同的线程将会共用一个 FooBar 实例。其中一个线程将会调用 foo() 方法，另一个线程将会调用 bar() 方法。
 * 请设计修改程序，以确保 "foobar" 被输出 n 次。
 *
 * @author 卢云(luyun)
 * @version app786
 * @since 2019.10.16
 */
public class FooBar {
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    private Semaphore s_foo = new Semaphore(1);
    private Semaphore s_bar = new Semaphore(0);

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            s_foo.acquire();
            printFoo.run();
            s_bar.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            s_bar.acquire();
            printBar.run();
            s_foo.release();
        }
    }
}
