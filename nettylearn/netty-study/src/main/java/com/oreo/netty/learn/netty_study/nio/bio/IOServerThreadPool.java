package com.oreo.netty.learn.netty_study.nio.bio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class IOServerThreadPool {

    public static void main(String[] args) {
        // 固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(2345));
        } catch (IOException ex) {
            log.error("Listen failed", ex);
            return;
        }
        try{
            while(true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    try{
                        InputStream inputstream = socket.getInputStream();
                        log.info("Received message {}", IOUtils.toString(new InputStreamReader(inputstream)));
                    } catch (IOException ex) {
                        log.error("Read message failed", ex);
                    }
                });
            }
        } catch(IOException ex) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
            log.error("Accept connection failed", ex);
        }
    }
}
