package com.noob.storage.rpc.socket.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Socket服务器建立代理<br/>
 * 可以简单的创建一个固定线程池大小的ServerSocket
 */
public class ServerSocketProxy extends ServerSocket {

    private Logger log = Logger.getLogger(ServerSocketProxy.class);

    private static final int DEFAULT_POOL_SIZE = 20;

    /**
     * 线程池
     */
    private ExecutorService threadPool = null;

    /**
     * 任何服务端程序处理socket请求的service
     */
    private Class<? extends ServerService> serverServiceClass;

    /**
     * 创建一个服务端socket监听
     *
     * @param port               监听端口
     * @param serverServiceClass 服务端处理service，继承自ServerService
     */
    public ServerSocketProxy(int port, Class<? extends ServerService> serverServiceClass) throws IOException {
        this(port, DEFAULT_POOL_SIZE, serverServiceClass);
    }

    /**
     * 创建一个服务端socket监听
     *
     * @param port               监听端口
     * @param poolSize           服务端线程池大小，用于控制并发数量
     * @param serverServiceClass 服务端处理service，继承自ServerService
     */
    public ServerSocketProxy(int port, int poolSize, Class<? extends ServerService> serverServiceClass) throws IOException {
        this(port, Executors.newScheduledThreadPool(poolSize), serverServiceClass);
    }

    /**
     * 创建一个服务端socket监听
     *
     * @param port               监听端口
     * @param threadPool         自定义服务端线程池
     * @param serverServiceClass 服务端处理service，继承自ServerService
     */
    public ServerSocketProxy(int port, ExecutorService threadPool, Class<? extends ServerService> serverServiceClass)
            throws IOException {
        super(port);
        this.threadPool = threadPool;
        this.serverServiceClass = serverServiceClass;
    }

    /**
     * 启动这个服务端socket
     */
    public void startServer() {
        /***在子线程中启动服务*/
        new ServerLoopAcceptThread().start();
        log.info(">>>>>>>>>>>>Socket Server started in RUNNING mode<<<<<<<<<<<<");
    }


    class ServerLoopAcceptThread extends Thread {

        public void run() {

            while (!isClosed() && !isInterrupted()) {
                try {
                    Socket socket = accept();
                    ServerService serverServer = serverServiceClass.newInstance();
                    Runnable runnable = createServerTask(serverServer, socket);
                    threadPool.execute(runnable);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    /**
     * 创建Runnable
     */
    private Runnable createServerTask(final ServerService serverService, final Socket socket) {
        return new Runnable() {
            public void run() {
                serverService.execute(socket);
            }
        };
    }

}