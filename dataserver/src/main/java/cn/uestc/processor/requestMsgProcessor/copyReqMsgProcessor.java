package cn.uestc.processor.requestMsgProcessor;

import cn.uestc.Global;
import cn.uestc.NettyClientHandler;
import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.NettyClientBootstrap;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.copyDataRequestMsg;
import cn.uestc.common.message.RequestMsg.copyRequestMessage;
import cn.uestc.dataserver;
import cn.uestc.utils.myFileUtils;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Processor(msgType = MsgTypeEnum.COPY_REQUEST)
public class copyReqMsgProcessor implements MessageProcessor<copyRequestMessage> {
    //副本复制，无重传机制
    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, copyRequestMessage message) throws Exception {
        List<dataserver> dataservers=message.getDataservers();
        String uuid = message.getUuid();

        for (dataserver dataserver : dataservers) {
            NettyClientBootstrap copyclient = new NettyClientBootstrap(dataserver.getIp(),
                    String.valueOf(dataserver.getPort()), new NettyClientHandler(), "开始复制文件副本");
            String sourceFile = Global.partitionPath + File.separator + uuid + "_blocks";
            int counts = obtainMaxIndex(sourceFile);
            for (int j = 0; j <= counts; j++) {
                File file = myFileUtils.getFile(sourceFile, "block_" + j);
                if (file != null && file.exists()) {
                    byte[] filedata = myFileUtils.fileToByteArray(file);
                    copyclient.getSocketChannel().writeAndFlush(new copyDataRequestMsg(uuid, filedata, "block_" + j));
                }
            }
            copyclient.getSocketChannel().writeAndFlush(new copyDataRequestMsg(uuid, null, null));
            copyclient.getSocketChannel().closeFuture().sync();
        }
        channelHandlerContext.channel().close();
    }
    /**
     * 获取文件夹中文件名的最大索引，确定循环传输边界
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
