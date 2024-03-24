package cn.uestc.processor;

import cn.uestc.Global;
import cn.uestc.NettyClientHandler;
import cn.uestc.annotation.Processor;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.NettyClientBootstrap;
import cn.uestc.common.StatusCodeEnum;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.writeDataRequestMsg;
import cn.uestc.common.message.ResponseMsg.writeResponseMsg;
import cn.uestc.dataserver;
import cn.uestc.exception.CommonException;
import cn.uestc.utils.myFileUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Processor(msgType=MsgTypeEnum.WRITE_RESPONSE)
public class writeResponseMsgProcessor implements MessageProcessor<writeResponseMsg> {
    @Override
    public void processMessage(ChannelHandlerContext channelHandlerContext, writeResponseMsg message) throws InterruptedException, IOException {
        List<dataserver> dataservers = message.getDataserverList();
        String uuid = message.getUuid();
        log.info("收到消息，类型是{}，内容为{}",
                MsgTypeEnum.getMsgTypeEnumByCode(message.getMsgType()), dataservers.toString());
        if(dataservers==null){
            channelHandlerContext.channel().close();
            throw new CommonException(StatusCodeEnum.NO_ENOUGH_BLOCKS);
        }
        Global.BlockCounts = myFileUtils.countFilesInFolder(uuid + "blocks");
        //与主dataserver建立连接，发送数据
        NettyClientBootstrap Client = new NettyClientBootstrap(dataservers.get(0).getIp(), String.valueOf(dataservers.get(0).getPort()),
                new NettyClientHandler(), "client 的 data client 正在启动");
        List<Integer> records = new ArrayList<>();
        int counts = 0;
        for (int i = 0; i < Global.BlockCounts; i ++) {
            File file = myFileUtils.getFile(uuid + "blocks", "block_" + i + ".bin");
            byte[] chunkedFile = myFileUtils.fileToByteArray(file);
            Client.getSocketChannel().writeAndFlush(
                    new writeDataRequestMsg(uuid, "block_" + i, chunkedFile, null, 0, null));
            //记录发送消息的序列号
            records.add(i);
            counts++;
        }
        Client.getSocketChannel().writeAndFlush(new writeDataRequestMsg(uuid, null, null, dataservers, counts, records));
        Client.getSocketChannel().closeFuture().sync();
        //关闭与nameserver的连接，退出到主程序
        channelHandlerContext.channel().close();
    }
}
