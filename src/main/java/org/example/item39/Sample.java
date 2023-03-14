package org.example.item39;

// 코드 39-2 마커 애너테이션을 사용한 프로그램 예
public class Sample {
    @Test
    public static void m1() {}        // 성공해야 한다.
    public static void m2() {}              // 테스트가 아니다.
    @Test
    public static void m3() {         // 실패해야 한다.
        throw new RuntimeException("실패");
    }
    public static void m4() {}              // 테스트가 아니다.
    @Test
    public void m5() {}               // 잘못 사용한 예 : 정적 메서드가 아니다
    public static void m6() {}              // 테스트가 아니다.
    @Test
    public static void m7() {         // 실패해야한다.
        throw new RuntimeException("실패");
    }
    public static void m8() {}              // 테스트가 아니다.
}
