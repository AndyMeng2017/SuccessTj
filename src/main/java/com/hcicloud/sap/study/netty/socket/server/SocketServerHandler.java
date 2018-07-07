package com.hcicloud.sap.study.netty.socket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class SocketServerHandler  extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("请求来自:"+ctx.channel().remoteAddress() + ",内容:" + msg);
        ctx.channel().writeAndFlush("from server:"+ UUID.randomUUID());
    }
}
