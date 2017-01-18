package com.noob.storage.thread.ob;

import com.noob.storage.common.Millisecond;
import com.noob.storage.thread.sync.MultiThreadTask;

/**
 * @author luyun
 * @since 2017.01.18 (fund portfolio pre)
 */
public class MultiThreadTaskTest {

    public static void main(String[] args) {
        MultiThreadTask multiThreadTask = new MultiThreadTask(null, Millisecond.TEN_MINUS);
        multiThreadTask.addSubTask(new MySubTask());
        multiThreadTask.addSubTask(new MySubTask());
        multiThreadTask.addSubTask(new MySubTask());
        multiThreadTask.start();
        System.out.println("end");
    }

}
