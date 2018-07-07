package com.hcicloud.sap.study.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZKConnectSessionWatch implements Watcher{
    final static Logger log = LoggerFactory.getLogger(ZKConnectSessionWatch.class);
    public static final String zkServerPath = "10.0.1.227:2181";
//    public static final String zkServerPath = "10.0.1.227:2181,10.0.1.227:2182,10.0.1.227:2183";
    public static final Integer timeout= 5000;

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZKConnectSessionWatch());
        long sessionId = zk.getSessionId();
        System.out.println(Long.toHexString(sessionId));
        byte[] sessionPasswd = zk.getSessionPasswd();

        log.debug("客户端开始连接zookeeper服务器……");
        log.debug("连接状态： {}", zk.getState());
        new Thread().sleep(1000);
        log.debug("连接状态： {}", zk.getState());

        //开始会话重连
        log.debug("开始会话重连……");
        new Thread().sleep(200);

        ZooKeeper zkSession = new ZooKeeper(zkServerPath, timeout,
                new ZKConnectSessionWatch(), sessionId,sessionPasswd);

        log.debug("连接状态： {}", zk.getState());
        new Thread().sleep(1000);
        log.debug("连接状态： {}", zk.getState());

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.debug("接受到watch通知: {}", watchedEvent);
    }
}

