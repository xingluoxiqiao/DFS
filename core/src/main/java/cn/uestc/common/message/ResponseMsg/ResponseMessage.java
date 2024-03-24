
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common.message.ResponseMsg;

import cn.uestc.common.message.CommonMessage;


public abstract class ResponseMessage extends CommonMessage {

    
    private static final ResponseMessage NULL_RESPONSE_MESSAGE = new NullResponseMessage();
    
    public static ResponseMessage nullMessage() {return NULL_RESPONSE_MESSAGE;}
    
    static class NullResponseMessage extends ResponseMessage{
        @Override
        protected void initialize() {}
    }

}

    