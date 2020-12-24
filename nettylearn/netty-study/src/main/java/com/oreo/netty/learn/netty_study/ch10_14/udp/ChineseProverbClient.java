package com.oreo.netty.learn.netty_study.ch10_14.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class ChineseProverbClient {

    public void run(int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseProverbClientHandler());

            Channel channel = b.bind(0).sync().channel();
            channel.writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询?",
                            CharsetUtil.UTF_8), new InetSocketAddress(
                            "255.255.255.255", port))).sync();

            if (channel.closeFuture().await(15000)){
                log.info("查询超时");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new ChineseProverbClient().run(port);
    }
}
