package com.noob.storage.test.manual;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Provider_Consumer_Mode<E> {

    private final Queue<E> queue = new LinkedList<>();
    private Semaphore permit = new Semaphore(10);
    private Semaphore remain = new Semaphore(0);

    public void put(E e) throws InterruptedException {
        permit.acquire();
        queue.add(e);
        remain.release();
    }

    public E get() throws InterruptedException {
        remain.acquire();
        E e = queue.poll();
        if (e != null) {
            permit.release();
        }
        return e;
    }


}
