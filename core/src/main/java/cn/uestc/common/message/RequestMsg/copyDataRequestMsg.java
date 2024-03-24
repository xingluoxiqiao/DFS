package cn.uestc.common.message.RequestMsg;

import cn.uestc.common.message.MsgTypeEnum;
import org.apache.commons.lang.builder.ToStringBuilder;

public class copyDataRequestMsg extends  RequestMessage{
    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.COPY_DATA_REQUEST.getCode());
    }

    private String uuid;
    private byte[] blockdata;
    private String blockname;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getBlockdata() {
        return blockdata;
    }

    public void setBlockdata(byte[] blockdata) {
        this.blockdata = blockdata;
    }

    public String getBlockname() {
        return blockname;
    }

    public void setBlockname(String blockname) {
        this.blockname = blockname;
    }

    public copyDataRequestMsg(String uuid, byte[] blockdata, String blockname) {
        this.uuid = uuid;
        this.blockdata = blockdata;
        this.blockname = blockname;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("uuid", uuid)
                .append("blockname", blockname)
                .append("blockData", (blockdata == null) ? "null" : "non-null")
                .toString();
    }
}
