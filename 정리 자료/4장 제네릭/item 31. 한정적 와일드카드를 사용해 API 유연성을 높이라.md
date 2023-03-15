# item 31. 한정적 와일드카드를 사용해 API 유연성을 높이라

<aside>
💡 한정적 와일드카드를 사용하여 API 유연성을 높일 수 있는 방법을 알아보자

</aside>

### 용어 먼저 다시 복습하고 가자

**매개변수화 타입 :** `List<E>` 에서 `List<String>`은 `E`가 `String`으로 대체된 매개변수화된 타입

**제네릭 타입** : `List<E>`

**한정적 타입 매개변수** : `<E extends Number>`

**한정적 와일드카드 타입** : `List<? extends Number>`

**비한정적 와일드카드 타입** : `List<?>` 

**불공변** : `List<String>`은 `List<Object>`의 하위 타입이 아니다.

**공변** : `String`이 `Object`의 하위(서브)타입이면, `List<String>`은 `List<? extend Object>`의 하위(서브) 타입이다.

**반공변** : `String`이 `Object`의 하위(서브)타입이면, `List<Objcet>`는 `List<? super>`의 하위(서브)타입이다.

### 와일드카드 타입을 사용하지 않는 스택

```java
public void pushAll(Iterable<E> src) {
    for (E e : src) {
        push(e);
    }
}

public void popAll(Collection<E> dst){
    while (!isEmpty())
        dst.add(pop());
}
```

```java
public static void main(String[] args) {
    StackV1<Number> numberStackV1 = new StackV1<>();
    List<Number> numVal = List.of(1, 2, 3.4, 4L);
    List<Integer> intVal = List.of(5, 6);
		// 정상 동작
    numberStackV1.pushAll(numVal); 
    // incompatible types: java.util.List<java.lang.Integer> cannot be converted to java.lang.Iterable<java.lang.Number>
		numberStackV1.pushAll(intVal);

		Collection<Object> objects = new ArrayList<>();
    //java: incompatible types: java.util.Collection<java.lang.Object> cannot be converted to java.util.Collection<java.lang.Number>
    numberStackV1.popAll(objects);
} 
```

Integer는 Number의 하위 타입이니 정상 동작해야 될 것 같지만 매개 변수화 타입(`List<String>`)이 불공변(`Iterable<E>`)이므로 Integer 리스트를 인수로 넘겼을 때는 컴파일 오류가 발생한다.

또한 popAll 메서드 또한 비슷한 오류가 발생한다.

### 한정적 와일드카드 타입을 사용한 스택

위 문제점을 해결하기 위해 자바는 한정적 와일드카드 타입이라는 특별한 매개변수화 타입을 지원한다.

pushAll 메서드의 입력 매개변수 타입은 ‘E의 Iterable’이 아니라 ‘E의 하위 타입의 Iterable’이어야 한다 

⇒ 와일드카드 타입으로 표현하면 `Iterable<? extend E>` 가 된다.

```java
public void pushAll(Iterable<? extends E> src) { // 한정적 와일드카드 적용
    for (E e : src) {
        push(e);
    }
}
```

popAll 메서드 또한 ‘E의 Collection’이 아니라 ‘E의 상위 타입인 Collection’이어야 한다.

⇒ 와일드카드 타입으로 표현하면 `Collection<? super E>` 가 된다.

```java
public void popAll(Collection<? super E> dst){
    while (!isEmpty())
        dst.add(pop());
}
```

유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드 타입을 사용하라

하지만! 생산자 역할과 소비자 역할을 동시에 하면 타입을 정확히 지정해야 되는 상황이므로 와일드카드 타입을 쓰면 안된다.

### 쉽게 외우자 PECS 공식 : producer-extends, consumer-super

매개변수화 타입 T이 **생산자**(위 예제에서는 pushAll의 매개변수화 타입)라면 `<? extends T>`를 사용하고

매개변수화 타입 T가 **소비자**(위 예제에서는 popAll의 매개변수화 타입)라면 `<? super T>`를 사용하자

**PECS 공식을 적용한 item30의 union 메서드**

```java
public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}

public static void main(String[] args) {
        Set<Integer> integers = Set.of(1, 3, 5);
        Set<Double> doubles = Set.of(2.0, 4.0, 6.0);
        Set<Number> union = union(integers, doubles);
    }
```

