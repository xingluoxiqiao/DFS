package cn.uestc.common.message.RequestMsg;


import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.RequestMessage;
import cn.uestc.dataserver;
import io.netty.handler.stream.ChunkedFile;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.List;

// 写入数据请求消息,包含文件数据
public class writeDataRequestMsg extends RequestMessage {

    private String uuid;
    private String blockname;
    private byte[] fileData;
    private List<dataserver> dataservers;
    private int blockCounts;
    private List<Integer> records;

    public List<Integer> getRecords() {
        return records;
    }

    public void setRecords(List<Integer> records) {
        this.records = records;
    }

    public int getBlockCounts() {
        return blockCounts;
    }

    public void setBlockCounts(int blockCounts) {
        this.blockCounts = blockCounts;
    }

    public List<dataserver> getDataservers() {
        return dataservers;
    }

    public void setDataservers(List<dataserver> dataservers) {
        this.dataservers = dataservers;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBlockname() {
        return blockname;
    }

    public void setBlockname(String blockname) {
        this.blockname = blockname;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.WRITE_DATA_REQUEST.getCode());
    }

    public writeDataRequestMsg() {
    }

    public writeDataRequestMsg(String uuid, String blockname, byte[] fileData,List<dataserver> dataservers,int blockCounts,List<Integer> records) {
        this.uuid = uuid;
        this.blockname = blockname;
        this.fileData = fileData;
        this.dataservers =dataservers;
        this.records=records;
        this.blockCounts = blockCounts;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("uuid", uuid)
                .append("blockname", blockname)
                .append("fileData", (fileData == null) ? "null" : "non-null")
                .append("dataservers", dataservers)
                .append("blockCounts", blockCounts)
                .append("records", records)
                .toString();
    }
}