package com.noob.storage.rpc.netty.dft;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.lucene.util.NamedThreadFactory;

import java.net.InetSocketAddress;

public class NettyServer {

    private final int port;

    private NettyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new NettyServer(port).start();
    }

    public void start() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup(10,
                new NamedThreadFactory("Netty-Server-"));

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new ServerHeartBeatHandler());
                        }
                    });

            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
