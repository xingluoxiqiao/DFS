
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common;


import cn.uestc.common.message.CommonMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import cn.uestc.exception.CommonException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerBootstrap {
    private int port;
    private String logInfo;
    private SimpleChannelInboundHandler<CommonMessage> handler;
    public NettyServerBootstrap(String port, SimpleChannelInboundHandler<CommonMessage> handler, String logInfo) {
        this.port = Integer.valueOf(port);
        this.handler = handler;
        this.logInfo = logInfo;
        try {
            bind();
        } catch (InterruptedException e) {
            throw new CommonException(StatusCodeEnum.THREAD_INTERRUPT_ERROR);
        }
    }

    private void bind() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        // 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 保持长连接状态
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new ObjectEncoder());
                p.addLast(new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));
                p.addLast(handler.getClass().newInstance());
            }
        });
        ChannelFuture f = bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info(logInfo);
            } else {
                log.error("Server failed to start", future.cause());
            }
        }).sync();
        f.channel().closeFuture().sync();
    }
}
