package com.oreo.netty.learn.nio;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jack
 * Create in 1:20 2018/11/22
 * Description:
 */
public class ServerThreadPool {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        RequestHandler requestHandler = new RequestHandler();
        try(ServerSocket serverSocket=new ServerSocket(8888)){
            System.out.println("NIOServer has started,listening on port:"+serverSocket.getLocalSocketAddress());
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection from " + clientSocket.getRemoteSocketAddress());
                executor.submit(new ClientHandler(clientSocket,requestHandler));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
