package com.hcicloud.sap.study.netty.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class NioServer {
    private static Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        //创建一个服务端的socket channel，但是还未绑定监听地址
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //非阻塞
        serverSocketChannel.configureBlocking(false);

        //通过ServerSocet为ServerSocketChannel指定监听地址。如果不绑定的话,调用ServerSocketChannel的accept方法会抛出NotYetBoundException
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        //注册连接建立事件
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //一直阻塞,直到有感兴趣的事件发生,select方法才会返回
            selector.select();
            //注册selector在上的事件可能不止一个,这些事件也可能同时发生,所以这里是SelectionKey Set集合
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            for (Iterator<SelectionKey> iterator = selectedKeys.iterator(); iterator.hasNext();) {
                SelectionKey selectionKey = iterator.next();
                final SocketChannel client;
                //isAcceptable==true，说明客户端和服务端已经建立好连接了
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    client = server.accept();

                    client.configureBlocking(false);
                    client.register(selector,   SelectionKey.OP_READ);

                    String key = "【"+ UUID.randomUUID() +"】";
                    clientMap.put(key, client);
                    continue;
                }

                //isReadable==true,说明可以从SocketChannel中读取数据了
                if (selectionKey.isReadable()) {
                    client = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    //从channel中读取数据
                    int count = client.read(readBuffer);
                    if (count > 0) {
                        readBuffer.flip();
                        Charset charset = Charset.forName("utf-8");
                        //将bytebuffer转换成字符串
                        String receiveMessage = String.valueOf(charset.decode(readBuffer).array());
                        System.out.println(client + "：" +receiveMessage);

                        //找出发送消息的客户端
                        String sendKey = "";
                        for (String key : clientMap.keySet()) {
                            SocketChannel socketChannel = clientMap.get(key);
                            if (socketChannel == client){
                                sendKey = key;
                                break;
                            }
                        }

                        for (String key : clientMap.keySet()) {
                            SocketChannel socketChannel = clientMap.get(key);
                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                            writeBuffer.put((sendKey + ":" +receiveMessage).getBytes());
                            writeBuffer.flip();
                            socketChannel.write(writeBuffer);
                        }
                    }
                }
            }

            //清除事件
            selectedKeys.clear();
        }
    }


}
