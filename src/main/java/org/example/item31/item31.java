package org.example.item31;

import java.util.*;

public class item31 {

    public static void main(String[] args) {
        StackV1<Number> numberStackV1 = new StackV1<>();
        List<Number> numVal = List.of(1, 2, 3.4, 4L);
        List<Integer> intVal = List.of(5, 6);

        numberStackV1.pushAll(numVal);
        //java: incompatible types: java.util.List<java.lang.Integer> cannot be converted to java.lang.Iterable<java.lang.Number>
        //numberStackV1.pushAll(intVal);

        Collection<Object> objects = new ArrayList<>();
        //java: incompatible types: java.util.Collection<java.lang.Object> cannot be converted to java.util.Collection<java.lang.Number>
        //numberStackV1.popAll(objects);

        StackV2<Number> numberStackV2 = new StackV2<>();
        numberStackV2.pushAll(numVal);
        numberStackV2.pushAll(intVal);
        numberStackV2.popAll(objects);

        Set<Integer> integers = Set.of(1, 3, 5);
        Set<Double> doubles = Set.of(2.0, 4.0, 6.0);
        Set<Number> union = union(integers, doubles);
    }

    // item 30의 union 한정적 와일드카드 타입 사용 버전
    public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    // 30-7 max의 와일드카드 타입 사용 버전
    public static <E extends Comparable<? super E>> Optional<E> max(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return Optional.empty();
        }

        E result = null;

        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return Optional.of(result);
    }

    //public static <E> void swap(List<E> list, int i, int j){};

    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }

    // 와일드카드 타입을 실제 타입으로 바꿔주는 private 도우미 메서드
    private static <E> void swapHelper(List<E> list, int i, int j){
        list.set(i, list.set(j, list.get(i)));
    };
}
