package com.oreo.netty.learn.netty_study.ch10_14.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的http接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        // 判断是否是关闭链路的指令帧
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), ((CloseWebSocketFrame) msg).retain());
            return;
        }
        // 判断是否是ping消息（心跳）
        if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
        }
        // 本例只支持文本消息，不支持二进制消息
        if (!(msg instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "服务器无法支持 %s 这种帧类型！", msg.getClass().getName()));
        }

        String request = ((TextWebSocketFrame) msg).text();

        log.info("{} 接收到 {}", ctx.channel(), request);

        ctx.channel().write(new TextWebSocketFrame(request + "，欢迎使用Netty WebSocket，现在时刻："
                + new Date().toString()));

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        // 如果http解码失败，返回错误
        // 或者如果接受到的http请求不是WebSocket的握手请求，返回失败
        if (!msg.decoderResult().isSuccess()
                || (!"websocket".equals(msg.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HTTP_1_1,
                    BAD_REQUEST));
            return;
        }

        // 发起握手响应，正式与客户端建立Socket连接
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8080/websocket", null, false);
        handshaker = wsFactory.newHandshaker(msg);

        if (handshaker == null)
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        else {
            // 构造握手响应消息返回给客户端，同时将WebSocket相关的编解码器添加到pipeline中，
            // 用来处理WebSocket的编解码;
            // 我们的服务端就可以处理WebSocket的消息了
            handshaker.handshake(ctx.channel(), msg);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx,
                                  FullHttpRequest msg,
                                  DefaultFullHttpResponse defaultFullHttpResponse) {
        // 返回响应给客户端
        if (defaultFullHttpResponse.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(defaultFullHttpResponse.status().toString(),
                    CharsetUtil.UTF_8);
            defaultFullHttpResponse.content().writeBytes(buf);
            buf.release();
            setContentLength(defaultFullHttpResponse, defaultFullHttpResponse.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(msg);
        if (!isKeepAlive(msg) || defaultFullHttpResponse.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
