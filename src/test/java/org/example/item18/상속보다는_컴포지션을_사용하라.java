package org.example.item18;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

class 상속보다는_컴포지션을_사용하라 {

    @Test
    public void 상속을_잘못_사용한_예() {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(List.of("틱", "탁탁", "펑"));
        System.out.println(s.getAddCount()); // 3을 기대했지만 6이 나온다
    }

    @Test
    public void 컴포지션을_사용한_예() {
        InstrumentedHashSetWithComposition<String> s = new InstrumentedHashSetWithComposition<>(new HashSet<>());
        s.addAll(List.of("틱", "탁탁", "펑"));
        System.out.println(s.getAddCount()); // 3을 기대했지만 6이 나온다
    }

}