package com.hcicloud.sap.study.thread.futureTask;

public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}
