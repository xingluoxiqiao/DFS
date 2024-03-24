package cn.uestc.processor.requestMsgProcessor;

import cn.uestc.Global;
import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.readDataRequestMsg;
import cn.uestc.common.message.ResponseMsg.readDataResponseMsg;
import cn.uestc.exception.CommonException;
import cn.uestc.utils.myFileUtils;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.uestc.common.StatusCodeEnum.FILE_READ_ERROR;
import static cn.uestc.common.StatusCodeEnum.FILE_WRITE_ERROR;

@Processor(msgType = MsgTypeEnum.READ_DATA_REQUEST)
public class readDataReqMsgProcessor implements MessageProcessor<readDataRequestMsg> {

    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, readDataRequestMsg message) throws InterruptedException, IOException {
        String uuid = message.getUuid();
        List<Integer> fileNameList =new ArrayList<>();
        String sourceFile = Global.partitionPath + File.separator + uuid + "_blocks";
        int totalCount = myFileUtils.countFilesInFolder(sourceFile);
        int j = obtainMaxIndex(sourceFile);
        //getData获取的是还未收到的block索引列表
        //没有说明是第一次获取，全部传输过去
        if (message.getData() == null) {
            for (int i = 0; i <= j; i++) {
                File file = myFileUtils.getFile(sourceFile, "block_" + i);
                if (file != null && file.exists()) {
                    byte[] filedata = myFileUtils.fileToByteArray(file);
                    channelHandlerContext.channel().writeAndFlush(new readDataResponseMsg(uuid, uuid + "_block_" + i, filedata, 0, null));
                    //传输后将索引加入已传输列表
                    fileNameList.add(i);
                }
            }
            channelHandlerContext.channel().writeAndFlush(new readDataResponseMsg(uuid, null, null, totalCount, fileNameList));
        }
        //否则是重传，传索引列表中包含的
        else {
            //记录本次重传的文件数量
            int againCounts=0;
            //记录重传的文件索引
            List<Integer> again = new ArrayList<>();
            List<Integer> more = message.getData();
            for (int i = 0; i <= more.size(); i++){
                String filename= "block_"+more.get(i);
                File file = myFileUtils.getFile(sourceFile, filename);
                if (file != null && file.exists()) {
                    byte[] filedata = myFileUtils.fileToByteArray(file);
                    channelHandlerContext.channel().writeAndFlush(new readDataResponseMsg(uuid, uuid + "_block_" + filename, filedata, 0, null));
                    //每传输一次，更新
                    againCounts++;
                    again.add(more.get(i));
                } else{
                    channelHandlerContext.channel().writeAndFlush(new readDataResponseMsg());
                }
            }
            //重传完成的信号
            channelHandlerContext.channel().writeAndFlush(new readDataResponseMsg(uuid, null, null,againCounts, again));
        }
    }

    /**
     * 获取文件夹中文件名的最大索引，确定循环传输边界
     * @param filepath
     * @return
     * @throws IOException
     */
    private int obtainMaxIndex(String filepath) throws IOException {
        File folder = new File(filepath);
        List<String> fileNames = Arrays.stream(folder.listFiles())
                .map(File::getName)
                .collect(Collectors.toList());

        // 打印文件名
        int max=0;
        for (String file : fileNames) {
            String s = file.split("_")[1];
            int i = Integer.parseInt(s);
            if (i > max) {
                max=i;
            }
        }
        return max;
    }
}

