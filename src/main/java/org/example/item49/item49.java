package org.example.item49;

import java.math.BigInteger;
import java.util.Objects;

public class item49 {

    public static void main(String[] args) {

        long[] longs = {0L, 1L, 2L};
        sort(longs, -1, 3);
        sort(longs, -2, -13);
    }

    // 코드 49-1 자바의 null 검사 기능 사용하기
    public void function1(BigInteger m){
        BigInteger bigInteger = Objects.requireNonNull(m, "m");
    }
    
    // 코드 49-2 재귀 정렬용 private 도우미 함수
    private static void sort(long a[], int offset, int length) {
        assert a != null;
        assert offset >= 0 && offset <= a.length;
        assert length >= 0 && length <= a.length - offset;
        // 계산 수행
    }
    
}
