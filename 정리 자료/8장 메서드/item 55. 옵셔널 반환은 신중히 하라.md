# item 55. 옵셔널 반환은 신중히 하라

<aside>
💡 옵셔널을 사용하는 방법을 알아보자

</aside>

### 자바 8 이전의 값을 반환 할 수 없을 경우

자바 8 이전에는 메서드가 특정 조건에서 값을 반환할 수 없을 때 취할 수 있는 선택지가 2개가 있었다.

예외를 던지거나, (반환 타입이 객체 참조라면) `null`을 반환하는 것이다.

두 가지 다 문제점이 있다.

예외를 던지는 경우 - 스택 추적 전체를 캡처하므로 비용이 비싸다.

`null`을 반환하는 경우 - `null` 처리 코드를 하지 않으면 `NullPointerException`이 발생 할 수 있다.

### 자바 8 이후 `Optional`의 등장

자바 8 이후로 `Optional`이라는 선택지가 추가 되었다.

- `Optional<T>` 는 `null`이 아닌 `T` 타입 참조를 하나 담거나, 혹은 아무것도 담지 않을 수 있다.
- 옵셔널은 원소를 최대 1개 가질 수 있는 ‘불변’ 컬렉션이다.
- 보통은 T를 반환해야 하지만 특정 조건에서는 아무것도 반환하지 않아야 할 때 `T` 대신 `Optional<T>` 를 반환하도록 선언하면 된다.
- 예외를 던지는 메서드보다 유연하고 사용하기 쉬우며, null을 반환하는 메서드보다 오류 가능성이 작다.

```java
// 코드 55-1 컬렉션에서 최댓값을 구한다(컬렉션이 비었으면 예외를 던진다.)
public static <E extends Comparable<E>> E  maxV1(Collection<E> c) {
    if (c.isEmpty()) {
        throw new IllegalArgumentException("빈 컬렉션");
    }

    E result = null;

    for (E e :c) {
        if (result == null || e.compareTo(result) > 0) {
            result = Objects.requireNonNull(e);
        }
    }
    return result;
}
```

아래 코드를 Optional로 반환하는 코드로 바꿔보자

```java
// 코드 55-2 컬렉션에서 최댓값을 구해 Optional<E>를 반환한다.
public static <E extends Comparable<E>> Optional<E> maxV2(Collection<E> c) {
    if (c.isEmpty()) {
        return Optional.empty(); 
    }

    E result = null;

    for (E e :c) {
        if (result == null || e.compareTo(result) > 0) {
            result = Objects.requireNonNull(e);
        }
    }
    return Optional.of(result);
}
```

빈 옵셔널은 `Optional.empty()` 을 팩터리 메서드로 반환하고 값이 있을 경우 `Optional.of(result)` 팩터리 메서드로 `Optional`에 담아 반환한다.

`Optional.of(result)` 의 경우 `null`을 넣으면 `NullPointerException`을 던지니 주의하자.

`null`을 허용하는 경우는 `Optional.ofNullable(value)`를 사용하면 된다.

**Optional을 반환하는 메서드에서는 절대 null을 반환하지 말자**

위 코드를 스트림 버전으로 작성해보자

```java
// 코드 55-3 컬렉션에서 최댓값을 구해 Optional<E>를 반환한다. - 스트림 버전
public static <E extends Comparable<E>> Optional<E> maxV3(Collection<E> c) {
    return c.stream().max(Comparator.naturalOrder());
}
```

Stream의 max 연산이 우리에게 필요한 옵셔널을 생성해준다.

### 옵셔널 활용

**옵셔널에서 값이 없을 경우 처리하는 방법**

1. 기본 값을 설정하는 방법 - orElse, orElseGet

```java
// 코드 55-4 옵셔널 활용 1 - 기본값을 정해둘 수 있다.
String lastWordInLexicon1 = maxV3(words).orElse("단어 없음...");
String lastWordInLexicon1_1 = maxV3(words).orElseGet(() -> new String("단어 없음ㅋㅋ..."));
```

기본 값을 설정하는 비용이 아주 큰 경우에는 `Supplier<T>` 를 인수로 받는 orElseGet을 사용하면 값이 처음 필요할 때 `Supplier<T>` 를 사용하여 생성하므로 초기 비용을 낮출 수 있다.

1. 원하는 예외를 던지는 방법 - orElseThrow 

```java
// 코드 55-5 옵셔널 활용 2 - 원하는 예외를 던질 수 있다.
String lastWordInLexicon2 = maxV3(words).orElseThrow(IllegalArgumentException::new);
```

