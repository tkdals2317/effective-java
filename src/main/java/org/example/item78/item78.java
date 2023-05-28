package org.example.item78;

import java.util.concurrent.atomic.AtomicLong;

public class item78 {

    // 코드 78-4 잘못된 코드 - 동기화가 필요하다!
    private static volatile int nextSerialNumber = 0;

    public static int generateSerialNumber() {
        return nextSerialNumber++;
    }
    
    // 코드 78-5 java.util.concurrent.atomic을 이용한 락-프리 동기화
    private static final AtomicLong nextSerialNum = new AtomicLong();
    
    public static long generateSerialNumberV2() {
        return nextSerialNum.getAndIncrement();
    }

}

