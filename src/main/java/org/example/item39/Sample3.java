package org.example.item39;

import java.util.ArrayList;

// 코드 39-7 배열 매개변수를 받는 애너테이션을 사용하는 코드
public class Sample3 {

    @ExceptionArrTest({ IndexOutOfBoundsException.class, NullPointerException.class})
    public static void doublyBad() { // 성공해야 한다.
        ArrayList<String> list = new ArrayList<>();
        // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나 NullPointerException을 던질 수 있다.
        list.add(5, null);
    }
}
