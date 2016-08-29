package com.noob.storage.file.multi;

import com.noob.storage.file.FileDataProcessTask;

import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 */
public class PrintFileLineTask extends FileDataProcessTask {

    @Override
    public void processLineData(String lineData, ConcurrentMap<String, Object> context) {
        System.out.println("println:" + lineData);
    }
}
