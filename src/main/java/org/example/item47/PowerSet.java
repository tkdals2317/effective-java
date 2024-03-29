package org.example.item47;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 코드 47-5 입력 집합의 멱집합을 전용 컬렉션에 담아 반환한다
public class PowerSet {
    public static final <E> Collection<Set<E>> of(Set<E> s) {
        List<E> src = new ArrayList<>(s);
        if (src.size() > 30)
            throw new IllegalArgumentException(
                    "집합의 원소가 너무 많습니다(최대 30개)." + s
            );

        return new AbstractList<Set<E>>() {
            @Override
            public Set<E> get(int index) {
                Set<E> result = new HashSet<>();
                for (int i = 0; index != 0; i++, index >>= 1) {
                    if ((index&1) == 1) {
                        result.add(src.get(i));
                    }
                }
                return result;
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof Set && src.containsAll((Set)o);
            }

            @Override
            public int size() {
                // 멱집합의 크기는 2를 원래 집합의 원소 수만큼 거듭제곱 것과 같다.
                return 1 << src.size();
            }
        };
    }

    public static void main(String[] args) {
        Set<String> set = Set.of("a", "b", "c");

        Collection<Set<String>> of = PowerSet.of(set);

        System.out.println(of.size());                  // 8
        System.out.println(of);                         // [[], [a], [c], [a, c], [b], [a, b], [b, c], [a, b, c]]
        System.out.println(of.contains(Set.of("a")));   // true
        System.out.println(of.contains(Set.of("d")));   // false

        Set<Integer> integerSet = new HashSet<>();
        IntStream.rangeClosed(0, 30)
                .forEach(integerSet::add);
        Collection<Set<Integer>> throwException = PowerSet.of(integerSet); // IllegalArgumentException: 집합의 원소가 너무 많습니다(최대 30개)

    }
}
