package com.noob.storage.nio;

import com.noob.storage.io.nio.client.NIOClient;
import com.noob.storage.io.nio.server.NIOServer;

/**
 * @author luyun
 */
public class MyNIOServerTest {

    public static void main(String[] args) {
        // 启动服务器
        NIOServer nioServer = new NIOServer(8080);
        nioServer.init();
        nioServer.start();


        new NIOClient("localhost", 8080).start();
    }

}
