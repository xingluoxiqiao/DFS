
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.common;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import cn.uestc.common.message.CommonMessage;
import lombok.extern.slf4j.Slf4j;

import cn.uestc.annotation.Processor;
import cn.uestc.exception.CommonException;
import org.reflections.Reflections;

/**
 * <p>
 * Description: 启动时初始化的数据
 * </p>
 *
 */
@Slf4j
public class BootLoader {
    // 属性值
    private static final Properties properties = loadProperties();

    // 处理器的缓存
    @SuppressWarnings("unchecked")
    private static final HashMap<Integer, MessageProcessor<? extends CommonMessage>> processorMap = loadProcessor();

    // 处理器包名
    private static final String keyForProcessorPackage = "Processor_Path";


    private static Properties loadProperties() {
        Properties properties = new Properties();
        // 设置属性值
        properties.setProperty("NAMESERVER_IP", initConfiguration.NAMESERVER_IP);
        properties.setProperty("NAMESERVER_PORT", initConfiguration.NAMESERVER_PORT);
        properties.setProperty("Processor_Path", initConfiguration.Processor_Path);
        properties.setProperty("BLOCK_SIZE", String.valueOf(initConfiguration.BLOCK_SIZE));
        return properties;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
   private static HashMap loadProcessor() {
        HashMap<Integer, MessageProcessor<?>> processorMap = new HashMap<Integer, MessageProcessor<?>>();
        try {
            //获取指定包下的所有Processor注解的类
            Set<Class<?>> processorSet = new Reflections(properties.getProperty(keyForProcessorPackage))
                    .getTypesAnnotatedWith(Processor.class);
            //遍历每一个类
            for (Class clazz : processorSet) {
                //获取Processor注解
                Processor processorAnnotation = (Processor) clazz.getAnnotation(Processor.class);
                //获取消息类型码
                Integer code = processorAnnotation.msgType().getCode();
                //将类实例化放入processorMap中
                processorMap.put(code, (MessageProcessor<?>) clazz.newInstance());
                log.info("载入processor：{},{}",code,clazz);
            }
        } catch (Exception e) {
            throw new CommonException(StatusCodeEnum.LOAD_PROCESSOR_ERROR);
        }
        return processorMap;
    }

    public static MessageProcessor getProcessor(Integer channelId) {
        return processorMap.get(channelId);
    }

    public static String getProperties(String key) {
        return properties.getProperty(key);
    }

}
