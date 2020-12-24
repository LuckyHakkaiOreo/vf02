package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.client;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.MessageType;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessage;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeatFuture;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 握手成功后，像服务端发送心跳消息
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            // 每五秒钟发送一个心跳
            ctx.executor().scheduleAtFixedRate(
                    new HeartBeatTask(ctx), 0,
                    5000, TimeUnit.MILLISECONDS);
        } else if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            log.info("客户端接收到服务端的心跳包：--->{}", message);
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 停止定时器
        if (heartBeatFuture != null) {
            heartBeatFuture.cancel(true);
            heartBeatFuture = null;
        }
        super.exceptionCaught(ctx, cause);
    }

    private static class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heartBeat = NettyMessageFactory.buildHeatBeatReq();
            log.info("客户端发送心跳消息到服务器：--->{}", heartBeat);
            ctx.writeAndFlush(heartBeat);
        }
    }
}
