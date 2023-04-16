package org.example.item62;

// 코드 62-3 Key 클래스로 권한을 구분했다.
public class ThreadLocalV2 {
    private ThreadLocalV2() {} // 객체 생성불가

    public static class Key { // (권한)
        Key() {}
    }

    // 위조 불가능한 고유 키를 생성한다.
    public static Key getKey(){
        return new Key();
    }


    public static void set(Key key, Object object);

    public static Object get(Key key);
}
