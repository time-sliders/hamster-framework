package com.noob.storage.rpc.socket.server;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Socket服务端ServerSocket， 接收到Socket请求之后处理Socket接口的定义
 */
public abstract class ServerService {

    protected Socket socket;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    private Logger logger = Logger.getLogger(ServerService.class);

    /**
     * 服务端处理客户端Socket请求
     *
     * @param socket 客户端连接Socket
     */
    public void execute(Socket socket) {
        try {
            this.socket = socket;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            start();
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(socket);
        }
    }

    public void println(String message) {

        if(StringUtils.isBlank(message)){
            return;
        }

        out.println(message);
    }

    public String readLine() throws IOException {
        return this.in.readLine();
    }

    /**
     * 处理socket请求的逻辑
     */
    public abstract void start();
}
