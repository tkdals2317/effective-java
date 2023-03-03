# item 24. 멤버 클래스는 되도록 static으로 만들라

<aside>
💡 **중첩 클래스를 언제 그리고 왜 사용해야 되는지 알아보자**

</aside>

### 중첩 클래스란?

중첩 클래스(nested class)란 다른 클래스 안에 정의된 클래스를 말한다.

중첩 클래스는 자신을 감싼 바깥 클래스에서만 쓰여야하며, 그 외의 쓰임새가 있다면 톱레벨 클래스로 만들어야 한다.

**중첩 클래스의 종류**

- 정적 멤버 클래스
- 비정적 멤버 클래스
- 익명 클래스
- 지역 클래스

이 중 정적 멤버 클래스를 제외한 나머지 클래스는 내부 클래스(inner class)에 해당한다.

### 1. 정적 멤버 클래스

정적 멤버 클래스는 다른 클래스 안에 선언되고, 바깥 클래스의 private 멤버에도 접근할 수 있다는 점만 제외하고는 일반 클래스와 똑같다.

```java
public class OuterClass {
    private int x = 10;
		
		// private으로 설정하면 바깥 클래스에서만 접근할 수 있다.
		private static class StaticMemberInnerClass {
        void x() {
            OuterClass outerClass = new OuterClass();
            outerClass.x = 100;
        }
    }
}
```

흔히 바깥 클래스와 함께 쓰일때만 유용한 pulbic 도우미 클래스로 쓰인다.

계산기가 지원하는 연산 종류를 정의하는 열거타입을 예로 들어보자.

```java
public class Calculator {

    private int operand1;
    private int operand2;

    public enum Operation {
        PLUS,
        MINUS
    }

}
```

Operation 열거 타입은 Calculator 클래스의 public 정적 멤버 클래스가 되어있다.

이렇게 되면 클라이언트가 `Calculator.Operation.*PLUS`* 나 `Calculator.Operation.*MINUS`* 와 같은 형태로 원하는 연산을 참조할 수 있다.

### 2. 비정적 멤버 클래스

정적 멤버 클래스와 비정적 멤버 클래스의 구문상 차이는 static이 붙어 있고 없고 뿐이지만, 의미상 차이는 의외로 크다.

비정적 멤버 클래스의 인스턴스는 바깥 클래스의 인스턴스와 암묵적으로 연결된다.(참조를 가지고 있다)

그래서 비정적 멤버 클래스의 인스턴스 메서드에서 정규화된 this를 사용하여 바깥 인스턴스의 메서드를 호출하거나 바깥 인스턴스의 참조를 가져올 수 있다.

```java
public class OuterClass {
    private int x = 10;

    private class NonStaticMemberInnerClass {
        void x() {
            OuterClass.this.x = 100; // this를 사용하여 바깥 인스턴스의 메서드나 인스턴스의 참조를 가져올 수 있다.
        }
    }
}
```

정규화된 `this`란 `클래스명.this` 형태로 바깥 클래스의 이름을 명시하는 용법을 말한다.

```java
@Test
void nonStaticMemberTest() {
    OuterClass.StaticMemberInnerClass staticMemberInnerClass = new OuterClass.StaticMemberInnerClass(); // 정적 멤버 클래스는 바깥 인스턴스 없이도 생성할 수 있다.
    OuterClass.NonStaticMemberInnerClass nonStaticMemberInnerClass = new OuterClass().new NonStaticMemberInnerClass(); // 비정적 멤버 클래스는 바깥 인스턴스 없이는 생성할 수 없다.
}
```

비정적 멤버 클래스는 바깥 인스턴스 없이는 생성할 수 없다.

개념상 중첩 클래스의 인스턴스가 바깥 인스턴스와 독립적으로 존재할 수 있다면 정적 멤버 클래스로 만들어야 한다.

비정적 멤버 클래스의 인스턴스와 바깥 인스턴스 사이의 관계는 멤버 클래스가 인스턴스화될 때 확립되며 더 이상 변경할 수 없다.

이러한 관계는 비정적 멤버 클래스의 인스턴스 안에 만들어져 메모리 공간을 차지하며 생성 시간도 더 오래 걸린다.

**그래서 비정적 멤버 클래스는 어디에 사용될까?**

어떤 클래스의 인스턴스를 감싸 마치 다른 클래스의 인스턴스처럼 보이게하는 뷰 역할로 어댑터 패턴을 정의할 때 자주 쓰인다.

주로 Map 인터페이스의 구현체들은 보통 (keySet, entrySet, values 메서드가 반환하는) 자신의 컬렉션 뷰를 구현할 때 비정적 멤버 클래스를 사용한다.

