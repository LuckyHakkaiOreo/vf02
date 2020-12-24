package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.client;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.MessageType;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessage;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端被激活的时候，向服务器端发送登录请求
        ctx.writeAndFlush(NettyMessageFactory.buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 如果是握手应答消息，需要判断是否登录认证成功
        if (message.getHeader() != null
            && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                // 握手失败，关闭连接
                ctx.close();
            } else {
                log.info("登录成功！响应消息：{}", message);
                // 响应消息透传给下一个ChannelHandler
                ctx.fireChannelRead(msg);
            }
        } else
            ctx.fireChannelRead(msg);
    }
}
