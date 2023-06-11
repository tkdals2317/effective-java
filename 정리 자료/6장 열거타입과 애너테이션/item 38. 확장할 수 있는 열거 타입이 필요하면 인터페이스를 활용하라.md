# item 38. 확장할 수 있는 열거 타입이 필요하면 인터페이스를 활용하라

<aside>
💡 열거타입을 인터페이스로 확장하는 방법을 알아보자

</aside>

### 확장할 수 있는 열거 타입

확장할 수 있는 열거 타입이 어울리는 쓰임의 대표적인 예가 **연산 코드(operation code, opcode)**다

연산 코드의 각 원소는 특정 기계가 수행하는 연산을 뜻한다.

이따금 API가 제공하는 기본 연산 외 사용자 확장 연산을 추가할 수 있도록 열어줘야할 때가 있다.

### 확장할 수 있는 열거 타입 예제

연산 코드용 인터페이스를 정의하고 열거 타입이 이 인터페이스를 구현하게 만든다.

```java
// 코드 38-1 인터페이스를 이용해 확장 가능 열거 타입을 흉내 냈다.
public interface Operation {
    double apply(double x, double y);
}

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
```

열거 타입인 BasicOperation은 확장할 수 없지만 인터페이스인 Operation은 확장 할 수 있고, 이 인터페이스를 연산의 타입으로 사용하면 된다.

```java
// 코드 38-2 확장 가능 열거 타입
public enum ExtendedOperation implements Operation{
    EXP("^") {
        @Override public double apply(double x, double y) { return Math.pow(x, y); }
    },
    REMAINDER("%") {
        @Override public double apply(double x, double y) { return x % y; }
    };

    private final String symbol;
    ExtendedOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
```

**위 코드가 코드 34-5 상수별 메서드 구현(추상 메서드)과 다른점**

새로 작성한 연산은 기존 연산을 쓰던 곳이면 어디든 쓸 수 있다.

Operation 인터페이스를 사용하도록 작성되어 있기만 하면 된다.

apply가 인터페이스에 선언되어 있으니 열거타입에 따로 추상 메서드로 선언하지 않아도 된다.

### 테스트

```java
public static void main(String[] args) {
        double x = 4.0000;
        double y = 2.0000;
        testV1(ExtendedOperation.class, x, y);
        testV2(Arrays.asList(ExtendedOperation.values()), x, y);
    }

    private static <T extends Enum<T> & Operation> void testV1(Class<T> opEnumType, double x, double y) {
        for (Operation op : opEnumType.getEnumConstants()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

    private static void testV2(Collection<? extends Operation> opSet, double x, double y) {
        for (Operation op : opSet) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

==== output =====
4.000000 ^ 2.000000 = 16.000000
4.000000 % 2.000000 = 0.000000
4.000000 ^ 2.000000 = 16.000000
4.000000 % 2.000000 = 0.000000
```

**testV1 메서드**

ExtendedOperation의 class 리터럴(한정적 타입 토큰 역할)을 넘겨 확장된 연산들이 무엇인지 알려준다.

`<T extends Enum<T> & Operation>` : Class 객체가 열거 타입인 동시에 Operation의 하위 타입이어야 한다는 뜻

열거타입이어야 원소를 순회할 수 있고, Operation이어야 원소가 뜻하는 연산을 수행할 수 있기 때문이다.

**testV2 메서드**

Class 객체 대신 한정적 와일드 카드 타입인 `Collection<? extends Operation>` 을 넘기는 방법이다

여러 구현 타입의 연산을 조합해 호출 할 수 있게 되어 testV1 보다 더 유연하다.

```java
List<Operation> extendedOperations = Arrays.asList(ExtendedOperation.values());
List<Operation> basicOperations = Arrays.asList(BasicOperation.values());
List<Operation> allOperation = new ArrayList<>();
allOperation.addAll(extendedOperations);
allOperation.addAll(basicOperations);
testV2(allOperation, x, y);

==== output =====
4.000000 ^ 2.000000 = 16.000000
4.000000 % 2.000000 = 0.000000
4.000000 + 2.000000 = 6.000000
4.000000 - 2.000000 = 2.000000
4.000000 * 2.000000 = 8.000000
4.000000 / 2.000000 = 2.000000
```

### 인터페이스를 활용한 확장 가능한 열거 타입의 문제점

**열거 타입끼리 구현을 상속할 수 없다는 점이다.**

아무 상태에도 의존하지 않는 경우 **디폴트 구현**을 이용해 인터페이스에 추가하는 방법이 있다.

반면 Operation 예는 연산 기호를 저장하고 찾는 로직이 `BasicOperation`과 `ExtendedOperation` 모두에 들어가야만 한다. 

이 경우 중복량이 적으니 문제되지 않지만, 공유하는 기능이 많다면 그 부분은 별도의 도우미 클래스나 정적 도우미 메서드로 분리하는 방식으로 코드 중복을 없앨 수 있다.

### 정리

**열거 타입 자체는 확장할 수 없지만, 인터페이스와 그 인터페이스를 구현하는 기본 열거 타입을 함께 사용해 효과를 낼 수 있다.**

이렇게하면 클라이언트는 이 인터페이스를 구현해 자신만의 열거 타입을 만들 수 있다.그리고 API가 기본 열거 타입을 명시하지 않고 인터페이스 기반으로 작성되었다면 기본 열거 타입의 인스턴스가 쓰이는 모든 곳을 새로 확장한 열거 타입의 인스턴스로 대체해 사용할 수 있다.