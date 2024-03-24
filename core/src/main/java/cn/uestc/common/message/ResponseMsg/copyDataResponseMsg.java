package cn.uestc.common.message.ResponseMsg;

import cn.uestc.common.message.MsgTypeEnum;

public class copyDataResponseMsg extends ResponseMessage{
    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.COPY_DATA_RESPONSE.getCode());
    }

}
