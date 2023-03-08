package org.example.item34;

public class item34 {
    // 코드 34-1 정수 열거 패턴 - 상당히 취약하다
    public static final int APPLE_FUJI = 0;
    public static final int APPLE_PIPPIN = 1;
    public static final int APPLE_GRANNY_SMITH = 2;

    public static final int ORANGE_NAVEL = 0;
    public static final int ORANGE_TEMPLE= 1;
    public static final int ORANGE_BLOOD = 2;

    // 코드 34-2 가장 단수한 열거 타입
    public enum Apple { FUJI, PIPPIN, GRANNY_SMITH }
    public enum Orange { NAVEL, TEMPLE, BLOOD }

    public static void main(String[] args) {
        String fuji = Apple.FUJI.toString();

        double earthWeight = 88.23;
        double mass = earthWeight/ Planet.EARTH.surfaceGravity();
        for (Planet p : Planet.values())
            System.out.printf("%s에서의 무게는 %f이다.%n", p, p.surfaceWeight(mass));
    }
}
