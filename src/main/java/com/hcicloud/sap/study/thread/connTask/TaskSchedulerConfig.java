package com.hcicloud.sap.study.thread.connTask;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("com.hcicloud.sap.study.thread.connTask")
@EnableScheduling//开启对定时器的支持
public class TaskSchedulerConfig {
}
