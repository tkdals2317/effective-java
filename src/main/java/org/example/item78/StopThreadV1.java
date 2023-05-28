package org.example.item78;

import java.util.concurrent.TimeUnit;

// 코드 78-1 잘못된 코드 - 이 프로그램은 얼마나 오래 실행될까?
public class StopThreadV1 {
    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(()->{
            int i = 0;
            while (!stopRequested) {
                i++;
            }
        });

        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
