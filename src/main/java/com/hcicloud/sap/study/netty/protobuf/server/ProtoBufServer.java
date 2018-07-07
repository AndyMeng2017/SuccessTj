package com.hcicloud.sap.study.netty.protobuf.server;

import com.hcicloud.sap.study.netty.protobuf.seconddemo.ModelInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ProtoBufServer {
    public static void main(String[] args){
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            //加载 Initializer
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    //将字节数组转换成Person对象和将Person对象转成字节数组,一共需要四个处理器
                                    pipeline.addLast(new ProtobufVarint32FrameDecoder());
                                    pipeline.addLast(new ProtobufDecoder(ModelInfo.Person.getDefaultInstance()));
                                    pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                                    pipeline.addLast(new ProtobufEncoder());

                                    //自定义处理器
                                    pipeline.addLast(new ProtobufServerHandler());
                                }
                            });
            //绑定监听端口
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
