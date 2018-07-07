package com.hcicloud.sap.study.netty.chat.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * 编写netty程序的步骤都比较相似：
 1、 启动group监听客户端请求
 2、编写Initializer添加handler
 3、自定义业务handler
 */
public class ChatClient {


    public static void main(String[] args){

        // 接收连接,但是不处理
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            //加载Initializer
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //1、按照分隔符切割消息
                            pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));

                            //2、socket编程中需要对字符串进行编码解码
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

                            //3、添加自定义处理器
                            pipeline.addLast(new ChatClientHandler());

                        }

                    });

            //连接服务端
            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for(;;) {
                channel.writeAndFlush(br.readLine() + "\r\n");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            eventLoopGroup.shutdownGracefully();

        }
    }

}
