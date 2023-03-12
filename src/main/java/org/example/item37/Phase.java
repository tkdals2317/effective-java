package org.example.item37;

// 코드 37-5 배열들의 배열의 인덱스에 ordinal()을 사용 - 따라하지 말 것!
public enum Phase {
    SOLID, LIQUID, GAS, PLASMA;
    public enum Transition {
        MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT, IONIZE, DEIONIZE;

        // 행은 from의 ordinal을, 열은 to의 ordinal을 인덱스로 쓴다.
        private static final Transition[][] Transitions = {
                {null, MELT, SUBLIME, null},
                {FREEZE, null, BOIL, null},
                {DEPOSIT, CONDENSE, null, IONIZE},
                {null, null, DEIONIZE, null}
        };

        // 한 상태에서 다른 상태로의 전이를 반환한다.
        public static Transition from(Phase src, Phase dst) {
            return Transitions[src.ordinal()][dst.ordinal()];
        }
    }

    public static void main(String[] args) {
        System.out.println(Transition.from(Phase.SOLID, Phase.LIQUID));
        System.out.println(Transition.from(Phase.SOLID, Phase.GAS));
        System.out.println(Transition.from(Phase.GAS, Phase.LIQUID));
    }
}