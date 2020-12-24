package com.oreo.netty.learn.netty_study.ch05.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {
    public void bind(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                        /*
                          DelimiterBasedFrameDecoder参数解析：
                          参数一：
                                单条最大消息长度，当单条消息达到这个长度没有遇到分隔符的时候，
                                抛出TooLongFrameException；
                                防止异常码流缺失分隔符导致的内存溢出，这是netty解码器的可靠性保障
                          参数二：
                                分隔符的缓冲对象
                         */

                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                        // 将ByteBuf解码成为String对象
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new EchoServerHandler());
                        /*
                          PS:
                            1.DelimiterBasedFrameDecoder：将消息按照分隔符进行解码，获取到完整的消息包
                            2.StringDecoder：将ByteBuf解码成为String对象
                            3.EchoServerHandler：收到的消息直接就是StringDecoder解码后的字符串对象
                         */
                    }
                });

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new EchoServer().bind(port);
    }
}
