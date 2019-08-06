package com.noob.storage.io.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * NIO 服务端组件
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private ServerSocketChannel ssc;

    public Server(int port, int backlog, boolean blocking) throws Exception {
        ssc = ServerSocketChannel.open();
        ssc.socket().setReuseAddress(true);
        ssc.socket().bind(new InetSocketAddress(port), backlog);
        ssc.configureBlocking(blocking);
    }

    public void runServer() throws Exception {
        DispatcherN dispatcherN = new DispatcherN();
        Acceptor acceptor = new Acceptor(ssc, dispatcherN);
        new Thread(acceptor).start();
        dispatcherN.run();
    }
}