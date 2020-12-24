package com.oreo.netty.learn.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class ServerHandler implements Runnable {

    private Socket socket;
    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String expression;
            String result;
            while (true) {
                // 如果读取的数据为空
                if ((expression = in.readLine()) == null) break;

                log.info("服务端收到信息：{}", expression);
                result = Calculator.cal(expression);
                log.info("计算结果:{}", result);
                out.println(result);

            }
        } catch (IOException e) {
            log.error("读取客户端消息报错：", e);
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
}
