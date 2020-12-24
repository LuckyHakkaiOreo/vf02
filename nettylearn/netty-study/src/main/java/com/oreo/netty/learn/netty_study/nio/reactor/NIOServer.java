package com.oreo.netty.learn.netty_study.nio.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建Selector对象（Reactor）
        Selector selector = Selector.open();
        // ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定需要监听的端口
        serverSocketChannel.bind(new InetSocketAddress(1234));
        // 注册感兴趣的事件：连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // selector.select()是阻塞的，
        // 当有至少一个通道可用时该方法返回可用通道个数。
        // 同时该方法只捕获Channel注册时指定的所关注的事件（连接事件）。
        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    // 服务器端的socket
                    ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
                    // 客户端的socket
                    SocketChannel socketChannel = acceptServerSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    log.info("Accept request from {}", socketChannel.getRemoteAddress());
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    // 客户端的socket
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int count = socketChannel.read(buffer);
                    if (count <= 0) {
                        socketChannel.close();
                        key.cancel();
                        log.info("Received invalide data, close the connection");
                        continue;
                    }
                    log.info("Received message {}", new String(buffer.array()));
                }
                keys.remove(key);
            }
        }
    }
}
