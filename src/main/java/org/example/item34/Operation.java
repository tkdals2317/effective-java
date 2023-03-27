package org.example.item34;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Operation {
    PLUS("+") { @Override public double apply(double x, double y) { return x + y; } },
    MINUS("-") { @Override public double apply(double x, double y) { return x - y; } },
    TIMES("*") { @Override public double apply(double x, double y) { return x * y;} },
    DIVIDE("/") { @Override public double apply(double x, double y) {return x - y; } };
    private final String symbol;
    Operation(String symbol) {
        this.symbol = symbol;
    }

    // 코드 34-5 상수별 메서드 구현을 활용한 열거 타입
    public abstract double apply(double x, double y);

    public String symbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    // 34-7 열거 타입용 fromString 메서드 구현하기
    // 코드 46-4 toMap 수집기를 이용하여 열거 타입 상수에 매핑한다.
    public static final Map<String, Operation> stringToEnum =
            Stream.of(Operation.values()).collect(
                    Collectors.toMap(Object::toString, e -> e));

    public static Optional<Operation> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }

    // 34-10 switch 문을 이용해 원래 열거 타입에 없는 기능을 수행한다.
    public static Operation inverse(Operation op) {
        return switch (op) {
            case PLUS -> Operation.MINUS;
            case MINUS -> Operation.PLUS;
            case TIMES -> Operation.DIVIDE;
            case DIVIDE -> Operation.TIMES;
            default -> throw new AssertionError("알 수 없는 연산 : " + op);
        };
    }

    public static void main(String[] args) {

        double x = 3.5;
        double y = 2.0;
        for (Operation op : Operation.values()) {
            Operation plus = Operation.valueOf("PLUS");
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

    // 34-4 값에 따라 분기하는 열거 타입 상수가 뜯하는 연산을 수행한다.
    /*public double apply(double x, double y) {
        switch (this) {
            case PLUS:
                return x + y;
            case MINUS:
                return x - y;
            case TIMES:
                return x * y;
            case DIVIDE:
                return x / y;
        }
        throw new AssertionError("알 수 없는 연산 " + this);
    }*/
}
