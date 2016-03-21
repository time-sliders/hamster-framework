package com.noob.storage.socket.server;

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
     * 是否是多线程模式<br/>
     * 当该字段值为false的时候,执行startServer方法,主线程会进入accept等待,直到用户连接<br/>
     * 当值为true时,会以线程的方式启动ServerSocket,不会影响主线程执行
     */
    private boolean isThreadMod = true;

    /**
     * 线程池
     */
    private ExecutorService threadPool = null;

    /**
     * 任何服务端程序处理socket请求的service
     */
    private Class<? extends ServerService> serverServiceClass;

    /**
     * 创建一个服务端socket监听，无线程池
     *
     * @param port               监听端口
     * @param serverServiceClass 服务端处理service，继承自ServerService
     */
    public ServerSocketProxy(int port, Class<? extends ServerService> serverServiceClass) throws IOException {
        this(port, DEFAULT_POOL_SIZE,serverServiceClass,true);
    }

    /**
     * 创建一个服务端socket监听
     *
     * @param port               监听端口
     * @param poolSize           服务端线程池大小，用于控制并发数量
     * @param serverServiceClass 服务端处理service，继承自ServerService
     * @param isThreadMod        是否是线程模式启动服务
     */
    public ServerSocketProxy(int port, int poolSize, Class<? extends ServerService> serverServiceClass,
                             boolean isThreadMod) throws IOException {
        this(port,Executors.newScheduledThreadPool(poolSize),serverServiceClass,isThreadMod);
    }

    /**
     * 创建一个服务端socket监听
     *
     * @param port               监听端口
     * @param threadPool         自定义服务端线程池
     * @param serverServiceClass 服务端处理service，继承自ServerService
     * @param isThreadMod        是否是线程模式启动服务
     */
    public ServerSocketProxy(int port, ExecutorService threadPool, Class<? extends ServerService> serverServiceClass,
                             boolean isThreadMod) throws IOException {
        super(port);
        this.threadPool = threadPool;
        this.isThreadMod = isThreadMod;
        this.serverServiceClass = serverServiceClass;
    }

    /**
     * 启动这个服务端socket
     */
    public void startServer() {

        Thread serverLoopAcceptThread = new ServerLoopAcceptThread();

        if (isThreadMod) {
            /***在子线程中启动服务*/
            serverLoopAcceptThread.start();
            log.info(">>>>>>>>>>>>Socket Server started in RUNNING mode<<<<<<<<<<<<");
        } else {
            /**在当前线程启动服务*/
            log.info(">>>>>>>>>>>>Socket Server started in RUNNING mode<<<<<<<<<<<<");
            serverLoopAcceptThread.run();
        }
    }


    class ServerLoopAcceptThread extends Thread{

        public void run(){

            while (!isClosed() && !isInterrupted()) {
                try {
                    Socket socket = accept();
                    ServerService serverServer = serverServiceClass.newInstance();
                    Runnable runnable = createServerTask(serverServer, socket);
                    if (threadPool == null) {
                        new Thread(runnable).start();
                    } else {
                        threadPool.execute(runnable);
                    }
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