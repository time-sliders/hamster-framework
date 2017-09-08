package com.noob.storage.file.multi;

import com.noob.storage.thread.sync.FileDataConsumerTask;
import com.noob.storage.thread.sync.MultiThreadFileSchedulerTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luyun
 * @since app6.1
 */
public class TestMultiReader {

    public static final String filePath = "/Users/zhangwei/IdeaProjects/hamster-framework/src/main/java/com/noob/storage/thread/sync/MultiThreadFileSchedulerTask.java";

    public static void main(String[] args) throws Exception {
        while (true) {
            try {
                //t1();
                t2();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void t1() {
        final AtomicInteger counter = new AtomicInteger();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File file = new File(filePath);
        MultiThreadFileSchedulerTask<Void> tsk = new MultiThreadFileSchedulerTask<Void>(file) {
            @Override
            protected FileDataConsumerTask<Void> createConsumerTask(Void v) {
                return new FileDataConsumerTask<Void>() {
                    public void processData(String lineData, Void context) throws InterruptedException {
                        counter.incrementAndGet();
                    }
                };
            }
        };
        tsk.start();
        stopWatch.stop();
        System.out.println("t1总耗时>" + stopWatch.getLastTaskTimeMillis() + "\tcount" + counter.get());
    }


    public static void t2() throws IOException, InterruptedException {
        final AtomicInteger counter = new AtomicInteger();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String s;
            while ((s = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(s)) {
                    counter.incrementAndGet();
                }
            }
        } finally {
            reader.close();
        }
        stopWatch.stop();
        System.out.println("t2总耗时>" + stopWatch.getLastTaskTimeMillis() + "\tcount" + counter.get());
    }
}