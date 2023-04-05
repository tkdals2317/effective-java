package org.example.item52;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class item52 {
    public static void main(String[] args) {
        // 1번. Thread 생성자 호출
        new Thread(System.out::println).start();

        // 2번. ExecutorService의 submit 메서드 호출
        ExecutorService executorService = Executors.newCachedThreadPool();
        //executorService.submit(System.out::println); // 컴파일 에러
        boolean zzz = "zzz".contentEquals("zzz");
        System.out.println(zzz);
    }

}
