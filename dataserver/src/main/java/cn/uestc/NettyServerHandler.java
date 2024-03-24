package cn.uestc;
import cn.uestc.common.BootLoader;
import cn.uestc.common.MessageProcessor;
import cn.uestc.common.message.CommonMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<CommonMessage> {


    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, CommonMessage message)
            throws Exception {
        MessageProcessor<CommonMessage> processor = (MessageProcessor<CommonMessage>)
                BootLoader.getProcessor(message.getMsgType());
        processor.processMessage(channelHandlerContext, message);
        ReferenceCountUtil.release(message);

    }
}
