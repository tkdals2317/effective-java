package org.example.item86;

import java.io.Serializable;

// 코드 87-2 기본 직렬화 형태에 적합하지 않은 클래스
// 객체의 물리적 표현과 논리적 표현의 차이가 크다
public final class StringListV1 implements Serializable {

    private int size = 0;
    private Entry head = null;

    private static class Entry implements Serializable {
        String data;
        Entry next;
        Entry previous;
    }
}
