package com.hcicloud.sap.study.multithreading.concurrentMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * https://blog.csdn.net/zjysource/article/details/50642728
 * 参考博客
 * 2018年4月23日 15:03:22
 *
 *
 */
public class FileCache {

    private static Collection<String> cachedFiles = new HashSet<>();
    private ConcurrentMap<String, CountDownLatch> cacheTimestamp = new ConcurrentHashMap<>();

    public void tryCache(String file)
    {
        CountDownLatch signal = cacheTimestamp.putIfAbsent(file, new CountDownLatch(1));
        try {
            if (signal == null) {

                signal = cacheTimestamp.get(file);
                try {
                    if (!cachedFiles.contains(file)) {

                        System.out.println(Thread.currentThread().getName() + " sleep 10.");
                        // 这里加入线程等待，执行说明：当多路线程并发进入tryCache访问的时【file的值相同】，只有一个线程在占用
                        Thread.sleep(10);

                        cachedFiles.add(file);
                    }
                } finally {
                    signal.countDown();
                    cacheTimestamp.remove(file);
                    System.out.println(Thread.currentThread().getName() + " await end.");
                }
            } else {
                signal.await();
                System.out.println(Thread.currentThread().getName() + " await begin.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    class InnerThread extends Thread{
        public void run() {
            try {
                tryCache("aaa");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){

        int LATCH_SIZE = 10;

        FileCache fileCache = new FileCache();
        for(int i=0; i<LATCH_SIZE; i++){
            fileCache.new InnerThread().start();
        }

    }






}
