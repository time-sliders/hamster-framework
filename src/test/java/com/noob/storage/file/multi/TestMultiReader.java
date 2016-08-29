package com.noob.storage.file.multi;

import com.noob.storage.file.MultiThreadFileReaderTask;

import java.io.File;

/**
 * @author luyun
 * @since app6.1
 */
public class TestMultiReader {

    public static void main(String[] args) throws Exception {
        MultiThreadFileReaderTask reader = new MultiThreadFileReaderTask(
                new File("/Users/zhangwei/test/1"), 3, PrintFileLineTask.class, null);
        reader.start();
    }
}
