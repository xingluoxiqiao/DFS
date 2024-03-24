
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common.message.ResponseMsg;


import cn.uestc.common.message.MsgTypeEnum;

public class LoginResponseMessage extends ResponseMessage {

    protected void initialize() {

        this.setMsgType(MsgTypeEnum.LOGIN_RESPONSE.getCode());
    }

    
}

    