package org.example.item81;

import java.util.concurrent.*;

public class item81 {


    private static final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

    // 코드 81-1 ConcurrentMap으로 구현한 동시성 정규화 맵 - 최적은 아니다.
    public static String internV1(String s) {
        String previousValue = map.putIfAbsent(s, s);
        return previousValue == null ? s : previousValue;
    }

    // 코드 81-2 ConcurrentMap으로 구현한 동시성 정규화 맵 - 더 빠르다.
    public static String internV2(String s) {
        String result = map.get(s);
        if (result == null) {
            result = map.putIfAbsent(s, s);
            if (result == null) {
                result = s;
            }
        }
        return result;
    }

    
    // 코드 81-3 동시 실행 시간을 재는 간단한 프레임워크
    public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            executor.execute(()->{
                // 타이머에게 준비를 마쳤음을 알린다.
                ready.countDown();
                try {
                    // 모든 작업자 스레드가 준비될 때까지 기다린다.
                    start.await();
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 타이머에게 작업을 마쳤음을 알린다.
                    done.countDown();
                }
            });
        }

        ready.await(); // 모든 작업자가 준비될 때까지 기다린다.
        long startNanos = System.nanoTime();
        start.countDown(); // 작업자들을 깨운다.
        done.await(); // 모든 작업자가 일을 끝마치기를 기다린다.
        return System.nanoTime() - startNanos;
    }


    public static void waitUsedMethod(Object object) {
        /*
        // 코드 81-4 wait 메서드를 사용하는 표준 방식
        synchronized (object){
            while (<조건이 충족되지 않았다>)
                object.wait(); // (락을 놓고, 깨어나면 다시 잡는다.)

            ... // 조건이 충족되었을 떄의 동작을 수행한다.
        }
        */
    }



}
