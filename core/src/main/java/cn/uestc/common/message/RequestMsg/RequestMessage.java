
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common.message.RequestMsg;

import cn.uestc.common.BootLoader;
import cn.uestc.common.message.CommonMessage;
import cn.uestc.common.message.MsgTypeEnum;
import io.netty.channel.ChannelId;

public abstract class RequestMessage extends CommonMessage {


    private static final RequestMessage NULL_REQUEST_MESSAGE = new NullRequestMessage();

    public static RequestMessage nullMessage() {return NULL_REQUEST_MESSAGE;}

    static class NullRequestMessage extends RequestMessage {
        @Override
        protected void initialize() {}
    }

}
