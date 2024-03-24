
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc;

import cn.uestc.cache.BlockManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import cn.uestc.common.BootLoader;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.CommonMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * <p>
 * Description:
 * </p>
 * 
*/
public class NettyServerHandler extends SimpleChannelInboundHandler<CommonMessage> {

   
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, CommonMessage message)
            throws Exception {

        @SuppressWarnings("unchecked")
        //nameserver通过消息类型获取对应的处理器
        MessageProcessor<CommonMessage> processor = (MessageProcessor<CommonMessage>) BootLoader.getProcessor(message.getMsgType());
        processor.processMessage(channelHandlerContext, message);
        ReferenceCountUtil.release(message);

    }
}
