package org.example.item61;

import java.util.Comparator;

public class item61 {

    // 코드 61-1 잘못 구현된 비교자 - 문제를 찾아보자!
    static Comparator<Integer> naturalOrderV1 = (i, j) -> i < j ? -1 : (i == j ? 0 : 1);

    // 코드 61-2 문제를 수정한 비교자
    Comparator<Integer> naturalOrderV2 = (iBoxed, jBoxed) -> {
        int i = iBoxed, j = jBoxed;
        return i < j ? -1 : (i == j ? 0 : 1);
    };

    // 코드 61-4 끔직이 느리다! 객체가 만들어지는 위치를 찾았는 가?
    public static void main(String[] args) {
        System.out.println(naturalOrderV1.compare(new Integer(42), new Integer(42)));

        long sum = 0L;
        long beforeTime = System.currentTimeMillis();

        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        System.out.println(sum);

        long afterTime = System.currentTimeMillis();
        System.out.println("소요 시간(m) : " + (afterTime - beforeTime) / 1000);
    }


}
