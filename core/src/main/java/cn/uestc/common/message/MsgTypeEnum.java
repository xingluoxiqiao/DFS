package cn.uestc.common.message;

import cn.uestc.exception.CommonException;
import cn.uestc.common.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public enum MsgTypeEnum {

    NULL(-1, "避免空指针"),
    /*********************data server发送的消息******************/
    LOGIN_REQUEST(1001, "data server发送的登录请求"),

    HEARTBEAT_REQUST(1002, "data server发送的心跳请求"),

    WRITE_DATA_RESPONSE(1003, "data server发送的文件写入结果的消息"),

    READ_DATA_RESPONSE(1004, "data server返回的文件数据"),

    COPY_DATA_REQUEST(1005, "data server发送的文件副本复制消息"),

    COPY_DATA_RESPONSE(1006, "data server发送的文件副本复制消息相应"),

    COPY_RESPONSE(1007, "data server发送的文件副本复制消息相应"),

    /*********************nameserver发送的消息******************/

    LOGIN_RESPONSE(2001,"nameserver发送的登录响应"),

    HEARTBEAT_RESPONSE(2002,"nameserver发送的心跳响应"),

    WRITE_RESPONSE(2003,"nameserver发送的文件上传响应"),
    /*********************client发送的消息******************/

    WRITE_DATA_REQUEST(3001, "写入block的请求"),

    READ_DATA_REQUEST(3002, "从block读文件请求"),

    WRITE_REQUEST(3003, "写入block的请求"),

    COPY_REQUEST(3004, "复制block的请求");


    private int code;

    private String desc;

    public int getCode() {

        return code;

    }

    public void setCode(int code) {

        this.code = code;

    }

    public String getDesc() {

        return desc;

    }

    public void setDesc(String desc) {

        this.desc = desc;

    }

    private MsgTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }




    private static final HashMap<Integer, MsgTypeEnum> msgTypeEnumMap = getMap();

    private static HashMap<Integer, MsgTypeEnum> getMap() {
        HashMap<Integer, MsgTypeEnum> map = new HashMap<Integer, MsgTypeEnum>();
        MsgTypeEnum[] values = MsgTypeEnum.values();
        for(MsgTypeEnum msgTypeEnum : values) {
            map.put(msgTypeEnum.getCode(), msgTypeEnum);
            log.info("载入消息类型{},{}}", msgTypeEnum.getCode(), msgTypeEnum.getDesc());
        }
        return map;
    }

    public static MsgTypeEnum getMsgTypeEnumByCode(int code) {
        if(!msgTypeEnumMap.containsKey(code)) {
            throw new CommonException(StatusCodeEnum.UN_KNOWN_ERROR);
        }
        return msgTypeEnumMap.get(code);

    }

    //用于日志打印
    public String toString() {
        return getCode() + " : " + getDesc();
    }
}
