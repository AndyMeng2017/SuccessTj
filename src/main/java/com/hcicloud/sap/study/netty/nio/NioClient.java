package com.hcicloud.sap.study.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        //非阻塞
        socketChannel.configureBlocking(false);

        //客户端同样需要selector去监听channel
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectedKeys) {
                //isConnectable=true,说明已经跟服务端建立连接了
                if (selectionKey.isConnectable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put((LocalDate.now() + "连接成功").getBytes());
                        writeBuffer.flip();
                        client.write(writeBuffer);


                        //异步给服务端写信息,避免阻塞主线程
                        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    writeBuffer.clear();
                                    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                    try {
                                        String sendMessage = bufferedReader.readLine();
                                        writeBuffer.put(sendMessage.getBytes());
                                        writeBuffer.flip();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                    //一旦客户端和服务端建立连接了,立刻注册读事件
                    client.register(selector, SelectionKey.OP_READ);
                    continue;
                }

                if(selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel)selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int count = client.read(readBuffer);
                    if (count > 0) {
                        String receiveMessage = new String(readBuffer.array(),0,count);
                        System.out.println(receiveMessage);
                    }
                }
            }

            //清除事件
            selectedKeys.clear();
        }
    }



}
