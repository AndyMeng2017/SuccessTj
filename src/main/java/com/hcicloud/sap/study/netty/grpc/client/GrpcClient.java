package com.hcicloud.sap.study.netty.grpc.client;

import com.hcicloud.sap.study.netty.grpc.generated.RequestInfo;
import com.hcicloud.sap.study.netty.grpc.generated.ResponseInfo;
import com.hcicloud.sap.study.netty.grpc.generated.StudentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8899)
                                .usePlaintext(true)
                                .build();
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(channel);
        RequestInfo requestInfo = RequestInfo.newBuilder().setUsername("sam").build();
        ResponseInfo responseInfo = blockingStub.getRealname(requestInfo);
        System.out.println(responseInfo.getRealname());
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
