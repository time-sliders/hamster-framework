package com.noob.storage.thread.sync;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 * @since app6.1
 */
public class TestMultiThreadTask {

    public static void main(String[] args) {
        ConcurrentMap<String,Object> context = new ConcurrentHashMap<>();
        MultiThreadTask task = new MultiThreadTask(context);
        task.addSubTask(new SubTask_1());
        task.addSubTask(new SubTask_2());
        System.out.println("task start");
        task.start();
        System.out.println("task end");
        System.out.println(context.get("income"));
        System.out.println(context.get("caption"));
    }
}
