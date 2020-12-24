package com.oreo.netty.learn.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

@Slf4j
public class BufferTest {
    public static void main(String[] args) throws IOException {
        CharBuffer charBuffer = CharBuffer.allocate(10);
        charBuffer.put('a');
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        charBuffer.put('b');
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        charBuffer.put('c');
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        charBuffer.put('d');
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        charBuffer.put('e');
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());

        charBuffer.flip();
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());

        log.info("value: {}",charBuffer.get());
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        log.info("value: {}",charBuffer.get());
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        log.info("value: {}",charBuffer.get());
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        log.info("value: {}",charBuffer.get());
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
        log.info("value: {}",charBuffer.get());
        log.info("charBuffer: capacity:{}， limit:{}，  position:{}", charBuffer.capacity(),charBuffer.limit(),charBuffer.position());
    }
}
