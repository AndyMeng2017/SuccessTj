package com.hcicloud.sap.study.netty.socket.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;


/**
 * 编写netty程序的步骤都比较相似：
 1、 启动group监听客户端请求
 2、编写Initializer添加handler
 3、自定义业务handler
 */
public class SocketClient {


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
                            //1、添加解码器,用于解释二进制内容
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            //2、编码器,用于计算消息的长度,并把消息长度以二进制的形式追加到消息的前面
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //3、socket编程中需要对字符串进行编码解码
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            //4、添加自定义处理器
                            pipeline.addLast(new SocketClientHandler());

                        }

                    });

            //绑定监听端口
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            eventLoopGroup.shutdownGracefully();

        }
    }

}