![Untitled](item%2024%20%E1%84%86%E1%85%A6%E1%86%B7%E1%84%87%E1%85%A5%20%E1%84%8F%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A2%E1%84%89%E1%85%B3%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%83%E1%85%AC%E1%84%83%E1%85%A9%E1%84%85%E1%85%A9%E1%86%A8%20static%E1%84%8B%E1%85%B3%E1%84%85%E1%85%A9%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%2086b490f86ec846a9b4f205dbc14227b3/Untitled.png)

비슷하게, Set과 List 같은 다른 컬렉션 인터페이스 구현들도 자신의 반복자를 구현할 때 비정적 멤버 클래스를 주로 사용한다.

![Untitled](item%2024%20%E1%84%86%E1%85%A6%E1%86%B7%E1%84%87%E1%85%A5%20%E1%84%8F%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A2%E1%84%89%E1%85%B3%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%83%E1%85%AC%E1%84%83%E1%85%A9%E1%84%85%E1%85%A9%E1%86%A8%20static%E1%84%8B%E1%85%B3%E1%84%85%E1%85%A9%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%2086b490f86ec846a9b4f205dbc14227b3/Untitled%201.png)

**멤버 클래스에서 바깥 인스턴스에 접근할 일이 없다면 무조건 static을 붙여서 정적 멤버 클래스로 만들자**

바깥 인스턴스로의 숨은 외부 참조로 인해 시간과 공간이 소비되고 가비지 컬렉션이 바깥 클래스의 인스턴스를 수거하지 못하는 메모리 누수가 생길 수도 있다.

멤버 클래스가 공개된 클래스의 public이나 protected 멤버라면 정적이냐 아니냐가 더 중요해진다. 한번 공개된 API는 향후에 static을 붙히면 하위 호환성이 깨지게 된다.

### 익명 클래스

익명 클래스는 말그대로 이름이 없는 클래스를 말한다.

또한 익명 클래스는 바깥 클래스의 멤버도 아니다. 멤버와 달리 쓰이는 시점에 선언과 동시에 인스턴스가 만들어진다.

오직 비정적인 문맥에서만 사용될 때만 바깥 클래스의 인스턴스를 참조할 수 있다.

정적 문맥에서라도 상수 변수 이외의 정적 멤버는 가질 수 없다. ⇒ 상수 표현을 위해 초기화된 final 기본 타입과 문자열 필드만 가질 수 있다.

**제약 사항**

- 선언한 지점에서만 인스턴스를 만들 수 있다.
- instansof 검사나 클래스의 이름이 필요한 작업은 수행할 수 없다.
- 여러 인터페이스를 구현할 수 없고 인터페이스를 구현하는 동시에 다른 클래스를 상속 받을 수도 없다.
- 가독성도 떨어진다.

**이렇게 제약 사항 많은 익명 클래스는 어디에 쓰일까?**

자바가 람다를 지원하기 전에 작은 함수 객체나 처리 객체를 만드는 데 사용했다.

또 다른 사용법은 정적 팩터리 메서드를 구현할 때다.

```java
// 골격 구현 클래스 AbstractList
static List<Integer> intArrayAsList(int[] a) {
    Objects.requireNonNull(a);
    return new AbstractList<>() {
        @Override
        public Integer get(int i) {
            return a[i];
        }

        @Override
        public Integer set(int i, Integer val) {
            int oldVal = a[i];
            a[i] = val;
            return oldVal;
        }

        @Override
        public int size() {
            return a.length;
        }
    };
}
```

 

### 지역 클래스

가장 드물게 사용된다.

멤버 클래스처럼 이름이 있고 반복해서 사용할 수 있다.

비정적 문맥에서만 사용 될 때만 바깥 인스턴스를 참조할 수 있다.

정적 멤버를 가질 수 없고 가독성을 위해 짧게 작성해야한다. 

### 정리

중첩 클래스에는 네가지가 있으며, 각각의 쓰임이 다르다.

메서드 밖에서도 사용해야하거나 메서드 안에 정의하기엔 너무 길다면 멤버 클래스로 만든다.

- 비정적 멤버 클래스 : 멤버 클래스의 인스턴스 각각이 바깥 인스턴스를 참조하는 경우
- 정적 멤버 클래스 : 멤버 클래스의 인스턴스 각각이 바깥 인스턴스를 참조하지 않는 경우
- 익명 클래스 : 중첩 클래스가 한 메서드 안에서만 사용되고 그 인스턴스를 생성하는 지점이 단 한곳이고 해당 타입으로 쓰기에 적합한 클래스나 인터페이스가 이미 있는 경우
- 지역 클래스 : 그 외(잘 쓸일 없을 거 같다)