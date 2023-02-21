package org.example.item26;

import org.example.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class item26 {

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        unsafeAdd(strings, Integer.valueOf(42));
        String s = strings.get(0);
    }

    private static void unsafeAdd(List list, Object o) {
        // unchecked call 경고
        list.add(o);
    }

    // 잘못된 예 - 모르는 타입의 원소도 받는 로 타입을 사용했다.
    static int numElementsInCommon(Set set1, Set set2){
        int result = 0;
        for (Object o1 : set1) {
            if (set2.contains(o1))
                result++;
        }
        return result;
    }


    static int numElementsInCommonWildCard(Set<?> set1, Set<?> set2){
        int result = 0;
        // set1.add("string"); // 컴파일 에러
        // set1.add(null); // null은 추가가능
        for (Object o1 : set1) {
            if (set2.contains(o1))
                result++;
        }
        return result;
    }

}
