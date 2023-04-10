package org.example.item30;

import java.util.*;
import java.util.function.UnaryOperator;

public class item30 {

//    public static Set union(Set s1, Set s2) {
//        Set result = new HashSet(s1);
//        result.addAll(s2);
//        return result;
//    }

    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    private static UnaryOperator<Object> IDENTITY_FN = (t) -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FN;
    }

    public static void main(String[] args) {
        Set<String> backend = Set.of("한용구", "이상민", "엄영범", "심의진", "이한솔");
        Set<String> front = Set.of("윤동우", "독고현", "유병재", "박태준");
        Set<String> jobdaAtsDevelop1Cell = union(backend, front);
        System.out.println(jobdaAtsDevelop1Cell);

        String[] strings = {"쌈베", "대마", "나일론"};
        UnaryOperator<String> sameString = identityFunction();
        for (String s : strings) {
            System.out.println(sameString.apply(s));
        }

        Number[] numbers = {1, 2.0, 3L};
        UnaryOperator<Number> sameNumber = identityFunction();
        for (Number n : numbers) {
            System.out.println(sameNumber.apply(n));
        }
    }
    // 코드 30-7 구현 컬렉션에서 최대값을 반환한다. - 재귀적 타입 한정 사용
    public static <E extends Comparable<E>> E  max(Collection<E> c) {
        if (c.isEmpty()) {
            throw new IllegalArgumentException("빈 컬렉션");
        }

        E result = null;

        for (E e :c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return result;
    }
}
