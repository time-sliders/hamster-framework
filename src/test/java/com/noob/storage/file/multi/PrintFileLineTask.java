package com.noob.storage.file.multi;

import com.noob.storage.thread.sync.FileDataConsumerTask;

import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 */
public class PrintFileLineTask extends FileDataConsumerTask {

    @Override
    public void processLineData(String lineData, ConcurrentMap<String, Object> context) {
        //System.out.println(lineData);
    }
}
