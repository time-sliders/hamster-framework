package com.noob.storage.file.multi;

import com.noob.storage.thread.sync.MultiThreadFileReaderTask;

import java.io.File;

/**
 * @author luyun
 * @since app6.1
 */
public class TestMultiReader {

    public static void main(String[] args) throws Exception {
        int i = 0;
        while (i++ <= 10000) {
            new MultiThreadFileReaderTask(new File("/Users/zhangwei/test/nio/1"),
                    5, 100, PrintFileLineTask.class, null).start();
        }
    }
}
