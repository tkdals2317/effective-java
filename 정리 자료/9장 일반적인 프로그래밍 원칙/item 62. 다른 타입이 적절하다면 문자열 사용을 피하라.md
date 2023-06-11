# item 62. 다른 타입이 적절하다면 문자열 사용을 피하라

<aside>
💡 문자열은 텍스트를 표현하도록 설계되었으나 자바에서 너무 잘 지원해주어 의도하지 않은 용도로도 사용될 때가 있다.

이번 아이템에서는 문자열을 쓰지 않아야할 사례를 다룬다.

</aside>

### 문자열은 다른 값 타입을 대신하기에 적합하지 않다.

수치형 데이터 ⇒  int, float, BigInteger

예/아니오 ⇒ 적절한 열거 타입, boolean

문자열 ⇒ String

입력 받은 데이터가 진짜 문자열일 때만 String을 사용하자

기본타입이든 참조 타입이든 적절한 값 타입이 있다면 그것을 사용하고, 없다면 새로 하나 작성하라

### 문자열은 열거 타입을 대신하기에 적합하지 않다.

아이템 34에서 이야기했듯, 상수를 열거할 때는 문자열보다는 열거 타입이 월등히 낫다.

### 문자열은 혼합 타입을 대신하기에 적합하지 않다.

여러 요소가 혼합된 데이터를 하나의 문자열로 표현하는 것은 대체로 좋지 않은 생각이다.

```java
// 코드 62-1 혼합 타입을 문자열로 처리한 부적절한 예
String compoundKey = className + "#" + i.next();
```

혹여라두 두 요소를 구분해주는 문자 #이 두 요수 중에 하나에서 쓰였다면 혼란스러운 결과를 초래한다.

적절한 equals, toString, compareTo 메서드를 제공할 수 없으며, String이 제공하는 기능에만 의존해야한다.

이런 클래스는 보통 private 정적 멤버 클래스로 선언한다.

### 문자열은 권한을 표현하기에 적합하지 않다.

권한을 종종 문자열로 표현하는 경우가 종종 있다. 예르르 들어 스레드 지역변수 기능을 설계한다고 했을 떄 그 이름처럼 각 스레드가 자신만의 변수를 갖게 해주는 기능을 구현한다고 해보자.

과거에는 클라이언트가 제공한 문자열로 스레드별 지역 변수를 식별하게 개발하였다.

```java
// 코드 62-2 잘못된 예 - 문자열을 사용해 권한을 구분하였다.
public class ThreadLocalV1 {
    private ThreadLocalV1() {} // 객체 생성불가

    // 현 스레드의 값을 키로 구분해 저장한다.
    public static void set(String key, Object value);

    // (키가 가르키는) 현 스레드의 값을 반환한다.
    public static Object get(String key);
}
```

두 클라이언트가 같은 키 값을 사용하게 되면 의도치 않게 같은 변수를 공유하게 된다.

의도적으로 같은 키를 사용하여 다른 클라이언트의 값을 가져오므로 보안에도 취약하다.

문자열을 대신 위조할 수 없는 키를 사용해서 이런 문제를 해결 해보자

이 키를 권한(capacity)라고 한다.

```java
// 코드 62-3 Key 클래스로 권한을 구분했다.
public class ThreadLocalV2 {
    private ThreadLocalV2() {} // 객체 생성불가

    public static class Key { // (권한)
        Key() {}
    }

    // 위조 불가능한 고유 키를 생성한다.
    public static Key getKey(){
        return new Key();
    }

    public static void set(Key key, Object object);

    public static Object get(Key key);
}
```

이 방법은 앞서의 문자열 기반 API의 문제 두 가지를 해결해주지만 개선할 여지가 있다.

set, get 메섲드는 이제 정적 메서드일 필요 없으니 key 클래스의 인스턴스 메서드로 바꾸자.

이렇게 하면 Key는 더 이상 스레드 지역변수일 구분하기 위한 키가 아니라, 그 자체가 스레드 지역변수가 된다.

결과적으로 지금의 톱레벨 클래스인 ThreadLocal은 별달리 하는 일이 없어지므로 치워버리고, 중첩 클래스 Key의 이름을 ThreadLocal로 바꿔버리자

```java
// 코드 62-4 리팩터링하여 Key를 ThreadLocal로 변경
public final class ThreadLocalV3 {
    public ThreadLocalV3();
    public static void set(Object object);
    public static Object get();
}
```

하지만 이 API는 get으로 얻은 Object를 형변환하여 사용해야 하므로 타입 안전하지 않다.

ThreadLocal을 매개변수화 타입으로 선언해서 타입 안전하지 않은 문제를 해결하자.

```java
// 코드 62-5 매개변수화하여 타입 안전성 확보
public final class ThreadLocalV4<T> {
    public ThreadLocalV4();
    public static void set(T object);
    public static T get();
}
```

이 형태는 java.lang.ThreadLocal과 흡사하다.

문자열 기반 API의 문제러르 해결해주며, 키 기반 API보다 우아하고 빠르다.

### 정리

더 적합한 데이터 타입이 있거나 새로 작성할 수 있다면, 문자열을 쓰지말자

문자열을 잘못 사용하면 번거롭고, 덜 유연하고, 느리고, 오류 가능성도 크다.