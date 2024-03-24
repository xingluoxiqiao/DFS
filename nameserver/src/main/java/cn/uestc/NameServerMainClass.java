
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc;

import cn.uestc.common.NettyServerBootstrap;

import static cn.uestc.common.BootLoader.getProperties;
import static cn.uestc.common.initConfiguration.NAMESERVER_PORT;


public class NameServerMainClass {


    public static void main(String[] args) {
        startServer();
    }
    
    private static void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyServerBootstrap dataServer = new NettyServerBootstrap(getProperties("NAMESERVER_PORT"),
                        new NettyServerHandler(), "name server的 netty server启动成功");
            }
        }).start();
    }

  
}
