package org.example.item42;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.*;

public class item42 {

    public static void main(String[] args) {

        List<String> words = new ArrayList<>(List.of("AAAAAA", "BB", "CCC", "DDD", "EEEE"));

        // 코드 42-1 익명 클래스의 인스턴스를 함수 객체로 사용 - 낡은 기법이다!
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });

        // 코드 42-2 람다식을 함수 객체로 사용 - 익명 클래스 대체
        Collections.sort(words, (s1, s2) -> Integer.compare(s1.length(), s2.length()));

        Collections.sort(words, comparingInt(String::length));

        words.sort(comparingInt(String::length));
        System.out.println(words);
    }
}
