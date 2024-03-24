package cn.uestc.common.message.RequestMsg;


import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.RequestMessage;

public class writeRequestMsg extends RequestMessage {

    private int blockCounts;
    private String uuid;


    public writeRequestMsg(int blockCounts, String uuid) {
        this.blockCounts=blockCounts;
        this.uuid = uuid;
    }

    @Override
    protected void initialize() {
        this.msgType = MsgTypeEnum.WRITE_REQUEST.getCode();
    }

    public writeRequestMsg() {
    }

    public int getblockCounts() {
        return blockCounts;

    }

    public void setBlockCounts(int blockCounts){

        this.blockCounts=blockCounts;

    }

    public String getUuid() {

        return uuid;

    }

    public void setUuid(String uuid) {

        this.uuid = uuid;

    }


}
