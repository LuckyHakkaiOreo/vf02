package com.oreo.netty.learn.bio;

import java.io.IOException;

/**
 * slf4j日志要生效：
 * 1.将log4j.properties加在resources目录下
 * 2.在pom文件中添加resource资源文件，扫描到我们的log4j.properties
 */
public class Main {
    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
