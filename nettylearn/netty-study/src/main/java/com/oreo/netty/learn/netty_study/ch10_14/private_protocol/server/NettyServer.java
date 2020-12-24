package com.oreo.netty.learn.netty_study.ch10_14.private_protocol.server;

import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.NettyConstant;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec.PrivateMsgDecoder;
import com.oreo.netty.learn.netty_study.ch10_14.private_protocol.codec.PrivateMsgEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    public void bind() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new PrivateMsgDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast(new PrivateMsgEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            // 握手应答ChannelHandler
                            ch.pipeline().addLast(new LoginAuthRespHandler());
                            // 心跳应答ChannelHandler
                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
                        }
                    });

            ChannelFuture f = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
            log.info("Netty 服务器启动完毕：{}", NettyConstant.REMOTEIP + ":" + NettyConstant.PORT);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyServer().bind();
    }
}
