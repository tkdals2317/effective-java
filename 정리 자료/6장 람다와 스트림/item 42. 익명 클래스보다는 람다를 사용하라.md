# item 42. 익명 클래스보다는 람다를 사용하라

<aside>
💡 자바 8에 추가된 람다와 추상 메서드를 비교해보고 사용법을 알아보자

</aside>

### 람다가 등장하기 전

자바에서 함수 타입을 표현할 때 추상 메서드를 하나만 담은 인터페이스(드물게는 추상 클래스)를 사용했다.

이런 인터페이스이 인스턴스를 함수 객체(function objcet)라고 하여, 특정 함수나 동작을 나타내는 데 썼다.

그리고 JDK 1.1이 등장하면서 함수 객체를 만드는 주요 수단은 익명 클래스가 되었다.

```java
// 코드 42-1 익명 클래스의 인스턴스를 함수 객체로 사용 - 낡은 기법이다!
Collections.sort(words, new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        return Integer.compare(o1.length(), o2.length());
    }
});
```

전략 패턴처럼, 함수 객체를 사용하는 과거 객체 지향 디자인 패턴에는 익명 클래스면 충분했다.

Comparator 인터페이스가 정렬을 담당하는 추상 전략을 뜻하며, 문자열을 정렬하는 구체적인 전략을 익명 클래스로 구현했다.

하지만 익명 클래스 방식은 코드가 너무 길어 함수형 프로그래밍에 적합하지 않다.

### 람다의 등장

자바 8에 와서 추상 메서드 하나짜리 인터페이스는 특별한 대우를 받아 함수형 인터페이스라 부르는 이 인터페이스들의 인스턴스를 람다식을 사용해 짧게 만들 수 있게 되었다.

람다는 함수나 익명 클래스와 개념은 비슷하지만 코드는 훨씬 간결하다.

```java
// 코드 42-2 람다식을 함수 객체로 사용 - 익명 클래스 대체
Collections.sort(words, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
```

위 코드를 보면 람다(`Comparator<String>`), 매개변수(s1, s2)(`String`), 반환값(`int`)의 타입에 대한 설명은 **생략**되고 컴파일러가 문맥을 살펴 **타입을 추론**해줌으로써 코드가 훨씬 간결해진 것을 볼 수 있다.

만약 컴파일러가 타입을 결정하지 못하는 경우에는 프로그래머가 직접 명시해야 한다.

**타입을 명시해야 코드가 더 명확할 때만을 제외하고는, 람다의 모든 매개변수 타입은 생략하자**

여기에 비교자 생성 메서드를 사용하면 코드를 더 간결하게 만들 수 있다.

```java
Collections.sort(words, comparingInt(String::length));
```

더 나아가 자바 8 때 List 인터페이스에 추가된 sort 메서드를 사용하면 더 짧아 진다.

```java
words.sort(comparingInt(String::length));
```

### 함수 객체를 실용적으로 사용해보자(With Operation)

```java
// 코드 42-3 상수별 클래스 몸체와 데이터를 사용한 열거 타입(코드 34-6)
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
}
```

위 코드를 람다를 사용하여 바꿔보자

방법은 간단하다!

1. 각 열거 타입의 상수의 동작을 람다로 구현해 생성자에 넘긴다. 
2. 생성자는 이 람다를 인스턴스 필드로 저장해둔다. 
3. 그런 다음 apply 메서드에서 필드에 저장된 람다를 호출하기만 하면 된다.

```java
// 코드 42-4 함수 객체(람다)를 인스턴스 필드에 저장해 상수별 동작을 구현한 열거 타입
public enum Operation {
    PLUS("+", (x, y) -> x + y),
    MINUS("-", (x, y) -> x - y),
    TIMES("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);
    private final String symbol;
    private final DoubleBinaryOperator op;

    Operation(String symbol, DoubleBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    public double apply(double x, double y) {
        return op.applyAsDouble(x, y);
    }

    @Override
    public String toString() {
        return symbol;
    }
}
```

<aside>
✅ 이 코드에서 열거 타입 상수의 동작을 표현한 DoublBinaryOperator 인터페이스 변수로 할당했다. DoubleBinaryOperator는 java.util.function 패키지가 제공하는 다양한 함수 인터페이스 중 하나로, double 타입 인수를 2개 받아 double 타입 결과를 돌려준다.

</aside>

### 람다 대신 상수별 클래스 몸체를 사용해야 할 때

람다의 단점 

1. 람다는 **이름이 없고 문서화도 못하므로 코드 자체로 동작이 명확히 설명 되지 않거나 코드 줄 수가 많아지면 람다를 쓰지말아야한다** 라는 점이다.
    
    ⇒람다는 한 줄일 때 가장 좋고 길어야 세 줄안에 끝내는 게 좋다.
    
2. 열거 타입 생성자에 넘겨지는 인수들의 타입도 컴파일 타임에 추론된다. 따라서 열거 타입 생성자 안의 람다는 열거 타입의 인스턴스 멤버에 접근할 수 없다.

**고로 상수별 동작을 단 몇 줄로 구현하기 어렵거나, 인스턴스 필드나 메서드를 사용해야만 하는 상황이라면 상수별 클래스 몸체를 사용해야 한다.**

### 람다 대신 익명 클래스를 사용해야 하는 경우

람다는 함수형 인터페이스에서만 사용되므로, 추상 클래스의 인스턴스를 만들 때 람다를 쓸 수 없으니 이때는 익명 클래스를 사용해야 한다.

비슷하게 추상 메서드가 여러개인 인터페이스의 인스턴스를 만들 때도 익명 클래스를 쓸 수 있다.

람다는 자신을 참조할 수 없다. 람다에서 this란 바깥 인스턴스를 가르킨다. 반면 익명 클래스에서의 this는 익명 클래스의 인스턴스 자신을 가리킨다.

**그래서 함수 객체가 자신을 참조해야 한다면 반드시 익명 클래스를 사용해야 한다.**

**람다도 익명 클래스처럼 직렬화 형태가 구현별로 다를 수 있다. 따라서 람다를 직렬화하는 일은 극히 삼가야 한다.**

### 정리

함수 객체의 역사

추상 메서드를 담은 인터페이스 ⇒ 익명 클래스 ⇒ 람다

**익명 클래스는 (함수형 인터페이스가 아닌) 타입의 인스턴스를 만들 때만 사용하라**

람다는 작은 함수 객체를 아주 쉽게 표현할 수 있어 함수형 프로그래밍의 지평을 열었다.