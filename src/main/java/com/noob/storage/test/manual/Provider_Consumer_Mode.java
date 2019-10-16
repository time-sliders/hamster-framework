package com.noob.storage.test.manual;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 卢云(luyun)
 * @version app786
 * @since 2019.10.15
 */
public class Provider_Consumer_Mode<E> {

    private final List<E> queue = new LinkedList<E>();
    ReentrantLock lock = new ReentrantLock();

    class Provider implements Runnable{
        public void put(E e) {
            synchronized (queue) {
                if (queue.size() > 100) {
                    return;
                }
                queue.add(e);
            }
        }

        @Override
        public void run() {

        }
    }

    class Consumer {
        public E get() {
            synchronized (queue) {
                if (!queue.isEmpty()) {
                    return queue.get(0);
                }
                return null;
            }
        }
    }
}
