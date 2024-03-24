package cn.uestc;

import cn.uestc.common.BootLoader;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.CommonMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<CommonMessage> {
    @Override
    @SuppressWarnings("unchecked")
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, CommonMessage message) throws Exception {
        log.info("收到消息"+ message.getMsgType());
        MessageProcessor<CommonMessage> processor = (MessageProcessor<CommonMessage>) BootLoader.getProcessor(message.getMsgType());
        processor.processMessage(channelHandlerContext, message);
        ReferenceCountUtil.release(message);
    }

}
