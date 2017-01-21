package com.noob.storage.io.nio.server;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * ServerSocketChannel 接收到客户端连接时的处理器
 * <p>
 * 该Actor只支持一读一写模式
 *
 * @author luyun
 */
public class Actor extends NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(Actor.class);

    private static final int STATUS_READ = 0;

    private static final int STATUS_WRITE = 1;

    private int state = 0;

    private String responseMsg = null;

    public Actor(SelectionKey selectionKey) {
        super(selectionKey);
    }

    @Override
    public void run() {
        if (state == STATUS_READ) {
            read();
        } else if (state == STATUS_WRITE) {
            write();
        }
    }

    private void write() {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        try {

            //将处理好的数据输出到客户端
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 8);
            ByteArrayInputStream bos = new ByteArrayInputStream(responseMsg.getBytes());
            byte[] dataBuffer = new byte[1024 * 8];

            //看一下有没有什么方式可以不用这个dataBuffer
            int num;
            while ((num = bos.read(dataBuffer)) > 0) {
                byteBuffer.put(dataBuffer, 0, num);
                socketChannel.write(byteBuffer);
            }

            bos.close();

        } catch (IOException e) {

            logger.error(e.getMessage(), e);
        } finally {

            //关闭资源
            IOUtils.closeQuietly(socketChannel);
            selectionKey.cancel();
        }

    }

    private void read() {
        try {

            //读取接收到的数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 8);
            byteBuffer.clear();
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            int readNum;
            StringBuilder sb = new StringBuilder();
            while ((readNum = socketChannel.read(byteBuffer)) > 0) {

                //切换到读取模式
                byteBuffer.flip();

                //解析数据
                byte[] readArray = new byte[readNum];
                System.arraycopy(byteBuffer.array(), 0, readArray, 0, readNum);
                sb.append(new String(readArray));

                //切换到写入模式
                byteBuffer.clear();
            }

            if (readNum == -1) {
                //如果read方法返回-1表示底层信道已经关闭
                socketChannel.close();
                return;
            }

            //TODO 业务处理
            responseMsg = "收到了!";
            System.out.println(sb.toString());

            //处理完毕之后切换到写模式
            state = STATUS_WRITE;
            selectionKey.interestOps(SelectionKey.OP_WRITE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
