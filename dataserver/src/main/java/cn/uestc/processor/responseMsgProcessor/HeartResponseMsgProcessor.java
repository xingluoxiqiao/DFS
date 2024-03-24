package cn.uestc.processor.responseMsgProcessor;

import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.ResponseMsg.HeartResponseMessage;
import cn.uestc.common.message.MsgTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Processor(msgType = MsgTypeEnum.HEARTBEAT_RESPONSE)
public class HeartResponseMsgProcessor implements MessageProcessor<HeartResponseMessage>{
    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, HeartResponseMessage message) {
       log.info("收到心跳响应消息");
    }
}


