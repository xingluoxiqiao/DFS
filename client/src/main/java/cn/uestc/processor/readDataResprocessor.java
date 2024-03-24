package cn.uestc.processor;

import cn.uestc.Global;
import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.StatusCodeEnum;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.readDataRequestMsg;
import cn.uestc.common.message.ResponseMsg.readDataResponseMsg;
import cn.uestc.exception.CommonException;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static cn.uestc.common.StatusCodeEnum.FILE_READ_ERROR;
import static cn.uestc.utils.myFileUtils.copyFileToFolder;
import static cn.uestc.utils.myFileUtils.createFolder;
@Slf4j
@Processor(msgType = MsgTypeEnum.READ_DATA_RESPONSE)
public class readDataResprocessor implements MessageProcessor<readDataResponseMsg> {

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, readDataResponseMsg responseMessage) throws Exception {
        log.info("收到消息，类型是{}", MsgTypeEnum.getMsgTypeEnumByCode(responseMessage.getMsgType()));
        String uuid = responseMessage.getUuid();
        byte[] fileData = responseMessage.getFileData();
        String blockname = responseMessage.getBlockname();
        String filename = Global.filePathTarget+File.separator+"blocks";
        if(uuid==null){
            throw new CommonException(FILE_READ_ERROR);
        }
        createFolder(filename);
        if (fileData != null) {
            copyFileToFolder(fileData, filename, blockname);
            Global.transferCounts.add(Integer.valueOf(blockname.split("_")[2]));
        }

        //文件传输完成，准备回应
        else {
            //当前dataserver文件全部传输完成
            if (responseMessage.getTotalCounts() == Global.transferCounts.size()) {
                //关闭连接
                channelHandlerContext.channel().close();
            }
            //遍历list，找出少了谁
            else {
                List<Integer> found = found(Global.transferCounts, responseMessage.getFileNameList());
                channelHandlerContext.channel().writeAndFlush(new readDataRequestMsg(uuid, found));
            }
        }

    }

    private List<Integer> found(List<Integer> BlockCounts, List<Integer> fileNameList) {
        List<Integer> found = new ArrayList<>(fileNameList);
        found.removeAll(BlockCounts);
        return found;
    }
}
