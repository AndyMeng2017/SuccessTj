package com.hcicloud.sap.study.netty.grpc.server;

import com.hcicloud.sap.study.netty.grpc.generated.RequestInfo;
import com.hcicloud.sap.study.netty.grpc.generated.ResponseInfo;
import com.hcicloud.sap.study.netty.grpc.generated.StudentServiceGrpc.StudentServiceImplBase;
import io.grpc.stub.StreamObserver;




public class StudentBizService extends StudentServiceImplBase{
    @Override
    public void getRealname(RequestInfo request, StreamObserver<ResponseInfo> responseObserver) {
        System.out.println("接收到客户端的信息:"+request.getUsername());
        ResponseInfo responseInfo = ResponseInfo.newBuilder().setRealname("Sam").build();
        responseObserver.onNext(responseInfo);
        responseObserver.onCompleted();
    }
}