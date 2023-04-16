package org.example.item62;

// 코드 62-4 리팩터링하여 Key를 ThreadLocal로 변경
public final class ThreadLocalV3 {
    public ThreadLocalV3();
    public static void set(Object object);
    public static Object get();
}
