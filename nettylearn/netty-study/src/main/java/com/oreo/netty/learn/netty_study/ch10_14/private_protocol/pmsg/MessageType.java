package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg;

public enum  MessageType {
    // 常规的服务请求/应答
    SERVICE_REQ((byte) 0, "常规的服务请求"),
    SERVICE_RESP((byte) 1, "常规的服务响应"),
    ONE_WAY((byte) 2, "oneway"),
    // 登录（握手）请求/应答
    LOGIN_REQ((byte) 3, "登录请求"),
    LOGIN_RESP((byte) 4, "登录响应"),
    // 心跳请求/应答
    HEARTBEAT_REQ((byte) 5, "心跳请求"),
    HEARTBEAT_RESP((byte) 6, "心跳响应");

    private byte value;
    private String desc;

    private MessageType(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public byte value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }
}
