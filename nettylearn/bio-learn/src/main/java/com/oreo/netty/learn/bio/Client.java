package com.oreo.netty.learn.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class Client {

    private static final int DEFAULT_SERVER_PORT=51688;

    /**
     * 默认端口号：51688
     */
    private static final String DEFAULT_SERVER_IP="127.0.0.1";

    public static void send(String expression) {
        send(DEFAULT_SERVER_IP,DEFAULT_SERVER_PORT,expression);
    }

    private static void send(String defaultServerIp, int defaultServerPort, String expression) {
        log.info("算术表达式为：{}", expression);

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(defaultServerIp,defaultServerPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(expression);
            log.info("准备读取");
            log.info("结果为：{}", in.readLine());
            log.info("读取完毕");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }

            if (out != null){
                out.close();
                out = null;
            }

            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }


    }

    public static void main(String[] args) {
        Client.send("客户端发送的消息！");
    }


}
