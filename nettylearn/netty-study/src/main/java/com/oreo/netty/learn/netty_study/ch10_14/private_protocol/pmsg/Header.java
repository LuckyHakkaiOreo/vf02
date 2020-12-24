package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Header {

    /**
     * 0xabef（固定值，表示使用自定义私有协议）+主版本号+次版本号
     */
    private int crcCode = 0xabef0101;

    /**
     * 消息长度
     */
    private int length;

    /**
     * 会话ID
     */
    private long sessionID;

    /**
     * 消息类型
     */
    private byte type;

    /**
     * 消息优先级
     */
    private byte priority;

    /**
     * 附件
     */
    private Map<String, Object> attachment = new HashMap<String, Object>();

}
