package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.server;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.MessageType;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessage;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.pmsg.NettyMessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    /**
     * 维护节点是否在线的map
     */
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();

    /**
     * 白名单列表
     */
    private String [] whiteList = {"127.0.0.1", "192.168.1.151"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();

            NettyMessage loginResp = null;
            // 重复登录的话，拒绝
            if (nodeCheck.containsKey(nodeIndex)
                    && nodeCheck.get(nodeIndex).booleanValue()) {
                // 非零消息标识登录失败
                loginResp = NettyMessageFactory.buildLoginResp((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel()
                        .remoteAddress();
                String ip = address.getAddress().getHostAddress();
                // 校验白名单
                boolean isOk = false;
                for (String wIp : whiteList) {
                    if (wIp.equals(ip)) {
                        isOk = true;
                        break;
                    }
                }

                loginResp = isOk ? NettyMessageFactory.buildLoginResp((byte) 0)
                        : NettyMessageFactory.buildLoginResp((byte) -1);
                if (isOk)
                    nodeCheck.put(nodeIndex, true);
            }
            log.info("登录响应：{}，登录结果【{}】", loginResp, loginResp.getBody());

            ctx.writeAndFlush(loginResp);
        }else
            ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 记录对应的节点下线
        nodeCheck.put(ctx.channel().remoteAddress().toString(), false);
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
