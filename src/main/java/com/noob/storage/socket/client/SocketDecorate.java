package com.noob.storage.socket.client;

import com.noob.storage.exception.ProcessException;
import com.noob.storage.resource.base.InitAble;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 客户端socket封装
 * 对socket进行封装，主要作用是对socket输入输出流进行封装,便于报文通行
 */
public class SocketDecorate extends Socket implements InitAble {

    private static final Logger log = Logger.getLogger(SocketDecorate.class);

    private PrintWriter out = null;
    private BufferedReader in = null;
    private String host;
    private int port;

    /**
     * TCP/IP 三次握手时的超时时间设置
     */
    private static final int SOCKET_CONNECT_TIMEOUT = 3000;
    /**
     * Socket 默认的超时时间
     */
    private static final int SOCKET_DEFAULT_TIMEOUT = 60000;


    public SocketDecorate(String host, int port) {
        /**防止主机为null的情况下连接本地计算机*/
        if (StringUtils.isBlank(host) || port < 0) {
            throw new ProcessException("Socket创建失败：主机名或端口号不合法！");
        }
        this.host = host;
        this.port = port;
    }

    /**
     * 尝试与服务端建立连接,在连接失败的情况下重试三次
     */
    public boolean init() {
        for (int i = 0; i < 3; i++) {
            try {
                SocketAddress address = new InetSocketAddress(host, port);
                this.connect(address, SOCKET_CONNECT_TIMEOUT);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getOutputStream(), "UTF-8")), true);
                in = new BufferedReader(new InputStreamReader(getInputStream(), "UTF-8"));
                this.setSoTimeout(SOCKET_DEFAULT_TIMEOUT);
                return true;
            } catch (Exception e) {
                log.error("与服务器[" + host + ":" + port + "]建立连接失败:" + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 向输出流中输出一行数据
     */
    public void println(String message) {
        out.println(message);
    }

    /**
     * 从socket输入流中读取一行数据<br/><br/>
     * <strong>如果socket是假连接(socket连接成功但是无法发送和接受数据,
     * println方法无法检测异常,可以在这句执行之前添加超时时间,
     * 从而判断socket是否可用),那么程序将会一直在这个地方等待</strong><br/>
     *
     * @throws IOException            If an I/O error occurs
     */
    public String readLine() throws IOException {
        String message = in.readLine();
        return message;
    }

    public void destroy() {
        try {
            close();
        } catch (IOException e) {
        }
    }

}
