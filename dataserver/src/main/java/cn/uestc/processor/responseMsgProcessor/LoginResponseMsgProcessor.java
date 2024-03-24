package cn.uestc.processor.responseMsgProcessor;

import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.ResponseMsg.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Processor(msgType = MsgTypeEnum.LOGIN_RESPONSE)
public class LoginResponseMsgProcessor implements MessageProcessor<LoginResponseMessage> {
    private static final String keyForDataserverId= "dataserver.id";
    private static final String keyForNameserverIp = "nameserver.ip";
    private static final String keyForNameserverPort = "nameserver.port";


    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, LoginResponseMessage message) throws InterruptedException, IOException {
        log.info("dataserver登录成功");
    }
}
