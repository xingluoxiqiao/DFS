package cn.uestc.common.message.RequestMsg;


import cn.uestc.common.message.MsgTypeEnum;
import cn.uestc.common.message.RequestMsg.RequestMessage;

public class LoginRequestMessage extends RequestMessage {


        private Long freeBlockCount;
        private String ip;

        private String dataserverid;

        private int dataserverport;

    @Override
        protected void initialize() {
            this.setMsgType(MsgTypeEnum.LOGIN_REQUEST.getCode());
        }


        public Long getFreeBlockCount() {

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


    public void setDataserverPort(int port) {
        this.dataserverport=port;
    }

    public int getDataserverport() {
        return dataserverport;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

