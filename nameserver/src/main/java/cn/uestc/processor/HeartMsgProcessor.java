package cn.uestc.processor;

import cn.uestc.annotation.Processor;
import cn.uestc.cache.BlockManager;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.HeartRequestMessage;
import cn.uestc.common.message.ResponseMsg.HeartResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
@Processor(msgType = MsgTypeEnum.HEARTBEAT_REQUST)
public class HeartMsgProcessor implements MessageProcessor<HeartRequestMessage> {


    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, HeartRequestMessage requestMessage)  {
        String dataserverid = requestMessage.getDataserverid();
        InetSocketAddress inetSocketAddress = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
        String ip = inetSocketAddress.getAddress().getHostAddress();
        BlockManager.updateBlockCount(ip,requestMessage.getFreeBlockCount());
        log.info("更新了 name server 的心跳消息{}",requestMessage.getFreeBlockCount());
        channelHandlerContext.channel().writeAndFlush( new HeartResponseMessage());
    }
}
