package org.example.item62;

// 코드 62-5 매개변수화하여 타입 안전성 확보
public final class ThreadLocalV4<T> {
    public ThreadLocalV4();
    public static void set(T object);
    public static T get();
}
