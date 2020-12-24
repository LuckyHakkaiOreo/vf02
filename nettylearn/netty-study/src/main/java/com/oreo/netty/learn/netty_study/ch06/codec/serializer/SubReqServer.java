package com.oreo.netty.learn.netty_study.ch06.codec.serializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubReqServer {

    public void bind(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 服务端一次只能处理一个客户端连接，当多个客户端来的时候，需要加入队列排队，100就是队列的大小
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 对象解码器
                            // 当前使用的是ClassResolvers.weakCachingConcurrentResolver，
                            // 创建线程安全的WeakReferebceMap对类加载器进行缓存，
                            // 它支持多线程并发访问，当vm内存不足时，会释放缓存中的内存，防止内存泄漏
                            ch.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                                    ClassResolvers.weakCachingConcurrentResolver(
                                            this.getClass().getClassLoader())));
                            // ObjectEncoder：可以在消息发送的时候，
                            // 自动将实现Serializable的POJO对象进行编码，因此用户无须亲自对对象
                            // 进行手工序列化。
                            ch.pipeline().addLast(new ObjectEncoder());
                            // 经过ObjectDecoder解码后的消息内容就是我们需要的SubscribeReq对象了
                            ch.pipeline().addLast(new SubReqServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
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
        new SubReqServer().bind(port);
    }
}
