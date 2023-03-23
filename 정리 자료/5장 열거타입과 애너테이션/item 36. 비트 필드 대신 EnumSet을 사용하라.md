# item 36. 비트 필드 대신 EnumSet을 사용하라

### 비트 필드 열거 상수

열거한 값들이 주로 (단독이 아닌) 집합으로 사용될 경우, 예전에는 각 상수에 서로 다른 2의 거듭 제곱값을 할당한 정수 열거 패턴을 사용해왔다.

```java
// 코드 36-1 비트 필드 열거 상수 - 구닥다리 기법!
public class Text {
    public static final int STYLE_BOLD          = 1 << 0; // 1
    public static final int STYLE_ITALIC        = 1 << 1; // 2
    public static final int STYLE_UNDERLINE     = 1 << 2; // 4
    public static final int STYLE_STRIKETHROUGH = 1 << 3; // 8

    // 매개변수 styles는 0개 이상의 STYLE_ 상수를 비트별 OR한 값이다.
    public void applyStyles(int styles) {}

}
```

다음과 같은 식으로 비트별 OR를 사용해 여러 상수를 하나의 집합으로 모을 수 있으며, 이렇게 만들어진 집합을 비트 필드라 한다.

```java
text.applyStyles(STYLE_BOLD | STYLE_ITALIC);
```

비트 필드를 사용하면 비트별 연산을 사용해 합집합과 교집합과 같은 집합 연산을 효율적으로 수행할 수 있다.

하지만 비트 필드는 정수 열거 상수의 단점을 그대로 지니며, 추가로 다음과 같은 문제까지 안고간다.

- 비트 필드값이 그대로 출력되면 단순한 정수 열거 상수를 출력할 때보다 해석하기 어렵다
- 비트 필드 하나에 녹아있는 모든 원소를 순회하기도 까다롭다.
- 최대 몇 비트가 필요한지 API 작성 시 미리 예측하여 적절한 타입(보통은 int나 long)을 선택해야 한다. API를 수정하지 않고는 비트 수를 더 늘릴 수 없다.

### EnumSet

위 비트 필드 열거 상수의 대안으로 java.util 패키지의 EnumSet 클래스는 열거 타입 상수의 값으로 구성된 집합을 효과적으로 표현해준다.

**장점**

- Set 인터페이스를 완벽히 구현하였다.
- 타입 안전
- 다른 어떤 Set 구현체와도 함께 사용할 수 있다.
- EnumSet의 내부는 비트 벡터로 구현되어있어 원소가 총 64개 이하라면, 즉 대부분의 경우에 EnumSet 전체를 long 변수 하나로 표현하여 비트 필드에 비견되는 성능을 보여준다.
- removeAll과 retainAll 같은 대량 작업은 비트 필드를 사용할 때 쓰는 것과 같은 비트를 효율적으로 처리할 수 있는 산술 연산을 써서 구현하였다.
- 비트를 직접 다룰 때 흔희 격는 오류에서 해방된다.
- 일반적인 Set보다 계산이 빠르다.

```java
// 코드 36-2 EnumSet - 비트 필드를 대체하는 현대적 기법
public class TextV2 {
    public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }
    
    // 어떤 Set을 넘겨도 되나, EnumSet이 가장 좋다
    public void applyStyles(Set<Style> styles) {}
}
```

```java
public static void main(String[] args) {
    TextV2 textV2 = new TextV2();
    textV2.applyStyles(EnumSet.of(Style.BOLD, Style.UNDERLINE));
}
```

applyStyles 메서드가 `EnumSet<Style>`이 아닌 `Set<Style>`을 받은 이유

모든 클라이언트가 `EnumSet`을 건네리라 짐작되는 상황이라도 이왕이면 인터페이스로 받는 것이 좋은 습관이다.

⇒ 클라이언트가 다른 Set을 넘기더라도 처리할 수 있기 때문이다.

**사용법**

EnumSet 의 대부분의 메서드는 인스턴스를 생성하는 메서드를 제외하고는 다른 Set처럼 동작합니다.

생성 메서드에 대해 알아봅시다.

```java
// 모든 요소를 포함한 EnumSet
EnumSet<Style> allOfStyle = EnumSet.allOf(Style.class); // BOLD, ITALIC, UNDERLINE, STRIKETHROUGH

// Style의 빈 컬렉션 생성
EnumSet<Style> noneOfStyle = EnumSet.noneOf(Style.class); // 빈 컬렉션

// 매개변수로 전달된 요소를 제외한 Set 생성
EnumSet<Style> complementOfSet = EnumSet.complementOf(EnumSet.of(Style.BOLD, Style.ITALIC)); // UNDERLINE, STRIKETHROUGH
```

### 정리

열거할 수 있는 타입을 한데 모아 집합 형태로 사용한다고 해도 비트 필드를 사용할 이유는 없다.

EnumSet 클래스가 비트 필드 수준의 명료함과 성능을 제공하고 아이템 34에서 설명한 열거 타입의 장점까지 선사하기 때문이다.

EnumSet의 유일한 단점이라면, 불변 EnumSet을 만들 수 없다는 것이다. 대신 Collections.unmodifiableSet으로 EnumSet을 감싸 사용할 수 있다.