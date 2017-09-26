package com.noob.storage.nio;

import com.noob.storage.io.nio.demo.NIOClient;
import com.noob.storage.io.nio.demo.NIOServer;

/**
 * @author luyun
 */
public class MyNIOServerTest {

    public static void main(String[] args) throws InterruptedException {
        // 启动服务器
        NIOServer nioServer = new NIOServer(8080);
        nioServer.init();
        nioServer.start();

        Thread.sleep(1000L);


        new NIOClient(null, 8080).start();
    }

}
