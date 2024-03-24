package cn.uestc.common.message.RequestMsg;

import cn.uestc.common.message.MsgTypeEnum;

import java.util.List;

public class readDataRequestMsg extends  RequestMessage{
    private String uuid;
    private List<Integer>  data;

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public readDataRequestMsg(String uuid, List<Integer> data) {
        this.uuid = uuid;
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public readDataRequestMsg(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.READ_DATA_REQUEST.getCode());
    }
}
