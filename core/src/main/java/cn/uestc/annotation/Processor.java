
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.uestc.common.message.MsgTypeEnum;

/**
 * <p>
 * Description: 标记消息处理器，并指定处理的消息类型
 * </p>
 */
// 添加中文注释
@Documented
// 指定该注解的保留策略为运行时
@Retention(RetentionPolicy.RUNTIME)
// 指定该注解的目标为类型
@Target({ ElementType.TYPE })
// 指定该注解可继承
@Inherited
public @interface Processor {

    public MsgTypeEnum msgType() default MsgTypeEnum.NULL;

}