반환 타입은 여전히 Set<E>라는 것을 주목하자.

반환 타입에는 한정적 와일드카드 타입을 사용하면 안된다. 오히려 클라이언트 코드에서 와일드카드 타입을 써야하기 때문이다.

클래스 사용자가 와일드카드 타입을 신경 써야한다면 그 API가 문제가 있을 가능성이 높다.

### 자바 7 이하에서의 사용법

자바 8부터는 타입 추론 기능이 강력해서 위와 같이 사용할 수 있지만 자바 7 이전에는 충분히 강력하지 못해 반환타입을 명시해야 한다.

이를 명시적 타입 인수라고 불르며 사용하는 방법은 아래와 같이 작성하면 된다.

```java
Set<Number> union = union.<Number>(integers, doubles);
```

### 한정적 와일드카드 타입의 복잡한 사용법

```java
// 30-7 max의 와일드카드 타입 사용 버전
public static <E extends Comparable<? super E>> Optional<E> max(Collection<? extends E> c) {
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

코드를 보기만해도 복잡하다.

하나하나 차근차근 뜯어보자

입력 매개변수 `Collection<E>`에서는 E 인스턴스를 생산하므로 `Collection<? extends E>` 로 수정했다.

반환 타입은 `<E extends Comparable<E>>` 에서 `<E extends Comparable<? super E>>` 로 수정했다.

이때 Comparable<E>는 E 인스턴스를 소비하므로 PECS 공식에 따르면 `<? super E>`를 적용해야한다.

**Comparable이나 Comparater의 경우 언제나 소비자이므로 항상 Comparable<? super E>를 사용하는 편이 낫다.**

### 타입 매개변수와 와일드카드

```java
// 비한정적 타입매개변수 사용한 swap
public static <E> void swap(List<E> list, int i, int j){};
// 비한정적 와일드카드를 사용한 swap
public static void swap(List<?> list, int i, int j){};
```

public API라면 간단한 두번 째 메서드가 낫다.

어떤 리스트든 이 메서드에 넘기면 명시한 인덱스의 원소를 교환해줄것이고 , 신경써야할 타입 매개변수도 없다.

**비한정적 와일드카드로 변경하기 위해 지켜야할 규칙**

기본 규칙 1. 메서드 선언에 타입 매개변수가 한 번만 나오면 와일드카드(?)로 대체하라

기본 규칙 2. 비한정적 타입 매개변수(`List<E>`)라면 비한정적 와일드 카드(`List<?>`)로 바꾸고, 한정적 타입 매개변수(`<E extends Number>`)라면 한정적 타입 와일드 카드(`List<? extends Number>`)로 바꾸면 된다. 

**비한정적 와일드카드로 변경한 메서드의 문제점**

```java
public static void swap(List<?> list, int i, int j) {
    list.set(i, list.set(j, list.get(i)));
}
```

직관적으로 구현한 코드가 컴파일 되지 않는다.

![Untitled](https://user-images.githubusercontent.com/49682056/225367571-ded60e79-8b34-4402-8591-d32dcdd64911.png)

원인은 리스트 타입이 List<?>인데, List<?>에는 null 이외에는 어떤 값도 넣을 수 없기 때문이다.

**해결책**

```java
public static void swap(List<?> list, int i, int j) {
    swapHelper(list, i, j);
}

// 와일드카드 타입을 실제 타입으로 바꿔주는 private 도우미 메서드
private static <E> void swapHelper(List<E> list, int i, int j){
    list.set(i, list.set(j, list.get(i)));
};
```

swapHelper 메서드는 리스트가 List<E>임을 알고 있으므로 리스트에서 꺼낸 값의 타입이 항상 E이고, E 타입 값이라면 이 리스트에 넣어도 안전함을 알고 있다.

메서드가 하나 더 생겼지만, 덕분에 외부에서는 와일드카드 기반의 선언을 유지할 수 있다.

### 정리

조금 복잡하더라도 와일드카드 타입을 적용하면 API가 훨씬 더 유연해진다.

널리 쓰이는 라이브러리를 작성한다면 반드시 와일드카드 타입을 적절히 사용해주자

PECS 공식을 기억하자! 생산자는 extends, 소비자는 super를 사용하자