package com.oreo.netty.learn.nio;

import sun.misc.Request;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Jack
 * Create in 1:20 2018/11/22
 * Description:
 */
public class ServerRefactor {
    public static void main(String[] args) {
        RequestHandler requestHandler = new RequestHandler();
        try(ServerSocket serverSocket=new ServerSocket(8888)){
            System.out.println("NIOServer has started,listening on port:"+serverSocket.getLocalSocketAddress());
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection from " + clientSocket.getRemoteSocketAddress());
                new ClientHandler(clientSocket,requestHandler).run();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
