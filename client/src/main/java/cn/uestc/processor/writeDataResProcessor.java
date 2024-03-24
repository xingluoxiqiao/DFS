package cn.uestc.processor;

import cn.uestc.Global;
import cn.uestc.annotation.Processor;
import cn.uestc.common.message.RequestMsg.copyRequestMessage;
import cn.uestc.dataserver;
import cn.uestc.exception.CommonException;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.writeDataRequestMsg;
import cn.uestc.common.message.ResponseMsg.writeDataResponseMsg;
import cn.uestc.entity.BlockInfo;
import cn.uestc.utils.myFileUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.uestc.common.StatusCodeEnum.FILE_WRITE_ERROR;
import static cn.uestc.common.message.MsgTypeEnum.WRITE_DATA_RESPONSE;

@Processor(msgType = WRITE_DATA_RESPONSE)
@Component
@Slf4j
public class writeDataResProcessor implements MessageProcessor<writeDataResponseMsg> {
    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, writeDataResponseMsg responseMessage) throws InterruptedException, IOException {
       log.info("收到消息，类型是{},内容为{}",
                MsgTypeEnum.getMsgTypeEnumByCode(responseMessage.getMsgType()), responseMessage);


        List<dataserver> dataservers = responseMessage.getdataservers();
        int ipsSize = dataservers.size();
        String uuid = responseMessage.getUuid();
        List<Integer> blockCounts = responseMessage.getBlockCounts();
        //如果成功，就插入数据库
        if(responseMessage.getSuccess()) {
            log.info("文件上传成功");
            //将文件数据插入数据库
            if(ipsSize==3){
                BlockInfo blockInfo = new BlockInfo(uuid,dataservers.get(0).getIp(),dataservers.get(0).getPort(),
                        dataservers.get(1).getIp(),dataservers.get(1).getPort(),
                        dataservers.get(2).getIp(),dataservers.get(2).getPort(),
                        null, LocalDateTime.now(),LocalDateTime.now(),0);
                Global.blockInfos.add(blockInfo);
            }else if(ipsSize==1){
                BlockInfo blockInfo = new BlockInfo(uuid,dataservers.get(0).getIp(),dataservers.get(0).getPort(),
                        null,0,null,0,
                        null,LocalDateTime.now(),LocalDateTime.now(),0);
                Global.blockInfos.add(blockInfo);
            }else if(ipsSize==2){
                BlockInfo blockInfo = new BlockInfo(uuid,dataservers.get(0).getIp(),dataservers.get(0).getPort(),
                        dataservers.get(1).getIp(),dataservers.get(1).getPort(),
                        null,0,
                        null,LocalDateTime.now(),LocalDateTime.now(),0);
                Global.blockInfos.add(blockInfo);
            }
            //发送文件副本复制指令
            InetSocketAddress inetSocketAddress = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
            String ip = inetSocketAddress.getAddress().getHostAddress();
            for (int i=0;i<dataservers.size();i++){
               if(dataservers.get(i).getIp().equals(ip)){
                   dataservers.remove(i);
                   break;
               }
            }
            channelHandlerContext.channel().writeAndFlush(new copyRequestMessage(dataservers,uuid));
        }
        //不成功，重发
        else{
            List<Integer> records =new ArrayList<>();
            for(int i=0;i<blockCounts.size() ;i++){
                writeDataRequestMsg writeDataRequestMsg = new writeDataRequestMsg();
                File file = myFileUtils.getFile(uuid + "_blocks", "block_" + blockCounts.get(i));
                if(file!=null){
                    byte[] fileData = myFileUtils.fileToByteArray(file);
                    writeDataRequestMsg.setFileData(fileData);
                }else{
                    throw new CommonException(FILE_WRITE_ERROR);
                }
                writeDataRequestMsg.setUuid(uuid);
                writeDataRequestMsg.setBlockname("block_"+i);
                writeDataRequestMsg.setDataservers(dataservers);
                writeDataRequestMsg.setBlockCounts(blockCounts.size());
                records.add(blockCounts.get(i));
                channelHandlerContext.channel().writeAndFlush(writeDataRequestMsg);
            }
            channelHandlerContext.channel().writeAndFlush(new writeDataRequestMsg(uuid,null,null,dataservers,blockCounts.size(),records));
        }

    }
}