실제 예외가 아니라 예외 팩터리를 건네어 예외가 실제로 발생하지 않는 한 예외 생성 비용은 들지 않는다.

1. 항상 값이 채워져있다고 가정한다. - get

```java
// 코드 55-6 옵셔널 활용 3 - 항상 값이 채워져 있다고 가정한다.
String lastWordInLexicon3 = maxV3(words).get();
```

만약 해당 메서드에서 값이 없다면 `NoSuchElementException` 이 발생한다.

---

**isPresent 메서드**

안전 벨브 역할의 메서드로, 옵셔널이 채워져 있으면 true를, 비어있으면 false를 반환한다.

이 메서드로 원하는 모든 작업을 수행할 수 있지만 신중히 사용해야한다.

실제로 isPresent를 쓴 코드 중 상당수는 다른 메서드로 대체할 수 있으며, 다른 메서드를 사용하는 것이 더 짧고 명확하고 용법에 맞는 코드가 될 가능성이 높다.

부모 프로세스의 프로세스 ID를 출력하거나 부모가 없다면 “N/A”를 출력하는 코드를 보자

```java
Optional<ProcessHandle> parentProcess = ph.parent();
System.out.println("부모 PID " + (parentProcess.isPresent() ? String.valueOf(parentProcess.get().pid()) : "N/A"));
// orElse로 대체 가능하다.
System.out.println("부모 PID " + (parentProcess.map(processHandle -> String.valueOf(processHandle.pid())).orElse("N/A")));
```

### Stream과 콜라보

스트림을 사용한다면 옵셔널들을 `Stream<Optional<T>>`로 받아서 그 중 채워진 옵셔널들에서 값을 뽑아 `Stream<T>`에 건네 담아 처리하는 방법이 있다.

```java
streamOfOptionals
  .filter(Optional::isPresent)
  .map(Optional::get);
```

자바 9에서부터 `Optional` 에 `Optional` 을 Stream으로 변환해주는 어댑터 메서드인 stream() 메서드가 추가 되었다.

옵셔널에 값이 있으면 그 값을 원소로 담은 스트림으로, 값이 없다면 빈 스트림으로 변환한다.

```java
// 위 코드를 Stream의 flatMap 메서드와 조합하여 좀 더 명료하게 만들어보자
streamOfOptional
	.flatMap(Optional::stream)
```

### 반환값으로 무조건 Optional이 옳은 것은 아니다.

컬렉션, 스트림, 배열, 옵셔널 같은 컨테이너 타입은 옵셔널로 감싸면 안된다.

대신 빈 List<T>를 반환하는 게 좋다.

빈 컨테이너를 그대로 반환하면 클라이언트에서 옵셔널 처리 코드를 넣지 않아도 된다.

Optional도 엄연히 새로 할당하고 초기화해야 되는 객체이고, 꺼내려면 메서드를 호출해야 하니 한단계를 더 거치게 된다.

그러니 성능이 중요한 상황에서는 옵셔널이 맞지 않을 수 있다.

맵의 키값으로 옵셔널을 사용하면 안된다. 맵 안에 키가 없다는 사실을 나타내는 방버이 두가지가 되게 된다.

**옵셔널을 컬렉션의 키, 값, 원소나 배열의 원소로 사용하는 게 적절한 상황은 거의 없다.**

옵셔널을 인스턴스 필드에 저장하는 경우도 특별한 에외 상황이 아니라면 좋지 않은 방법이다.

### Optional을 사용해야 하는 상황

결과가 없을 수 있고, 클라이언트가 이 상황을 특별하게 처리해야 한다면 Optional을 반환한다.

### 기본 타입 Optional

박싱된 기본 타입을 담는 옵셔널은 기본 타입 자체보다 무거울 수 밖에 없다. 

그래서 int, long, double 전용 옵셔널 클래스가 존재한다.

int - OptionalInt

long - OptionalLong

double - OptionalDouble

이렇게 대체제가 있으니 박싱된 기본 타입을 담은 온셔널을 반환하는 일은 없도록 하자

### 정리

값을 반환하지 못할 가능성이 있고, 호출할 때마다 반환값이 없을 가능성을 염두에 둬야하는 메서드라면 옵셔널을 반환하는 것을 고려해보자

성능이 중요한 경우는 null이나 예외를 던지는 방향이 나을 수 있다.

옵셔널은 왠만해서는 반환값 이외의 용도로 쓰지 말자