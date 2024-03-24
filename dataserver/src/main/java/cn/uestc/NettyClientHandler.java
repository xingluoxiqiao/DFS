package cn.uestc;

import cn.uestc.common.BootLoader;
import cn.uestc.common.message.CommonMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<CommonMessage> {

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, CommonMessage message) throws Exception {

        // 获取消息类型对应的处理器
        BootLoader.getProcessor(message.getMsgType()).processMessage(channelHandlerContext, message);

        // 释放消息引用
        ReferenceCountUtil.release(message);
    }
}