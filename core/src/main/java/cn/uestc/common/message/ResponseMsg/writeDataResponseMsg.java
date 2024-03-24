package cn.uestc.common.message.ResponseMsg;

import cn.uestc.common.BootLoader;
import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.dataserver;

import java.util.List;

public class writeDataResponseMsg extends ResponseMessage {
    @Override
    protected void initialize() {
        this.msgType = MsgTypeEnum.WRITE_DATA_RESPONSE.getCode();
    }

    private String uuid;
    private List<dataserver> dataservers;
    private Boolean success;
    List<Integer> BlockCounts;

    public List<dataserver> getdataservers() {
        return dataservers;
    }

    public void setIps(List<dataserver> dataservers) {
        this.dataservers = dataservers;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Integer> getBlockCounts() {
        return BlockCounts;
    }

    public void setBlockCounts(List<Integer> blockCounts) {
        BlockCounts = blockCounts;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public writeDataResponseMsg() {
    }
    public writeDataResponseMsg(String uuid,Boolean success,List<dataserver> dataservers,List<Integer> blockCounts) {
        this.uuid=uuid;
        this.success=success;
        this.dataservers=dataservers;
        this.BlockCounts=blockCounts;
    }


}
