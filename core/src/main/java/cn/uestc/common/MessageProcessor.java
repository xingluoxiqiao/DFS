
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common;

import cn.uestc.common.message.CommonMessage;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * <p>
 *     Description: 消息处理接口
 * </p>

 */
//This interface defines a MessageProcessor which takes a CommonMessage as an argument and returns nothing
public interface MessageProcessor<S extends CommonMessage> {
    //This method takes a ChannelHandlerContext and a CommonMessage as arguments and returns nothing
    public void processMessage(ChannelHandlerContext channelHandlerContext, S message) throws Exception;

}
    