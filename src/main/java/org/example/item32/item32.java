package org.example.item32;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class item32 {

    public static void main(String[] args) {
        varargs("이렇게 ", "여러개의 ", "매개변수를 ", "넘길 수 있습니다");
        List<String> strings1 = List.of("aaa", "bbb", "ccc");
        dangerous(strings1);

        // Exception in thread "main" java.lang.ClassCastException: class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String; ([Ljava.lang.Object; and [Ljava.lang.String; are in module java.base of loader 'bootstrap') at org.example.item32.item32.main(item32.java:14)
        String[] attributes = pickTwo("좋은", "빠른", "저렴한");
        List<String> strings = pickTwoV2("좋은", "빠른", "저렴한");

        List<String> strings2 = List.of("aaa", "bbb", "ccc");
        List<String> flatten1 = flatten(strings1, strings2);
        List<String> flatten2 = flatten(List.of(strings1, strings2));
    }

    static void varargs(String... string) {
        for (String s : string) {
            System.out.print(s);
        }
    }

    // 코드 32-1 제네릭과 varargs를 혼용하면 타입 안전성이 깨진다!
    static void dangerous(List<String>... stringLists) {
        List<Integer> intList = List.of(42);
        Object[] objects = stringLists;
        objects[0] = intList; // 힙오염 발생
        String s = stringLists[0].get(0); // ClassCastException
    }

    // 코드 32-2 자신의 제네릭 매개변수 배열의 참조를 노출한다. - 안전하지 않다!
    static <T> T[] toArray(T... args) {
        return args;
    }

    // T타입 인수 3개를 받아 그중 2개를 무작위로 골라 담은 배열을 반환하는 메서드
    static <T> T[] pickTwo(T a, T b, T c) {
        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0: return toArray(a, b);
            case 1: return toArray(a, c);
            case 2: return toArray(b, c);
        }
        throw new AssertionError();
    }

    // 코드 32-5 제네릭 varargs 매개변수를 List로 대체한 예 - 타입 안전하다
    static <T> List<T> pickTwoV2(T a, T b, T c) {
        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0: return List.of(a, b);
            case 1: return List.of(a, c);
            case 2: return List.of(b, c);
        }
        throw new AssertionError();
    }

    // 코드 32-3 제네릭 varargs 매개변수를 안전하게 사용하는 메서드
    @SafeVarargs
    static <T> List<T> flatten(List<? extends T>... lists){
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    // 코드 32-4 제네릭 varargs 매개변수를 List로 대체한 예 - 타입 안전하다
    static <T> List<T> flatten(List<List<? extends T>> lists){
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

}
