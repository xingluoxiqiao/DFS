
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common.message;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * <p>
 *     Description: 
 * </p>

 */
public abstract class CommonMessage implements Serializable{
        
    protected static final long serialVersionUID = 2243859217859238868L;

    protected int msgType;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    
    // 所有子类都应该继承该方法
    protected abstract void initialize();
    
    public CommonMessage() {
        initialize();
    }
    public String toString() {
        //返回当前对象的字符串表示，使用ToStrinStyle.SHORT_PREFIX_STYLE格式
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

    