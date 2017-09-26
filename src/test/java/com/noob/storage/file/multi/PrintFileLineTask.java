package com.noob.storage.file.multi;

import com.noob.storage.thread.sync.FileDataConsumerTask;

import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 */
public class PrintFileLineTask extends FileDataConsumerTask<Void> {

    @Override
    public void processData(String lineData,Void v) throws InterruptedException {
        Thread.sleep(100L);
    }
}
