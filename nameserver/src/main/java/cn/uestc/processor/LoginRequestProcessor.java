package cn.uestc.processor;

import cn.uestc.annotation.Processor;
import cn.uestc.cache.BlockManager;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.ResponseMsg.LoginResponseMessage;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.LoginRequestMessage;
import cn.uestc.dataserver;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

@Slf4j
@Processor(msgType = MsgTypeEnum.LOGIN_REQUEST)
public class LoginRequestProcessor implements MessageProcessor<LoginRequestMessage> {

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, LoginRequestMessage requestMessage)  {
        log.info("dataserver开始登录");
        // 获取请求的ip地址
        InetSocketAddress inetSocketAddress = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
        String ip = inetSocketAddress.getAddress().getHostAddress();
        long freecounts = requestMessage.getFreeBlockCount();
        int port = requestMessage.getDataserverport();
        String dataserverid = requestMessage.getDataserverid();
        //遍历判断当前dataserver是否已经登录过
        Map<dataserver, Long> everyBlockCounts = BlockManager.getEveryBlockCounts();
        Set<Map.Entry<dataserver, Long>> entries = everyBlockCounts.entrySet();
        for(Map.Entry<dataserver, Long> entry:entries){
            if(entry.getKey().getId().equals(dataserverid)){
                log.info("Id为{}的data server重新登录", dataserverid);;
                continue;
            }
        }
        BlockManager.addBlockCount(new dataserver(ip,port,dataserverid),freecounts);
        BlockManager.printAllKeysAndValues(BlockManager.getEveryBlockCounts());;
        log.info("Id为{}的data server登录成功", dataserverid);
        log.info("当前节点剩余空块数{}",freecounts);
        channelHandlerContext.channel().writeAndFlush(new  LoginResponseMessage());
    }
}
