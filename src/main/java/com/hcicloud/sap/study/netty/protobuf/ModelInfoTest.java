package com.hcicloud.sap.study.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hcicloud.sap.study.netty.protobuf.firstdemo.ModelInfo;


/**
 *
 *引入：
     com.google.protobuf:protobuf-java:3.3.1
     com.google.protobuf:protobuf-java-util:3.3.1
 *
 * 在windows中安装 protobuf ，下载 protoc-3.5.1-win32.zip包
 * 生成 ModelInfo 对象的命令
 * D:\IdeaProjects\pingan\SuccessTj\src\com\hcicloud\sap\study\netty\protobuf>protoc --java_out=. Student.proto
 *
 *
 *
 * 这段代码简单的演示了将对象变成字节数组以及从字节数组中反序列化成java对象。
 */
public class ModelInfoTest {
    public static void main(String[] args){
        ModelInfo.Student student = ModelInfo.Student.newBuilder()
                                    .setName("mhn")
                                    .setAge(25)
                                    .setAddress("天通苑")
                                    .build();
        //转化成字节数组，而字节数组就可以在网络上传输了
        //比如说：从客户端传输到服务端，从服务端传输到客户端
        byte[] byteArray = student.toByteArray();

        //从字节数组中反序列成对象
        try {
            ModelInfo.Student student1 = ModelInfo.Student.parseFrom(byteArray);
            System.out.println(student1.getName());
            System.out.println(student1.getAge());
            System.out.println(student1.getAddress());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
