package com.hcicloud.sap.study.netty.http;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * 编写netty程序的步骤都比较相似：
 1、 启动group监听客户端请求
 2、编写Initializer添加handler
 3、自定义业务handler
 */
public class HttpServer {


    public static void main(String[] args){

        //todo EventLoopGroup类似死循环，一直监听8899端口是否有请求到达。

        // 接收连接,但是不处理
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 真正处理连接的group
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            //加载Initializer
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("httpServerCodec", new HttpServerCodec());
                            pipeline.addLast("testHttpServerHandler", new HttpServerHandler());

                        }

                    });
            //绑定监听端口
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();

        }
    }

}
