package com.noob.storage.io;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class BookPrinter {

    private static volatile int speed = 8;
    private static volatile boolean blocked = false;
    private static final Object blockObj = new Object();

    public static void main(String[] args) {
        String filePath = "";
        String startLine = "";

        new Thread(() -> {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    try {
                        String cmd = reader.readLine();
                        if ("-".equals(cmd)) {
                            blocked = true;
                            System.out.println("reading paused");
                        } else if ("=".equals(cmd)) {
                            blocked = false;
                            synchronized (blockObj) {
                                blockObj.notifyAll();
                            }
                            System.out.println("continue reading");
                        } else {
                            speed = Integer.valueOf(cmd);
                            System.out.println("current read speed changed to : " + speed);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                IOUtils.closeQuietly(reader);
                System.out.println("reader thread reclaimed");
            }
        }).start();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String s;
            boolean isStarted = false;
            while ((s = reader.readLine()) != null) {
                if (!isStarted) {
                    if (s.contains(startLine)) {
                        isStarted = true;
                        System.out.println("start reading...");
                    } else {
                        continue;
                    }
                }

                System.out.println(s);
                try {
                    Thread.sleep(s.length() * 10 * speed);
                    if (blocked) {
                        synchronized (blockObj) {
                            blockObj.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("read thread finished");
        }
    }
}
