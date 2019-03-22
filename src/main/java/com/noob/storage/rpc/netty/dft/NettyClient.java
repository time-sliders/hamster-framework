package com.noob.storage.rpc.netty.dft;

import com.noob.storage.utils.DateUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.lucene.util.NamedThreadFactory;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup(10,
                new NamedThreadFactory("Netty-Client-"));
        try {
            Bootstrap b = new Bootstrap();
            b.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("127.0.0.1", 8080))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.eventLoop().scheduleAtFixedRate(new Runnable() {
                                @Override
                                public void run() {
                                    ch.writeAndFlush(DateUtil.format(new Date(), DateUtil.DEFAULT_FORMAT));
                                }
                            }, 0L, 100L, TimeUnit.MILLISECONDS);
                            ch.pipeline().addLast(new ClientHeartBeatHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
