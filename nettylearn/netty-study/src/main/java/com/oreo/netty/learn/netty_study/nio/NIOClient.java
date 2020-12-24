package com.oreo.netty.learn.netty_study.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(2345);
        socketChannel.connect(address);
//        URL resource = NIOClient.class.getClassLoader().getResource("test.txt");
        RandomAccessFile file = new RandomAccessFile(
                NIOClient.class.getClassLoader().getResource("test.txt").getFile(), "rw");
        FileChannel channel = file.getChannel();
        channel.transferTo(0, channel.size(), socketChannel);
        channel.close();
        file.close();
        socketChannel.close();
    }
}
