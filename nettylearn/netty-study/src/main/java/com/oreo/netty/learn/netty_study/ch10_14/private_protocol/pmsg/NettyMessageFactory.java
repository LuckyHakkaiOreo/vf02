package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg;

public class NettyMessageFactory {

    /**
     * 构建登录请求消息
     * @return
     */
    public static NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }

    /**
     * 构建登录响应消息
     * @param result
     * @return
     */
    public static NettyMessage buildLoginResp(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    /**
     * 构建心跳请求
     * @return
     */
    public static NettyMessage buildHeatBeatReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_REQ.value());
        message.setHeader(header);
        return message;
    }

    /**
     * 构建心跳应答
     * @return
     */
    public static NettyMessage buildHeatBeatResp() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }


}
