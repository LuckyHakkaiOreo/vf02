package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.client;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.NettyConstant;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec.PrivateMsgDecoder;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec.PrivateMsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClient {

    /**
     * 创建一个单线程的定时调度线程池
     */
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 设置消息解码器
                            pipeline.addLast(new PrivateMsgDecoder(1024 * 1024,
                                    4, 4));
                            // 设置消息编码器
                            pipeline.addLast("MessageEncoder", new PrivateMsgEncoder());
                            // 读取消息超时处理器
                            pipeline.addLast("readTimeoutHandler"
                                    , new ReadTimeoutHandler(50));

                            pipeline.addLast("LoginAuthHandler", new LoginAuthReqHandler());
                            pipeline.addLast("HeartBeatHandler", new HeartBeatReqHandler());

                        }
                    });

            // 发起异步连接操作
            ChannelFuture future = b.connect(new InetSocketAddress(host, port),
                    new InetSocketAddress(NettyConstant.LOCALIP,
                            NettyConstant.LOCAL_PORT)).sync();
            // 当对应的channel关闭的时候，就会返回对应的channel。
            // Returns the ChannelFuture which will be notified when this channel is closed. This method always returns the same future instance.
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        try {
                            // 发起重连操作
                            connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
    }
}
