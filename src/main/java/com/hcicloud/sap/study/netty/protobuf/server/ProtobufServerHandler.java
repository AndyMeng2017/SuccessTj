package com.hcicloud.sap.study.netty.protobuf.server;

import com.hcicloud.sap.study.netty.protobuf.seconddemo.ModelInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtobufServerHandler extends SimpleChannelInboundHandler<ModelInfo.Person> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ModelInfo.Person msg) throws Exception {
        System.out.println(msg.getName());
        System.out.println(msg.getAge());
        System.out.println(msg.getAddress());
    }
}
