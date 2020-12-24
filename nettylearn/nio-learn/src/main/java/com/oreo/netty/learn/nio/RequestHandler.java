package com.oreo.netty.learn.nio;

/**
 * Created by Jack
 * Create in 1:21 2018/11/22
 * Description:
 */
public class RequestHandler {
    public String handle(String request){
        return "From NIOServer Hello " + request + ".\n";
    }
}
