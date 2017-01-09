package com.noob.storage.file.multi;

import com.noob.storage.thread.sync.MultiThreadFileSchedulerTask;

import java.io.File;

/**
 * @author luyun
 * @since app6.1
 */
public class TestMultiReader {

    public static void main(String[] args) throws Exception {
        long l1 = System.currentTimeMillis();
        new MultiThreadFileSchedulerTask(new File("/Users/zhangwei/test/nio/1"),
                4, 100, PrintFileLineTask.class, null).start();
        System.out.println(System.currentTimeMillis() - l1);
    }
}
