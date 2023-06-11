# item 34. int 상수 대신 열거 타입을 사용하라

<aside>
💡 열거타입의 장점과 다양한 사용법을 알아보자

</aside>

### 정수 열거 패턴의 단점

```java
// 코드 34-1 정수 열거 패턴 - 상당히 취약하다
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int APPLE_GRANNY_SMITH = 2;

public static final int ORANGE_NAVEL = 0;
public static final int ORANGE_TEMPLE= 1;
public static final int ORANGE_BLOOD = 2;
```

열거 타입이 지원되기 전에는 정수 상수를 한 묶음에 선언하여 사용하였다.

**단점**

- 타입 안전을 보장할 수 없다.
- 표현력도 좋지 않다.
- 프로그램이 깨지기 쉽다.
- 문자열로 출력하기가 까다롭다.

정수 대신 문자열을 상수로 사용하는 열거 패턴은 더 나쁘다.

**단점**

- 문자열을 하드코딩해야 한다.
- 오타가 있어도 컴파일러는 확인을 못하므로 런타임에 버그가 생긴다
- 문자열 비교에 따른 성능 저하

### 열거 타입이란?

(1) 일정 개수의 상수 값을 정의 한 다음, 그 외의 값은 허용하지 않는 타입이다.

자바의 열거 타입은 완전한 형태의 클래스라서 다른 언어의 열거 타입보다 훨씬 강력하다.

```java
// 코드 34-2 가장 단수한 열거 타입
public enum Apple { FUJI, PIPPIN, GRANNY_SMITH }
public enum Orange { NAVEL, TEMPLE, BLOOD } 
```

열거 타입 자체는 클래스이며, 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공객한다.

열거 타입은 밖에서 접근할 수 있는 생성자를 제공하지 않으므로 사실상 final이다.

따라서 클라이언트가 인스턴스를 직접 생성하거나 확장할 수 없으니 열거 타입 선언으로 만들어진 인스턴스들은 딱 하나씩만 존재함이 보장 된다. 

⇒ 인스턴스 통제가 된다.

⇒ 싱글턴은 원소가 하나뿐인 열거 타입, 반대로 열거 타입은 싱글턴을 일반화한 형태

### 열거  타입의 장점

1. **컴파일 타임 타입 안전성을 제공한다.** 
    
    ex) Apple을 매개변수로 받는 메서드에서는 null을 제외하고 다른 타입의 값을 넘기려하면 컴파일 오류가 난다.
    
2. **각자의 이름공간이 있어서 이름이 같은 상수도 평화롭게 공존한다.**
    
    새로운 상수를 추가하거나 순서를 바꿔도 다시 컴파일 하지 않아도 된다.
    
3. **toString 메서드로 출력하기 적합한 문자열을 내어준다.**
4. **임의의 메서나 필드를 추가할 수 있고 인터페이스를 구현하게 할 수 있다.**
5. **Object 메서드들을 높은 품질로 구현해놓았다.**
6. **Comparable과 Serializable을 구현했으며, 그 직렬화 형태도 웬만해서 동작하게 구현해놓았다.**

### 열거타입에 메서드와 필드를 추가해서 사용하는 법

열거타입은 실제로는 클래스이므로 고차원의 추상 개념 하나를 완벽히 표현할 수 있다.

```java
// 코드 34-2 데이터와 메서드를 갖는 열거타입
public enum Planet {
    MERCURY (3.303e+23, 2.4397e6),
    VENUS   (4.869e+24, 6.0518e6),
    EARTH   (5.976e+24, 6.37814e6),
    MARS    (6.421e+23, 3.3972e6),
    JUPITER (1.9e+27,   7.1492e7),
    SATURN  (5.688e+26, 6.0268e7),
    URANUS  (8.686e+25, 2.5559e7),
    NEPTUNE (1.024e+26, 2.4746e7),
    PLUTO   (1.27e+22,  1.137e6);

    private final double mass;   // 질량
    private final double radius; // 반지름
    private final double surfaceGravity;

    // 중력 상수
    public static final double G = 6.67300E-11;

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        surfaceGravity = G * mass / (radius * radius);
    }
    public double mass()   { return mass; }
    public double radius() { return radius; }

    public double surfaceGravity() { return surfaceGravity; }

    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity; // F = ma
    }
}
```

**열거 타입 상수 각각을 특정 데이터와 연결지으려면 생성자에서 데이터를 받아 인스턴스 필드에 저장하면 된다.**

열거타입은 근본적으로 불변이므로 모든 필드는 final이어야 한다.

필드는 public으로 선언해도 되지만, private으로 감추고 public 접근자 메서드를 두는게 낫다.

```java
// 지구에서의 무게를 입력받아 여덟 행성에서의 무게를 출력하는 코드
public static void main(String[] args) {
    double earthWeight = 88.23;
    double mass = earthWeight/ Planet.EARTH.surfaceGravity();
    for (Planet p : Planet.values())
        System.out.printf("%s에서의 무게는 %f이다.%n", p, p.surfaceWeight(mass));
}
```

- 열거 타입은 자신 안에 정의된 상수들의 값을 배열에 담아 반환하는 정적 메서드인 values 메서드를 제공한다.
- 값들은 선언된 순서로 저장된다.
- 각 열거 타입 값의 toString 메서드는 상수 이름을 문자열로 반환하므로 출력하기 편하다.
- 열거 타입 중 하나를 제거하거나 추가한다 하더라도 위 기능은 정상적으로 동작한다.
- 만약 제거된 상수를 참조하는 코드에서는 컴파일 에러가 난다.
- 클래스 혹은 패키지에서만 유용한 기능는 노출해야 할 합당한 이유가 없다면 priavate이나 package-private으로 감추자

