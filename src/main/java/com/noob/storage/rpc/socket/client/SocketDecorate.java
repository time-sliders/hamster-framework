package com.noob.storage.rpc.socket.client;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 客户端socket封装
 * 对socket进行封装，主要作用是对socket输入输出流进行封装,便于报文通行
 */
public class SocketDecorate extends Socket {

    private static final Logger log = LoggerFactory.getLogger(SocketDecorate.class);

    private PrintWriter out = null;
    private BufferedReader in = null;
    private String host;
    private int port;

    /**
     * TCP/IP 三次握手时的超时时间设置
     */
    private static final int SOCKET_CONNECT_TIMEOUT = 15000;
    /**
     * Socket 默认的超时时间
     */
    private static final int SOCKET_DEFAULT_TIMEOUT = 90000;

    /**
     * 最大重试次数
     */
    private static final int maxRetryTime = 3;


    public SocketDecorate(String host, int port) {
        //防止主机为null的情况下连接本地计算机
        if (StringUtils.isBlank(host) || port <= 0) {
            throw new RuntimeException("Socket创建失败：主机名或端口号不合法！");
        }
        this.host = host;
        this.port = port;
    }

    /**
     * 尝试与服务端建立连接,在连接失败的情况下重试三次
     */
    public boolean init() {
        for (int i = 0; i < maxRetryTime; i++) {
            try {
                SocketAddress address = new InetSocketAddress(host, port);
                this.connect(address, SOCKET_CONNECT_TIMEOUT);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getOutputStream(), "UTF-8")), true);
                in = new BufferedReader(new InputStreamReader(getInputStream(), "UTF-8"));
                this.setSoTimeout(SOCKET_DEFAULT_TIMEOUT);
                return true;
            } catch (Exception e) {
                log.error("与服务器[{}:{}]建立连接失败:{}",new Object[]{host,port,e.getMessage()});
            }
        }
        return false;
    }

    /**
     * 向输出流中输出一行数据
     */
    public void println(String message) {

        if (out == null || StringUtils.isBlank(message)) {
            return;
        }

        out.println(message);
    }

    /**
     * 从socket输入流中读取一行数据<br/>
     *
     * @throws IOException If an I/O error occurs
     */
    public String readLine() throws IOException {
        return in.readLine();
    }

    public void destroy() {
        IOUtils.closeQuietly(this);
    }

}
