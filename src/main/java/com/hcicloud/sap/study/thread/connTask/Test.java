package com.hcicloud.sap.study.thread.connTask;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring的多线程
 * 2017年8月31日 21:41:27
 * @author menghaonan
 */
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(TaskExecutorConfig.class);
        TaskService taskService=context.getBean(TaskService.class);
        for(int i=0;i<20;i++){
            taskService.executeAsyncTask(i);
            taskService.executeAsyncTask1(i);
        }
        //最后可以根据结果可以看出结果是并发执行而不是顺序执行的呢
        context.close();
    }
}
