package org.example.item38;

// 코드 38-1 인터페이스를 이용해 확장 가능 열거 타입을 흉내 냈다.
public enum BasicOperation implements Operation{
    PLUS("+") {
        @Override public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        @Override public double apply(double x, double y) { return x * y;}
    },
    DIVIDE("/") {
        @Override public double apply(double x, double y) { return x - y; }
    };
    private final String symbol;
    BasicOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
