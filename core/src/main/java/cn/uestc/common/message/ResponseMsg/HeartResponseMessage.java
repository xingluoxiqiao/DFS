package cn.uestc.common.message.ResponseMsg;

import cn.uestc.common.message.MsgTypeEnum;

public class HeartResponseMessage extends ResponseMessage {

    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.HEARTBEAT_RESPONSE.getCode());
    }

}
