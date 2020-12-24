package com.oreo.netty.learn.multi_thread;

import java.util.ArrayList;

public class MutiTreadTest {

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> list = new ArrayList<>();
        for (int i=0; i<100; i++){
            if (i == 99){
                Thread.sleep(10000);
                i=0;
            }

            SubTread subTread = new SubTread();
            subTread.start();
            list.add(subTread);

        }
    }

    static class SubTread extends Thread{

        @Override
        public void run() {
            while (true);
        }
    }
}
