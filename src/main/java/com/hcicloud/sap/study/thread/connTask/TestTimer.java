package com.hcicloud.sap.study.thread.connTask;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring的定时任务
 * 2017年8月31日 21:41:27
 * @author menghaonan
 */
public class TestTimer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskSchedulerConfig.class);

        TimerTaskJob timerTaskJob=context.getBean(TimerTaskJob.class);

//        context.close();
    }
}
