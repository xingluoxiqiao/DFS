package cn.uestc.common.message.ResponseMsg;

import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.dataserver;

import java.util.List;

public class writeResponseMsg extends  ResponseMessage{
    @Override
    protected void initialize() {
        this.msgType= MsgTypeEnum.WRITE_RESPONSE.getCode();
    }
    private String uuid;
    private List<dataserver> dataserverList;
    private long [] freeCounts;
    private long totalCount;

    public writeResponseMsg(String uuid, List<dataserver> dataserverList, long[] freeCounts, long totalCount) {
        this.uuid = uuid;
        this.dataserverList = dataserverList;
        this.freeCounts = freeCounts;
        this.totalCount = totalCount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<dataserver> getDataserverList() {
        return dataserverList;
    }

    public void setDataserverList(List<dataserver> dataserverList) {
        this.dataserverList = dataserverList;
    }

    public long[] getFreeCounts() {
        return freeCounts;
    }

    public void setFreeCounts(long[] freeCounts) {
        this.freeCounts = freeCounts;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public writeResponseMsg() {
    }
}
