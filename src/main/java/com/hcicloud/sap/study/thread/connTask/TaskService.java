package com.hcicloud.sap.study.thread.connTask;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 线程池任务
 * @author mingge
 *
 */
@Service
public class TaskService {

    @Async
    public void executeAsyncTask(int i){
        System.out.println("执行异步任务:"+i);
    }

    @Async
    public void executeAsyncTask1(int i){
        System.out.println("执行异步任务**************************:"+(i+i));
    }
}
