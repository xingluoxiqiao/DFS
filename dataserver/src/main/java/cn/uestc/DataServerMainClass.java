
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc;

import cn.uestc.common.message.RequestMsg.HeartRequestMessage;
import cn.uestc.common.message.RequestMsg.LoginRequestMessage;
import cn.uestc.common.NettyServerBootstrap;
import cn.uestc.common.NettyClientBootstrap;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static cn.uestc.common.BootLoader.getProperties;
import static cn.uestc.common.Unit.KB;
import static cn.uestc.common.Unit.MB;
import static cn.uestc.common.initConfiguration.*;


/**
 * <p>
 * Description:
 * </p>
 *
 */
public class DataServerMainClass {
    private static String keyForDataserverPort=null;
    public static void main(String[] args) throws InterruptedException, UnknownHostException {


        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入dataserver服务器运行的端口号（默认9998）");
        keyForDataserverPort=scanner.nextLine();
        System.out.println("请输入文件存储的绝对路径文件夹");
        Global.partitionPath = scanner.nextLine();
        initBlock(Global.partitionPath);
        // 启动服务
        startServer(keyForDataserverPort);
        // 连接name server
        NettyClientBootstrap dataClient = new NettyClientBootstrap(getProperties("NAMESERVER_IP"),
                getProperties("NAMESERVER_PORT"), new NettyClientHandler(), "data server的netty client 准备登录");

        LoginRequestMessage message = new LoginRequestMessage();
        //获取本机ip
        String ip = InetAddress.getLocalHost().getHostAddress();
        /////////
        String dataserverid = String.valueOf(new Random().nextInt(10)+1);
        Global.dataserverID= Integer.parseInt(dataserverid);
        message.setDataserverid(dataserverid);
        message.setFreeBlockCount(Global.freeBlockCount);
        message.setDataserverPort(Integer.valueOf(keyForDataserverPort==null?"9998":keyForDataserverPort));
        dataClient.getSocketChannel().writeAndFlush(message);
        //关闭当前
        //dataClient.getSocketChannel().close();
        while (true) {
            TimeUnit.SECONDS.sleep(10);
            HeartRequestMessage heartRequestMessage = new HeartRequestMessage(Global.freeBlockCount,getProperties(dataserverid));
            dataClient.getSocketChannel().writeAndFlush(heartRequestMessage);
        }
    }

    private static void initBlock(String partitionPath) {

        File file = new File(partitionPath);
        if (file.exists()) {
            long freeSpace = file.getFreeSpace();
            Global.freeBlockCount = (freeSpace /(((long) BLOCK_SIZE) * KB));
        }
        else{
            throw  new RuntimeException("该文件不存在");
        }
    }


    private static void startServer(String keyForDataserverPort) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(keyForDataserverPort==null){
                    NettyServerBootstrap dataServer = new NettyServerBootstrap("9998",
                            new NettyServerHandler(), "data server的 netty server启动成功");
                }else{
                    NettyServerBootstrap dataServer = new NettyServerBootstrap(keyForDataserverPort,
                            new NettyServerHandler(), "data server的 netty server启动成功");
                }
            }
        }).start();
    }

}
