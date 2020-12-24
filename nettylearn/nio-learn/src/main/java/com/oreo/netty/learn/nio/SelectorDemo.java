package com.oreo.netty.learn.nio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

@Slf4j
public class SelectorDemo {

    public static class TCPEchoServer implements Runnable{

        /**
         * 服务器地址
         */
        private InetSocketAddress localAddress;

        public TCPEchoServer(int port){
            this.localAddress = new InetSocketAddress(port);
        }

        @SneakyThrows
        public void run() {
            Charset utf8 = Charset.forName("UTF-8");

            ServerSocketChannel ssc = null;
            Selector selector = null;

            Random random = new Random();
            try {
                // 创建选择器
                selector = Selector.open();

                // 创建服务器channel
                ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);

                // 设置监听服务器的端口，设置最大连接缓冲数为100
                ssc.bind(localAddress, 100);

                //服务器通道注册【客户端TCP连接】到selector
                ssc.register(selector, SelectionKey.OP_ACCEPT);

            } catch (IOException e) {
                log.info("服务器启动失败！");
                return;
            }

            log.info("服务器启动地址为：{}，端口为：{}", localAddress.getAddress(), localAddress.getPort());

            while (!Thread.currentThread().isInterrupted()){
                int n = selector.select();

                if (n == 0)
                    continue;

                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> it = keySet.iterator();
                SelectionKey key=null;

                while (it.hasNext()){
                    key = it.next();
                    // 在迭代器中删除当前需要处理的事件，避免以后被重复执行
                    it.remove();
                    // 表示捕获到accept连接事件
                    if (key.isAcceptable()){
                        // accept会返回一个普通通道，每个通道在内核中都对应一个socket缓冲区
                        // 表示一个客户端连接通道
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);

                        // 获取到连接后，可以针对该连接注册其读取事件
                        int opRead = SelectionKey.OP_READ;
//                        sc.register(selector, opRead, new Buffer)
                    }
                }


            }

        }
    }




}
