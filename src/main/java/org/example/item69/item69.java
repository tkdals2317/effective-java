package org.example.item69;

import java.util.*;

public class item69 {

    static class Mountain {
        public void climb() {

        }
    }
    public static void main(String[] args) {

        Mountain[] range = { new Mountain(), new Mountain()};

        // 코드 69-1 예외를 완전히 잘못 사용한 예
        try {
            int i = 0;
            while (true){
                range[i++].climb();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        
        // 표준적인 관용구대로 작성한 코드
        for (Mountain m : range) {
            m.climb();
        }

        List<Mountain> collection = new ArrayList<>();
        // 컬렉션을 이런 식으로 순회하지 말 것!
        try {
            Iterator<Mountain> i = collection.iterator();
            while (true) {
                Mountain mountain = i.next();
            }
        } catch (NoSuchElementException e) {

        }
    }

}
