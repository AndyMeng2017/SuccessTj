package com.hcicloud.sap.study.netty.protobuf.client;

import com.hcicloud.sap.study.netty.protobuf.seconddemo.ModelInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtoBufClient {
    public static void main(String[] args){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            //加载Initializer
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //将字节数组转换成Person对象和将Person对象转成字节数组,一共需要四个处理器
                            channelPipeline.addLast(new ProtobufVarint32FrameDecoder());
                            channelPipeline.addLast(new ProtobufDecoder(ModelInfo.Person.getDefaultInstance()));
                            channelPipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            channelPipeline.addLast(new ProtobufEncoder());

                            //自定义处理器
                            channelPipeline.addLast(new ProtobufClientHandler());
                        }
                    });

            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("localhost",8899).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            eventLoopGroup.shutdownGracefully();
        }
    }
}
