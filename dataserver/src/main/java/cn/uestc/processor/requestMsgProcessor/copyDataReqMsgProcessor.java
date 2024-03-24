package cn.uestc.processor.requestMsgProcessor;

import cn.uestc.Global;
import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.copyDataRequestMsg;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.List;

import static cn.uestc.utils.myFileUtils.copyFileToFolder;

@Processor(msgType = MsgTypeEnum.COPY_DATA_REQUEST)
public class copyDataReqMsgProcessor implements MessageProcessor<copyDataRequestMsg> {
    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, copyDataRequestMsg responseMessage) throws Exception {
        String uuid = responseMessage.getUuid();
        byte[] fileData = responseMessage.getBlockdata();
        String blockname = responseMessage.getBlockname();
        String filename = Global.partitionPath + File.separator + uuid + "_blocks";

        if(fileData != null) {
            copyFileToFolder(fileData, filename, blockname);
        }else {
            //文件传输完成，关闭连接
            channelHandlerContext.channel().close();
        }


    }
}
