package com.oreo.netty.learn.bio;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO的服务端
 */
@Slf4j
public class Server {
    /**
     * 默认端口号：51688
     */
    private static final int DEFAULT_SERVER_PORT =51688;

    /**
     * 单例的ServerSocket
     */
    private ServerSocket serverSocket;

    public void start() throws IOException {
        start(DEFAULT_SERVER_PORT);
    }

    public  synchronized void start(int port) throws IOException {
        if (serverSocket != null) return;

        try{
            serverSocket = new ServerSocket(port);
            log.info(">>>>>>>> 服务端已启动，绑定端口：{}", port);
            // 服务端自旋
            while (true){
                // 阻塞方法，获取到客户端socket连接
                Socket client = serverSocket.accept();
                new Thread(new ServerHandler(client)).start();
            }
        }finally {
            if (serverSocket != null){
                log.info("服务端已关闭！");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
