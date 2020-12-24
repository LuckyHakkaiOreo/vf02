package com.oreo.netty.learn.netty_study.ch01.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.*;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("TimeClientHandler.channelActive");
        // 当连接激活时，往服务器发送一个消息
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("TimeClientHandler.channelRead");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        log.info("读取到服务端的消息 : {}", body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("TimeClientHandler.exceptionCaught");
        // 释放资源
        log.warn("发生异常 : {}", cause.getMessage());
        ctx.close();
    }
    
    
    // =========================================================================

    /**
     * 1.The {@link Channel} of the {@link ChannelHandlerContext} was registered with its {@link EventLoop}
     * 1.当ChannelHandlerContext的Channel被注册到它的EventLoop的时候，该方法被调用
     *
     * 2.Calls {@link ChannelHandlerContext#fireChannelRegistered()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireChannelRegistered()方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的channelRegistered方法
     */
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
        log.info("TimeClientHandler.channelRegistered");
        super.channelRegistered(ctx);
    }

    /**
     * The {@link Channel} of the {@link ChannelHandlerContext} was unregistered from its {@link EventLoop}
     * 1.当ChannelHandlerContext的Channel未被注册到它的EventLoop的时候，该方法被调用
     *
     * Calls {@link ChannelHandlerContext#fireChannelUnregistered()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireChannelUnregistered()方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的channelUnregistered方法
     *
     */
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TimeClientHandler.channelUnregistered");
        super.channelUnregistered(ctx);
    }


    /**
     * 1.The {@link Channel} of the {@link ChannelHandlerContext} was registered is now inactive and reached its
     * end of lifetime.
     * 1.当ChannelHandlerContext的Channel被注册后，然后被闲置并且到达了生命周期的尽头
     *
     * 2.Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireChannelInactive方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的channelInactive方法
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("TimeClientHandler.channelInactive");
        super.channelInactive(ctx);
    }

    /**
     * 1.Gets called if an user event was triggered.
     * 1.如果用户事件被触发，则调用。
     *
     * 2.Calls {@link ChannelHandlerContext#fireUserEventTriggered(Object)} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireUserEventTriggered方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的userEventTriggered方法
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("TimeClientHandler.userEventTriggered");
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 1.Gets called once the writable state of a {@link Channel} changed.
     * You can check the state with {@link Channel#isWritable()}.
     * 1.当{@link Channel}的可写状态更改时调用。
     * 您可以使用{@link Channel#isWritable()}检查状态。
     * 2.Calls {@link ChannelHandlerContext#fireChannelWritabilityChanged()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireChannelWritabilityChanged方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的channelWritabilityChanged方法
     */
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TimeClientHandler.channelWritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }
}
