package com.hcicloud.sap.study.thread.connTask;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TimerTaskJob {

//    @Scheduled(fixedRate=2000)





    //测试定时任务的开启
//    @Scheduled(cron="0/5 * * * * *")
//    public void test(){
//        System.out.println("我是定时任务:"+new Date().toString());
//    }



    //测试定时任务中的while（true）
//    @Scheduled(cron="0/1 * * * * *")
//    public void testWhile(){
//
////        while (true){
//        System.out.println("我是定时任务:"+new Date().toString());
//        try {
//            Thread.sleep(5000);
//
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////        }
//
//        //TODO 去掉 while 循环后，会发现时间相差 6 秒，可知同一个类中的方法，会等待上一个方法结束后，1 秒【定时任务设置的每一秒执行一次】才会执行。
//        //我是定时任务:Fri Sep 01 10:19:09 CST 2017
//        //我是定时任务:Fri Sep 01 10:19:15 CST 2017
//        //我是定时任务:Fri Sep 01 10:19:21 CST 2017
//        //我是定时任务:Fri Sep 01 10:19:27 CST 2017
//    }


}
