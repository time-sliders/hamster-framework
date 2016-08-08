package com.noob.storage.concurrent.delay;

import java.util.concurrent.DelayQueue;

/**
 * @author luyun
 * @since app5.9
 */
public class Task {

    public static DelayQueue<DelayedTask> delayQueue = new DelayQueue<DelayedTask>();

    public static void main(String[] args) throws Exception {
        delayQueue.put(new DelayedTask(1000));
        delayQueue.put(new DelayedTask(2000));
        delayQueue.put(new DelayedTask(5000));
        delayQueue.put(new DelayedTask(4000));
        delayQueue.put(new DelayedTask(3000));
        Long start = System.currentTimeMillis();
        while(true){
            DelayedTask getTask = delayQueue.take();
            System.out.println(getTask.howLong(start));
        }
    }

}
