package com.oreo.netty.learn.netty_study.ch04.fault;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

    public void bind(int port) {
        // 配置服务端的NIO线程组：boss、worker线程
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 创建服务端启动类对象：ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, worker)
                    // 设置channel：NioServerSocketChannel其功能类似于jdk中ServerSocketChannel类
                    .channel(NioServerSocketChannel.class)
                    // 配置tcp参数：SO_BACKLOG的大小为1024
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 配置childHandler
                    .childHandler(new ChildChannelHandler());

            // 同步阻塞方法：绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            // f.channel().closeFuture().sync()该方法也是同步阻塞的：
            // 为的是在等待服务器关闭后，才退出main函数
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            arg0.pipeline().addLast(new TimeServerHandler());
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
        new TimeServer().bind(port);
    }
}
