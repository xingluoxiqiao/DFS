package cn.uestc.processor.requestMsgProcessor;

import cn.uestc.Global;
import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.writeDataRequestMsg;
import cn.uestc.common.message.ResponseMsg.writeDataResponseMsg;
import cn.uestc.dataserver;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static cn.uestc.utils.myFileUtils.copyFileToFolder;
import static cn.uestc.utils.myFileUtils.createFolder;
import static org.reflections.Reflections.log;

@Processor(msgType = MsgTypeEnum.WRITE_DATA_REQUEST)
public class writeDataReqMsgProcessor implements MessageProcessor<writeDataRequestMsg> {

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, writeDataRequestMsg message) throws Exception {
        //创建该文件的输出文件夹,将文件存入
        String blockname = message.getBlockname();
        byte[] fileData = message.getFileData();
        String uuid = message.getUuid();
        List<dataserver> dataservers = message.getDataservers();
        List<Integer> records = message.getRecords();
        //新建文件夹
        String filename=Global.partitionPath+File.separator+uuid+"_blocks";
        createFolder(filename);
        if(fileData!=null) {
            copyFileToFolder(fileData, filename, blockname);
            Global.freeBlockCount--;
            Global.transferCounts.add(Integer.valueOf(blockname.split("_")[1]));
        }
        //文件传输完成，准备回应
        else{
            //文件全部传输完成
            log.info("文件接收完成");
            if(message.getBlockCounts()==Global.transferCounts.size()){
                channelHandlerContext.channel().writeAndFlush(new writeDataResponseMsg(uuid,true,dataservers,null));
                Global.transferCounts.clear();
                Global.dataservers=dataservers;
            }
            //遍历list，找出少了谁
            else{
                List<Integer> found = found(Global.transferCounts,records);
                channelHandlerContext.channel().writeAndFlush(new writeDataResponseMsg(uuid,false,dataservers,found));
            }
        }


    }
    private static List<Integer> found(List<Integer> blockCounts,List<Integer> records){
        List<Integer> missing = new ArrayList<>();
        for (int num : records) {
            if (!blockCounts.contains(num)) {
                missing.add(num);
            }
        }
        return missing;
    }

 }
