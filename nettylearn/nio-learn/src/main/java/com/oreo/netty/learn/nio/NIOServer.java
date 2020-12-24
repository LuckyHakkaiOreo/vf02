package com.oreo.netty.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Jack
 * Create in 1:50 2018/11/22
 * Description:
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(9999));
        System.out.println("NIO NIOServer has started,listening on port:" + serverChannel.getLocalAddress());
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        RequestHandler requestHandler = new RequestHandler();
        while (true) {
            //唯一一个会阻塞的地方，所有的等待都汇集到这边
            int select = selector.select();
            if (select == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = channel.accept();
                    System.out.println("Connection from " + clientChannel.getRemoteAddress());
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.read(buffer);
                    String request = new String(buffer.array()).trim();
                    buffer.clear();
                    System.out.println(String.format("From %s : %s", channel.getRemoteAddress(), request));
                    String response = requestHandler.handle(request);
                    channel.write(ByteBuffer.wrap(response.getBytes()));
                }
                iterator.remove();
            }
        }
    }
}













