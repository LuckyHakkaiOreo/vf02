package com.oreo.netty.learn.netty_study.nio.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class MultiThreadNIOServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(1234));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if(selector.selectNow() < 0) {
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = acceptServerSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    log.info("Accept request from {}", socketChannel.getRemoteAddress());
                    // 注册完SocketChannel的OP_READ事件
                    SelectionKey readKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    // 注册完SocketChannel的OP_READ事件后，
                    // 可以对相应的SelectionKey attach一个对象
                    // （本例中attach了一个Processor对象，该对象处理读请求），
                    // 并且在获取到可读事件后，可以取出该对象。
                    readKey.attach(new Processor());
                } else if (key.isReadable()) {
                    // 并且在获取到可读事件后，可以取出attach对象。
                    // 注：attach对象及取出该对象是NIO提供的一种操作，但该操作并非Reactor模式的必要操作，只是为了方便演示NIO的接口。
                    Processor processor = (Processor) key.attachment();
                    processor.process(key);
                }
            }
        }
    }
}
