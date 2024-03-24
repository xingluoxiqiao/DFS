package cn.uestc.processor;

import cn.uestc.annotation.Processor;
import cn.uestc.cache.BlockManager;
import cn.uestc.common.message.ResponseMsg.writeResponseMsg;
import cn.uestc.dataserver;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.writeRequestMsg;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
@Slf4j
@Processor(msgType = MsgTypeEnum.WRITE_REQUEST)
public class writeRequestProcessor implements MessageProcessor<writeRequestMsg> {

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, writeRequestMsg requestMessage) throws InterruptedException, IOException {
            int Totalblockcounts = requestMessage.getblockCounts();
        String uuid = requestMessage.getUuid();
        log.info("连接到client");
            // 创建一个writeResponseMsg对象
            Map<dataserver, Long> everyBlockCounts = BlockManager.getEveryBlockCounts();
            BlockManager.sortByValue(everyBlockCounts);
            //取剩余空块数最多的三个dataserver
            List<Map.Entry<dataserver, Long>> topThree = everyBlockCounts.entrySet().stream()
                    .sorted(Comparator.comparingLong(Map.Entry::getValue))
                    .limit(3)
                    .toList();
            List<dataserver> dataservers=new ArrayList<>();
            long[] counts =new long[3];
            for(int i=0;i<topThree.size()&&i<3;i++){
                dataservers.add(topThree.get(i).getKey());
                counts[i]=topThree.get(i).getValue();
            }
            if(counts[0]>requestMessage.getblockCounts()) {
                channelHandlerContext.channel().writeAndFlush(
                        new writeResponseMsg(uuid,dataservers,counts,Totalblockcounts));
            }
            else{
                channelHandlerContext.channel().writeAndFlush(
                        new writeResponseMsg(uuid,null,null,Totalblockcounts));
            }
            log.info("已回应client的请求");
        }

}
