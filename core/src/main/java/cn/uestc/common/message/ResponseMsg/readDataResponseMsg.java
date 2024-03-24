package cn.uestc.common.message.ResponseMsg;

import cn.uestc.common.message.MsgTypeEnum;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.List;

public class readDataResponseMsg extends ResponseMessage{
    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.READ_DATA_RESPONSE.getCode());
    }

    private String uuid;
    private String blockname;
    private byte[] fileData;
    private int totalCounts;
    //已传输的文件索引列表
    private List<Integer> fileNameList;

    public List<Integer> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<Integer> fileNameList) {
        this.fileNameList = fileNameList;
    }

    public int getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(int totalCounts) {
        this.totalCounts = totalCounts;
    }

    public readDataResponseMsg(String uuid, String blockname,byte[] fileData, int totalCounts, List<Integer> fileNameList) {
        this.uuid = uuid;
        this.blockname = blockname;
        this.fileData = fileData;
        this.totalCounts = totalCounts;
        this.fileNameList = fileNameList;
    }

    public readDataResponseMsg() {
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setBlockname(String blockname) {
        this.blockname = blockname;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    public String getUuid(){
        return uuid;
    }
    public String getBlockname(){
        return blockname;
    }
    public byte[] getFileData(){
        return  fileData;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("uuid", uuid)
                .append("blockname", blockname)
                .append("fileData", (fileData == null) ? "null" : "non-null")
                .append("totalCounts", totalCounts)
                .append("fileNameList", fileNameList)
                .toString();
    }
}
