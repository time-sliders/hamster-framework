package com.noob.storage.rpc.netty.dft;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.atomic.AtomicLong;

@ChannelHandler.Sharable // <-- 标示一个Channel- Handler可以被多个Channel安全地共享
public class ServerHeartBeatHandler extends ChannelInboundHandlerAdapter {

    private static AtomicLong l = new AtomicLong(0L);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received msg:" + in.toString(CharsetUtil.UTF_8));// 将消息记录到控制台
        ctx.write(Unpooled.copiedBuffer("server resp msg idx: " + l.getAndIncrement(), CharsetUtil.UTF_8));
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);// 将未决消息冲刷到远程节点，并且关闭该Channel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
