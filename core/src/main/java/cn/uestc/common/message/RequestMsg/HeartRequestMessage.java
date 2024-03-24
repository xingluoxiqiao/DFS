
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common.message.RequestMsg;


import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.dataserver;

/**
 * <p>
 *     Description: 
 * </p>
 */
public class HeartRequestMessage extends RequestMessage {

    private static final long serialVersionUID = 1L;

    // 可分配空间
    private long freeBlockCount;
    private String dataserverid;


    public HeartRequestMessage() {
    }

    public HeartRequestMessage(long freeBlockCount, String dataserverid) {
        this.freeBlockCount = freeBlockCount;
        this.dataserverid = dataserverid;
    }



    public long getFreeBlockCount() {

        return freeBlockCount;

    }

    public void setFreeBlockCount(long freeBlockCount) {

        this.freeBlockCount = freeBlockCount;

    }

    public String getDataserverid() {
        return dataserverid;
    }
    public void setDataserverid(String dataserverid) {
        this.dataserverid=dataserverid;
    }


    @Override
    protected void initialize() {
        this.setMsgType(MsgTypeEnum.HEARTBEAT_REQUST.getCode());
    }
}

    