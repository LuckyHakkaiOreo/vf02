package com.oreo.netty.learn.nio;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Jack
 * Create in 1:23 2018/11/22
 * Description:
 */
public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final RequestHandler requestHandler;

    public ClientHandler(Socket clientSocket, RequestHandler requestHandler) {
        this.clientSocket = clientSocket;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try (Scanner input = new Scanner(clientSocket.getInputStream())) {
            while (true) {
                String request = input.nextLine();
                if ("quit".equals(request)) {
                    break;
                }
                System.out.println(String.format("From %s : %s", clientSocket.getRemoteSocketAddress(), request));
                String response = requestHandler.handle(request);
                clientSocket.getOutputStream().write(response.getBytes());
            }
        }catch (IOException e){
            System.out.println("Caught exception: "+e);
            throw new RuntimeException(e);
        }
    }
}
