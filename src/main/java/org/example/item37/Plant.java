package org.example.item37;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class Plant {

    enum LifeCycle {ANNUAL, PERENNIAL, BIENNIAL}

    final String name;
    final LifeCycle lifeCycle;


    public Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }


    public static void main(String[] args) {
        // 코드 37-1 ordinal()을 배열 인덱스로 사용 - 따라하지 말것!
        // 정원에 심은 식물을 배열 하나로 관리
        Plant[] garden = { new Plant("장미", LifeCycle.PERENNIAL), new Plant("나팔꽃", LifeCycle.BIENNIAL)};

        // 생애주기별로 총 3개의 집합을 만든다.
        Set<Plant>[] plantsByLifeCycle = new Set[LifeCycle.values().length];
        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            plantsByLifeCycle[i] = new HashSet<>();
        }
        // 정원을 한바퀴 돌며 식물을 해당 해당 집합에 넣는다.
        for (Plant plant : garden) {
            plantsByLifeCycle[plant.lifeCycle.ordinal()].add(plant);
        }
        
        // 결과 출력
        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            System.out.printf("%s : %s%n", Plant.LifeCycle.values()[i], plantsByLifeCycle[i]);
        }

        // 코드 37-2 EnumMap을 사용해 데이터와 열거 타입을 매핑한다.
        EnumMap<LifeCycle, Set<Plant>> plantsByLifeCycleV2 = new EnumMap<>(LifeCycle.class);

        for (Plant.LifeCycle lc : Plant.LifeCycle.values()) {
            plantsByLifeCycleV2.put(lc, new HashSet<>());
        }

        for (Plant plant : garden) {
            plantsByLifeCycleV2.get(plant.lifeCycle).add(plant);
        }

        System.out.println(plantsByLifeCycleV2);

        // 코드 37-3 스트림을 사용한 코드 1 - EnumMap을 사용하지 않는다.
        System.out.println(Arrays.stream(garden).
                collect(groupingBy(p -> p.lifeCycle)));

        // 코드 37-4 스트림을 사용한 코드 2 - EnumMap을 이용해 데이터와 열거 타입을 매핑했다.
        System.out.println(Arrays.stream(garden)
                .collect(groupingBy(p -> p.lifeCycle, () -> new EnumMap<>(LifeCycle.class), toSet())));

    }
}
