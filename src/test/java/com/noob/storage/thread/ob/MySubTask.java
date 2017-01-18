package com.noob.storage.thread.ob;

import com.noob.storage.thread.sync.SubTask;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author luyun
 * @since 2017.01.18 (fund portfolio pre)
 */
public class MySubTask extends SubTask {

    private static final AtomicLong index = new AtomicLong();

    public MySubTask() {
        super.setName("MySubTask-" + index.getAndIncrement());
    }

    @Override
    protected void doBusiness(ConcurrentMap<String, Object> context) {
        try {
            Thread.sleep(new Random().nextInt(10000));
            System.out.println(getName() + "\tend");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
