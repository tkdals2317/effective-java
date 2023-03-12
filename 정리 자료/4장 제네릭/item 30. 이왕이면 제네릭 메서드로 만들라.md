# item 30. 이왕이면 제네릭 메서드로 만들라

<aside>
💡 클래스 뿐만아니라 메서드도 제네릭 메서드로 만들 수 있다! 제네릭 메서드를 만드는 방법을 알아보자

</aside>

### 개요

클래스와 마찬가지로, 메서드도 제네릭으로 만들 수 있다.

주로 매개변수화 타입을 받는 정적 유틸리티 메서드들이 제네릭 메서드이다. ex) Collections의 binarySearch, sort 메서드

### 로타입으로 작성된 두 집합의 합집합을 반환하는 메서드

```java
public static Set union(Set s1, Set s2) {
    Set result = new HashSet(s1);
    result.addAll(s2);
    return result;
}
```

로타입으로 만들어본 Union 메서드이다.

컴파일은 되지만 unchecked call 경고가 발생한다.

![Untitled](https://user-images.githubusercontent.com/49682056/224526876-30567bb9-cdb8-4070-81f7-84b02a1115ed.png)

![Untitled 1](https://user-images.githubusercontent.com/49682056/224526871-c1b0543a-238a-4806-b150-cd7e49b364ec.png)

### 제네릭으로 바꾼 합집합 메서드

메서드 선언에서 세 집합(입력 2개, 반환 1개)의 원소 타입을 타입 매개변수로 명시하고, 메서드 안에서도 이 타입 매개변수만 사용하게 수정하자

```java
public static <E> Set<E> Union(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}
```

위 제네릭 메서드는 경고 없이 컴파일 되며, 타입 안전하고, 쓰기도 쉽다. 사용자는 직접 형변환을 해줄 필요도 없다.

```java
public static void main(String[] args) {
    Set<String> backend = Set.of("한용구", "이상민", "엄영범", "심의진", "이한솔");
    Set<String> front = Set.of("윤동우", "독고현", "유병재", "박태준");
    Set<String> jobdaAtsDevelop1Cell = union(backend, front);
    System.out.println(jobdaAtsDevelop1Cell);
}
```

하지만 위 메서드는 집합 3개(입력 2개, 반환 1개)의 타입이 모두 같아야 한다는 단점이 있는데, 이는 한정적 와일드 카드 타입을 사용하여 유연하게 개선할 수 있다 ⇒ item 31에서 소개한다.

### 제네릭 싱글턴 팩터리 패턴

불변 객체를 여러 타입으로 활용할 수 있게 만들어야 할 때가 있다.

**제네릭은 런타임에 타입 정보가 소거되므로 하나의 객체를 어떤 타입으로든 매개변수화 할 수 있다.**

하지만 이렇게 하려면 요청한 타입매개변수에 맞게 매번 그 객체의 타입을 바꿔주는 정적 팩터리를 만들어야 한다.

이런 패턴을 **제네릭 싱글턴 팩터리**라 한다.

![Untitled 2](https://user-images.githubusercontent.com/49682056/224526873-ca2eebc7-9dc5-408c-af29-614296e61b76.png)

![Untitled 3](https://user-images.githubusercontent.com/49682056/224526874-1a74db8f-55d6-4b51-b6e7-ecfcc0bc88d6.png)

```java
private static UnaryOperator<Object> IDENTITY_FN = (t) -> t;

@SuppressWarnings("unchecked")
public static <T> UnaryOperator<T> identityFunction() {
    return (UnaryOperator<T>) IDENTITY_FN;
}
```

제네릭 싱글턴 팩터리 패턴으로 작성한 항등 함수이다.

자바의 제네릭이 실체화 된다면 항등함수를 타입별로 만들어야 하지만, 런타임에 타입이 소거되는 제네릭의 특성을 살리면 위 메서드 하나로 충분하다.

```java
public static void main(String[] args) {
    String[] strings = {"쌈베", "대마", "나일론"};
    UnaryOperator<String> sameString = identityFunction();
    for (String s : strings) {
        System.out.println(sameString.apply(s));
    }

    Number[] numbers = {1, 2.0, 3L};
    UnaryOperator<Number> sameNumber = identityFunction();
    for (Number n : numbers) {
        System.out.println(sameNumber.apply(n));
    }
}
```

### 재귀적 타입 한정(recursive type bound)

상대적으로 드물긴, 자기 자신이 들어간 표현식을 사용하여 타입 매개변수의 허용범위를 한정 할 수 있는데 이를 **재귀적 타입 한정**이라고 한다.

재귀적 타입 한정은 주로 자연적 순서를 정하는 `Comparable` 인터페이스와 함께 쓰인다.

```java
public interface Comparable<T> {
		int compareTo(T o)
}
```

여기서 **타입 매개변수** **T**는 **Comparable<T>를 구현한 타입이 비교할 수 있는 원소의 타입**을 정의한다.

거의 모든 타입은 자신과 같은 타입의 원소만 비교할 수 있다.

String ⇒ Comparable<String>

Integer ⇒ Comparable<Integer>

Comparable을 구현한 원소의 컬렉션을 입력 받는 메서드들은 주로 그 원소를 정렬 혹은 검색하거나, 최솟값, 최대값을 구하는 식으로 사용된다.

이 기능을 수행하려면 컬렉션에 담긴 모든 원소가 상호 비교될 수 있어야한다.

다음은 이 제약을 코드로 표현한 모습이다.

```java
// 코드 30-6 재귀적 타입 한정을 이용해 상호 비교할 수 있음을 표현했다.
public static <E extends Comparable<E>> E max(Collection<E> c);
```

타입 한정인 `<E extends Comparable<E>>` 는 “모든 타입 E는 자신과 비교할 수 있다”라고 읽을 수 있다.

```java
// 코드 30-7 구현 컬렉션에서 최대값을 반환한다. - 재귀적 타입 한정 사용
public static <E extends Comparable<E>> E max(Collection<E> c) {
    if (c.isEmpty()) {
        throw new IllegalArgumentException("컬렉션이 비어 있습니다");
    }
    
    E result = null;
    
    for (E e :c) {
        if (result == null || e.compareTo(result) > 0) {
            result = Objects.requireNonNull(e);
        }
    }
    return result;
}
// 빈 리스트일 시 Optional 반환 
public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
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

### 정리

제네릭 타입과 마찬가지로, 클라이언트에서 입력 매개변수와 반환값을 명시적으로 형변환해야하는 메서드보다 제네릭 메서드가 안전하고 사용하기 쉽다.

제네릭 메서드도 형변환 없이 사용할 수 있게 만들어주자