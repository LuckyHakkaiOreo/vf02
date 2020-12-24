package com.oreo.netty.learn.netty_study.nio.reactor;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Processor {
    private static final ExecutorService service = Executors.newFixedThreadPool(16);

    public void process(SelectionKey selectionKey) {
        service.submit(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int count = socketChannel.read(buffer);
            if (count < 0) {
                socketChannel.close();
                selectionKey.cancel();
                log.info("{}\t Read ended", socketChannel);
                return null;
            } else if(count == 0) {
                return null;
            }
            log.info("{}\t Read message {}", socketChannel, new String(buffer.array()));
            return null;
        });
    }
}
