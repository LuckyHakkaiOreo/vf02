package com.oreo.netty.learn.netty_study.nio.doubleReactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

@Slf4j
public class DoubleNIOServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(1234));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        int coreNum = Runtime.getRuntime().availableProcessors();
        Processor[] processors = new Processor[2 * coreNum];
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new Processor();
        }

        int index = 0;
        // mainReactor，只用来处理客户端的连接事件
        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            for (SelectionKey key : keys) {
                keys.remove(key);
                if (key.isAcceptable()) {
                    ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = acceptServerSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    log.info("Accept request from {}", socketChannel.getRemoteAddress());
                    // 每接受到一个连接，就交给processor去处理
                    Processor processor = processors[(int) ((index++) % coreNum)];
                    // 为该客户端注册读取事件
                    processor.addChannel(socketChannel);
                    processor.wakeup();
                }
            }
        }
    }
}