상수에 따라 동작이 달라져야 하는 상황은 어떨까?

```java
public enum Operation {
    PLUS {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    }, MINUS {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    }, TIMES {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    }, DIVIDE {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    };
    
    // 코드 34-5 상수별 메서드 구현을 활용한 열거 타입
    public abstract double apply(double x, double y);

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
```

주석 처리된 switch-case문으로 타입에 따라 연산을 수행하는 새로운 상수가 추가되면 case문도 추가해야되고 실수로 추가하지 않으면 런타임 에러가 나는 깨지기 쉬운 코드이다.

하지만 추상 메서드를 통해 상수별 메서드 구현을 하게 되면 새로운 상수 추가 시 apply를 재정의를 강제한다.

### 상수별 클래스 몸체와 데이터를 사용한 열거 타입

```java
// 코드 34-6 상수별 클래스 몸체(class body)와 데이터를 사용한 열거타입
public enum Operation {
    PLUS("+") { @Override public double apply(double x, double y) { return x + y; } },
    MINUS("-") { @Override public double apply(double x, double y) { return x - y; } }, 
    TIMES("*") { @Override public double apply(double x, double y) { return x * y;} },
    DIVIDE("/") { @Override public double apply(double x, double y) {return x - y; } };
    private final String symbol;
    Operation(String symbol) {
        this.symbol = symbol;
    }

    
    public abstract double apply(double x, double y);

    public String symbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
```

```java
public static void main(String[] args) {
    double x = 3.5;
    double y = 2.0;
    for (Operation op : Operation.values()) {
        System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }
}
```

### valueOf, toString, fromString

열거 타입에는 상수 이름을 받아 그 이름에 해당하는 상수를 반환해주는 valueOf 메서드가 자동으로 생성된다.

그리고 열거 타입의 toString 메서드를 재정의하려거든, toString이 반환하는 문자열을 해당 열거 상수 타입 상수로 변환해주는 fromString 메서드도 함께 제공해주는 것을 고려해보자

```java
// 34-7 열거 타입용 fromString 메서드 구현하기
public static final Map<String, Operation> stringToEnum =
        Stream.of(Operation.values()).collect(
                Collectors.toMap(Object::toString, e -> e));

public static Optional<Operation> fromString(String symbol) {
    return Optional.ofNullable(stringToEnum.get(symbol));
}
```

Optional로 반환하는 것은 주어진 문자열이 가르키는 연산이 존재하지 않을 수 있음을 클라이언트에 알리고, 그 상황을 클라이언트에서 대처하도록 한 것이다.

### 전략 열거 타입

```java
// 코드 34-9 전략 열거 타입 패턴
public enum PayrollDay {
    MONDAY(PayType.WEEKDAY),
    TUESDAY(PayType.WEEKDAY),
    WEDNESDAY(PayType.WEEKDAY),
    THURSDAY(PayType.WEEKDAY),
    FRIDAY(PayType.WEEKDAY),
    SATURDAY(PayType.WEEKEND),
    SUNDAY(PayType.WEEKEND);

    private final PayType payType;

    PayrollDay(PayType payType) {
        this.payType = payType;
    }

    int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    // 전략 열거 타입
    enum PayType {
        WEEKDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINS_PER_SHIFT ? 0 : (minsWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        };

        abstract int overtimePay(int mins, int payRate);

        private static final int MINS_PER_SHIFT = 8 * 60;

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }
}
```

위 코드에서는 PayrollDay에 중첩 열거 타입인 PayType이라는 잔업수당 전략 열거 타입을 선언하고 새로운 상수를 추가할 때 잔업수당 ‘전략’을 선택하게하게 한다.

잔업 수당을 계산을 전략 열거 타입에 위임하여 swith-case문이나 상수별 메서드 구현을 안해도 된다.

### 기존 열거 타입에 상수별 동작을 혼합해 넣을 때는 switch 문이 좋은 선택이 될 수 있다.

보통 switch문으로 열거 타입의 상수별 동작을 구현하는데는 적합하지는 않다.

하지만 기존 열거 타입에 상수별 동작을 혼합해 넣을 때는 switch 문이 좋은 선택이 될 수 있다.

```java
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
```

각 연산의 반대 연산을 반환하는 메서드가 필요한 경우 switch를 활용하여 사용 할 수 있다.

또는 추가하려는 메서드가 의미상 열거타입에 속하지 않는다면 직접 만든 열거타입이라도 switch를 사용하는 것이 좋다.

### 열거타입을 사용해야 하는 경우

**필요한 원소를 컴파일타임에 다 알 수 있는 상수 집합이라면 항상 열거타입을 사용하자**

열거 타입에 정의된 상수 개수가 영원히 고정 불변일 필요는 없다. 열거타입은 나중에 상수가 추가돼도 바이너리 수준에서 호환되도록 설계되었다.

### 정리

열거 타입은 정수 상수보다 뛰어나다.

단순히 상수로만 사용하는 것이 아니라 다양한 방법으로 열거타입을 사용할 수 있다.

- 열거타입에 필드나 메서드를 사용하는 방법
- 드물게 상수별로 다르게 동작할 때는 switch문을 사용하자
- 열거 타입 상수 일부가 같은 동작을 공유한다면 전략 열거 타입 패턴을 사용하자