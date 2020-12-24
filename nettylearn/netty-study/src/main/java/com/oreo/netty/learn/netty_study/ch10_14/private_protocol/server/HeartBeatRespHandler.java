package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.server;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.MessageType;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessage;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        NettyMessage message = (NettyMessage) msg;

        if (message.getHeader() != null
            && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            log.info("接收到客户端的心跳消息：--->{}", message);

            NettyMessage heatBeatResp = NettyMessageFactory.buildHeatBeatResp();
            log.info("向客户端发送心跳响应：--->{}", heatBeatResp);

            ctx.writeAndFlush(heatBeatResp);
        } else
            ctx.fireChannelRead(msg);
    }
}
