package com.hcicloud.sap.study.netty.protobuf.client;

import com.hcicloud.sap.study.netty.protobuf.seconddemo.ModelInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

public class ProtobufClientHandler extends SimpleChannelInboundHandler<ModelInfo.Person> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ModelInfo.Person person) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        ModelInfo.Person person = ModelInfo.Person.newBuilder()
                                            .setName("mhn")
                                            .setAge(25)
                                            .setAddress("天通苑")
                                            .build();
        ctx.writeAndFlush(person);
    }
}
