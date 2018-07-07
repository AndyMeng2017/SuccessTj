package com.hcicloud.sap.study.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKConnect implements Watcher {

    final static Logger log = LoggerFactory.getLogger(ZKConnect.class);

//    public static final String zkServerPath = "10.0.1.227:2181";
    public static final String zkServerPath = "10.0.1.227:2181,10.0.1.227:2182,10.0.1.227:2183";
    public static final Integer timeout= 5000;

    public static void main(String[] args) throws Exception {
        /**
         * 客户端和ZK服务端的连接是一个异步的过程
         * 当连接成功后，客户端会收到一个watch的通知
         *
         * 参数：
         * connectString：连接服务器的ip字符串
         *     比如："10.0.1.227:2181,10.0.1.227:2182,10.0.1.227:2183"
         *     可以是一个ip,也可以是多个ip，一个ip代表单机，多个ip代表集群
         *     也可以在ip后面加路径
         *
         * sessionTimeout：超时时间，心跳收不到了，那就超时
         * watcher：通知事件，如果有对应的事件触发，则会收到一个通知；如果不需要，那就设置为NULL
         * canBeReadOnly：可读，当这个物理机节点断开后，还是可以读到数据的，只是不能写，
         *      此时数据被读取到的可能是旧数据，此处建议设置为false，不推荐使用
         * sessionId：会话的ID
         * sessionPasswd：会话密码    当会话丢失后，可以依据session和sessionPasswd 重新获取会话
         */

        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZKConnect());

        log.debug("客户端开始连接zookeeper服务器……");
        log.debug("连接状态： {}", zk.getState());

        new Thread().sleep(2000);

        log.debug("连接状态： {}", zk.getState());
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        log.debug("接受到watch通知: {}", watchedEvent);
    }
}
