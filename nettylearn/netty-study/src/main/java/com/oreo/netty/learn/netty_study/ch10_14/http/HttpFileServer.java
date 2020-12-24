package com.oreo.netty.learn.netty_study.ch10_14.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpFileServer {
    private static final String DEFAULT_URL = "/src/main/java/com/oreo/netty/learn/netty_study";

    public void run(final int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            // 请求消息解码器
                            ch.pipeline().addLast("http-decoder",
                                    new HttpRequestDecoder());
                            // 目的是将多个消息转换为单一的request或者response对象
                            // HttpObjectAggregator解码器：将多个消息转换为单一的 FullHttpRequest或者 FullHTTPResponse，
                            // 原因是Http解码器在每个http消息中会生成多个消息对象：
                            // (1) HttpRequest/HttpResponse
                            // (2) HttpClient
                            // (3) LastHttpContent
                            ch.pipeline().addLast("http-aggregator",
                                    new HttpObjectAggregator(65536));
                            //响应解码器
                            ch.pipeline().addLast("http-encoder",
                                    new HttpResponseEncoder());
                            //ChunkedWriteHandler: 目的是支持异步大文件传输（）
                            ch.pipeline().addLast("http-chunked",
                                    new ChunkedWriteHandler());
                            // 业务逻辑
                            ch.pipeline().addLast("fileServerHandler",
                                    new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture future = b.bind("192.168.0.28", port).sync();
            System.out.println("HTTP文件目录服务器启动，网址是 : " + "http://192.168.0.28:"
                    + port + url);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8001;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String url = DEFAULT_URL;
        if (args.length > 1) {
            url = args[1];
        }
        new HttpFileServer().run(port, url);
    }
}
