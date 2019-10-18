package com.noob.storage.test.manual;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Provider_Consumer_Mode<E> {

    private final Queue<E> queue = new LinkedList<>();
    private Semaphore permit = new Semaphore(5);
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

    public static void main(String[] args) throws IOException, InterruptedException {
        Provider_Consumer_Mode<String> m = new Provider_Consumer_Mode<String>();
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(m.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //noinspection InfiniteLoopStatement
        while (true) {
            String s = reader.readLine();
            if (StringUtils.isNotBlank(s)) {
                m.put(s);
            }
        }
    }
}
