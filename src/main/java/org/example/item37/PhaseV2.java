package org.example.item37;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

// 코드 37-6 중첩 EnumMap으로 데이터와 열거 타입 쌍을 연결했다
public enum PhaseV2 {
    SOLID, LIQUID, GAS, PLASMA;

    public enum TransitionV2 {
        MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID),
        IONIZE(GAS, PLASMA), DEIONIZE(PLASMA, GAS);


        private final PhaseV2 from;
        private final PhaseV2 to;

        TransitionV2(PhaseV2 from, PhaseV2 to) {
            this.from = from;
            this.to = to;
        }

        // 상전이 맵을 초기화한다.
        private static final Map<PhaseV2, Map<PhaseV2, TransitionV2>> m = Stream.of(values())
                .collect(groupingBy(transition -> transition.from,
                        () -> new EnumMap<>(PhaseV2.class),
                        toMap(t -> t.to, t -> t, (x, y) -> y, () -> new EnumMap<>(PhaseV2.class))));

        //상 전이 맵 초기화 for문 버전
        private static final Map<PhaseV2, Map<PhaseV2, TransitionV2>> m2 =
                new EnumMap<PhaseV2, Map<PhaseV2, TransitionV2>>(PhaseV2.class);
        
        static{
            for(PhaseV2 p : PhaseV2.values())
                m2.put(p, new EnumMap<PhaseV2, TransitionV2>(PhaseV2.class));

            for(TransitionV2 trans : TransitionV2.values())
                m2.get(trans.from).put(trans.to, trans);
        }

        public static TransitionV2 from(PhaseV2 from, PhaseV2 to) {
            return m.get(from).get(to);
        }
    }

    public static void main(String[] args) {
        System.out.println(TransitionV2.from(PhaseV2.SOLID, PhaseV2.LIQUID)); // MELT
        System.out.println(TransitionV2.from(PhaseV2.SOLID, PhaseV2.GAS)); // SUBLIME
        System.out.println(TransitionV2.from(PhaseV2.GAS, PhaseV2.LIQUID)); // CONDENSE
    }
}