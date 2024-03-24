package cn.uestc.common.message.RequestMsg;

import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.dataserver;

import java.util.List;

public class copyRequestMessage extends RequestMessage {
    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.COPY_REQUEST.getCode());
    }

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private List<dataserver> dataservers;

    public List<dataserver> getDataservers() {
        return dataservers;
    }

    public void setDataservers(List<dataserver> dataservers) {
        this.dataservers = dataservers;
    }

    public copyRequestMessage(List<dataserver> dataservers, String uuid) {
        this.dataservers = dataservers;
        this.uuid = uuid;
    }
}
