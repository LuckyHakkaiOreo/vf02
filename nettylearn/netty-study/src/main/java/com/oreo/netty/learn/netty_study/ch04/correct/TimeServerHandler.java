package com.oreo.netty.learn.netty_study.ch04.correct;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private int counter;
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
        log.info("TimeServerHandler.channelRegistered");
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
        System.out.println("TimeServerHandler.channelUnregistered");
        super.channelUnregistered(ctx);
    }

    /**
     * The {@link Channel} of the {@link ChannelHandlerContext} is now active
     * 1.ChannelHandlerContext的Channel现在被激活
     *
     * 2.Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireChannelActive()方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的channelActive方法
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TimeServerHandler.channelActive");
        super.channelActive(ctx);
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
        System.out.println("TimeServerHandler.channelInactive");
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
         System.out.println("TimeServerHandler.userEventTriggered");
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
         System.out.println("TimeServerHandler.channelWritabilityChanged");
         super.channelWritabilityChanged(ctx);
     }

// =====================================================================================================================

    /**
     * 1.Invoked when the current {@link Channel} has read a message from the peer.
     * 1.当当前的channel从对端（在我们这儿指的是客户端）读取到消息的时候
     * <p>
     * 2.Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireChannelRead方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的channelRead方法
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("TimeServerHandler.channelRead");
        // 从消息（ByteBuf）中读取数据

        String body = (String) msg;
        log.info("Timer 服务器接收到的命令是：{}; 接受消息的总次数: {}", body, ++counter);
        // 构建响应信息，回写到对端
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        log.info("行分隔符：{}", System.getProperty("line.separator"));
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
    }

    /**
     * 1.Invoked when the last message read by the current read operation has been consumed by
     * {@link #channelRead(ChannelHandlerContext, Object)}.
     * If {@link ChannelOption#AUTO_READ} is off,
     * no further attempt to read an inbound data
     * from the current {@link Channel} will be made until {@link ChannelHandlerContext#read()} is called.
     * 1.当最后一个消息被channelRead(ChannelHandlerContext, Object)消费后调用。
     * 如果{@link ChannelOption#AUTO_READ}是关闭的，
     * 那么在调用{@link ChannelHandlerContext#read()}之前，
     * 不会再尝试从当前的{@link Channel}读取入站数据。
     * <p>
     * 2.Calls {@link ChannelHandlerContext#fireChannelReadComplete()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireChannelReadComplete方法，
     * 转发到下一个ChannelPipeline中的ChannelInboundHandler中的channelReadComplete方法*
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("TimeServerHandler.channelReadComplete");
        // Request to flush all pending messages via this ChannelOutboundInvoker.
        // 请求通过此ChannelOutboundInvoker刷新所有挂起的消息。
        ctx.flush();
    }

    /**
     * 1.Gets called if a {@link Throwable} was thrown.
     * 在抛出{@link Throwable}时调用。
     * <p>
     * 2.Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * 2.在父类 ChannelInboundHandlerAdapter中，默认实现：
     * 调用ChannelHandlerContext#fireExceptionCaught方法，
     * 转发到下一个ChannelPipeline中的ChannelHandler中的exceptionCaught方法
     */
    @Override
    @SuppressWarnings("deprecation")
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("TimeServerHandler.exceptionCaught：{}", cause);
        /*
        Request to close the {@link Channel}
        and notify the {@link ChannelFuture} once the operation completes,
        either because the operation was successful or because of an error.
        请求关闭{@link Channel}，并在操作完成后通知{@link ChannelFuture}，
        原因是操作成功或发生错误。

        After it is closed it is not possible to reuse it again.
        关闭后就不可能再使用它了。

        <p>
        This will result in
        having the {@link ChannelOutboundHandler#close(ChannelHandlerContext, ChannelPromise)} method called
        of the next {@link ChannelOutboundHandler} contained in the {@link ChannelPipeline} of the {@link Channel}.
        这将导致调用{@link Channel}的{@link ChannelPipeline}中的
        下一个{@link ChannelOutboundHandler}
        的{@link ChannelOutboundHandler＃close（ChannelHandlerContext，ChannelPromise）}方法被调用。

        * */
        ctx.close();
    }

}
