# item 32. 제네릭과 가변인수를 함께 쓸 때는 신중하라

<aside>
💡 가변인수와 제네릭을 같이 사용했을 때의 문제점과 사용법을 알아보자

</aside>

### 가변인수와 제네릭

가변 인수는 자바 5에 추가되었고 메서드에 넘기는 인수의 개수를 클라이언트가 조절할 수 있게해주는 장치이다.

```java
public static void main(String[] args) {
    varargs("이렇게 ", "여러개의 ", "매개변수를 ", "넘길 수 있습니다");
}

static void varargs(String... string){
    for (String s : string) {
        System.out.print(s);
    }
}
```

**가변인수의 동작 방식으로 인한 제네릭 혼용 시 생기는 문제점**

가변인수 메서드를 호출하면 가변인수를 담기위한 배열이 자동으로 하나 만들어진다.

내부로 감춰야 했을 이 배열을 클라이언트에 노출하는 문제가 생겼다.

그 결과 varargs 매개변수에 제네릭이나 매개변수화 타입이 포함되면 알기 어려운 컴파일 경고가 발생한다.

![Untitled](https://user-images.githubusercontent.com/49682056/226115505-2c522d77-0291-4c0b-8e58-2609d9b3dd6c.png)

매개변수화 타입의 변수가 타입이 다른 객체를 잠조하면 힙오염이 발생할 수 있다는 경고이다.

다른 타입 객체를 참조하는 상황에서는 컴파일러가 자동생성한 형변환이 실패할 수 있으니, 제네릭 타입 시스템이 약속한 타입 안전성의 근간이 흔들려버린다.

```java
// 코드 32-1 제네릭과 varargs를 혼용하면 타입 안전성이 깨진다!
static void dangerous(List<String>... stringLists) {
    List<Integer> intList = List.of(42);
    Object[] objects = stringLists;
    objects[0] = intList; // 힙오염 발생
    String s = stringLists[0].get(0); // ClassCastException
}
```

마지막 줄에서 보이지 않는 형변환이 숨어 있어 ClassCastException이 발생한다.

**⇒ 제네릭 varargs 배열 매개변수에 값을 저장하는 것은 안전하지 않다.**

사용자는 위 메서드를 사용할 때 경고가 떠 `@SuppressWarnings("unchecked")` 메서드를 달아 경고를 없앴지만 사용자 입장에서는 사용하기 꺼려진다.

### 그래도 제네릭 varargs 매개변수를 선언할 수 있게 한 이유

제네릭 배열을 프로그래머가 직접 생성하는 건 허용하지 않으면서 제네릭 varargs 매개변수를 받는 메서드를 선언할 수 있게 한 이유가 뭘까?

답은 단순하다. 실무에서 아주 유용하기 때문이다. 

`Arrays.asList(T …)`, `Collections.addAll(Collection<? super T> c, T… elements)` , `EnumSet.of(E first, E… Rest)` 가 대표적이다.

다행이도 위 메서드들은  dangerous 메서드와 달리 타입 안전하다.

### `@SafeVarargs` 애너테이션

`**@SafeVarargs` 애너테이션은 메서드 작성자가 그 메서드가 타입 안전함을 보장하는 장치로 클라이언트 측에서 발생하는 경고를 숨길 수 있게 해준다.**

메서드가 안전한 게 확실하지 않다면 절대 `@SafeVarargs` 애너테이션을 달아선 안된다.

**어떻게 하면 메서드가 안전한지 확신 할 수 있을까?**

가변인수 메서드를 호출 할 때 varargs 매개변수를 담는 제네릭 배열이 만들어지는데 이 배열에 

**(1) 아무것도 저장하지 않고(그 매개변수를 덮어쓰지 않고)** 

**(2) 그 배열의 참조가 밖으로 노출되지 않는다면(신뢰할 수 없는 코드가 배열에 접근할 수 없다면)**

타입 안전하다고 말할 수 있다.

(1) 아무것도 저장하지 않고(그 매개변수를 덮어쓰지 않고)도 타입 안전성을 깰 수 있다.

```java
// 코드 32-2 자신의 제네릭 매개변수 배열의 참조를 노출한다. - 안전하지 않다!
static <T> T[] toArray(T... args) {
    return args;
}
```

이 메서드가 반환하는 배열의 타입은 이 메서드에 인수를 넘기는 컴파일 타임에 결정되는데, 그 시점에는 컴파일러에게 충분한 정보가 주어지지 않아 타입을 잘못 판단할 수 있다.

따라서 자신의 varargs 매개변수 배열을 그대로 반환하면 힙 오염을 이 메서드를 호출한 쪽의 콜 스택으로까지 전이하는 결과를 낳을 수 있다.

```java
// T타입 인수 3개를 받아 그중 2개를 무작위로 골라 담은 배열을 반환하는 메서드
static <T> T[] pickTwo(T a, T b, T c) {
    switch (ThreadLocalRandom.current().nextInt(3)) {
        case 0: return toArray(a, b);
        case 1: return toArray(a, c);
        case 2: return toArray(b, c);
    }
    throw new AssertionError();
}

String[] attributes = pickTwo("좋은", "빠른", "저렴한");
```

위 pickTwo 메서드를 사용하는 사용자 입장에서는 위험하지도 않고 경고도 내지 않는다.

이 메서드를 본 컴파일러는 toArray에 넘길 T 인스턴스 2개를 담을 varargs 매개변수 배열을 만드는 코드를 생성한다.

이 코드가 만드는 배열의 타입은 Object[]인데, Objcet가 pickTwo에 어떤 타입의 객체를 넘기더라도 담을 수 있는 가장 구체적인 타입이기 때문이다.

그리고 toArray 메서드가 돌려준 이 배열이 그대로 pickTwo를 호출한 클라이언트까지 전달된다. 즉, 항상 Object[] 타입 배열을 반환한다.

```java
// Exception in thread "main" java.lang.ClassCastException: class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String; ([Ljava.lang.Object; and [Ljava.lang.String; are in module java.base of loader 'bootstrap') at org.example.item32.item32.main(item32.java:14)
String[] attributes = pickTwo("좋은", "빠른", "저렴한");
```

위 코드는 정상적으로 컴파일되지만 실행하면 ClassCastException이 발생한다. 

pickTwo 메서드의 반환값을 attributes에 저장하기 위해 Object[]을 String[]으로 형변환하는 코드를 컴파일러가 자동 생성하기 때문이다.

다른 아이템에서도 말했듯 Object[]는 String[]의 하위 타입이 아니므로 형변환에 실패한다.

즉, 위 예제는 제네릭 varargs 매개변수 배열에 다른 메서드가 접근하도록 허용하면 안전하지 않다라는 점이다.

⇒ **(2) 그 배열의 참조가 밖으로 노출되지 않는다면(신뢰할 수 없는 코드가 배열에 접근할 수 없다면) 규칙 위배**

단 참조를 노출하더라고 안전한 예외가 두가지 있다.

1.  `@SafeVarargs` 가 붙은 또 다른 varargs 메서드에 넘기는 경우
2. 이 배열 내용의 일부 함수를 호출만 하는 일반 메서드에 넘기는 경우 

### 제네릭 varargs 매개변수를 안전하게 사용하는 메서드

```java
// 코드 32-3 제네릭 varargs 매개변수를 안전하게 사용하는 메서드
@SafeVarargs
static <T> List<T> flatten(List<? extends T>... lists){
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists) {
        result.addAll(list);
    }
    return result;
}

// 사용
List<String> flatten1 = flatten(strings1, strings2);
```

제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 모든 메서드에 `@SafeVarargs` 를 달자.

대신 위에서 말했던 규칙을 반드시 지켰을 때이다.

- varargs 매개변수 배열에 아무것도 저장하지 않는다.
- 그 배열(혹은 복제본)을 신뢰할 수 없는 코드에 노출하지 않는다.

<aside>
📍 @SafeVarargs 애너테이션은 재정의할 수 없는 메서드에만 달아야 한다.
재정의한 메서드도 안전할지는 보장할 수 없기 때문이다.
자바 8에서 이 애너테이션은 오직 정적 메서드와 final 인스턴스 메서드에만 붙일 수 있고, 자바 9부터는 private 인스턴스 메서드에도 허용된다.

</aside>

### `@SafeVarargs` 만이 정답은 아니다

varargs 매개변수를 List 매개변수로 바꿀 수도 있다.

```java
// 코드 32-4 제네릭 varargs 매개변수를 List로 대체한 예 - 타입 안전하다
static <T> List<T> flatten(List<List<? extends T>> lists){
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists) {
        result.addAll(list);
    }
    return result;
}

// 사용
List<String> flatten2 = flatten(List.of(strings1, strings2));
```

이 방식의 장점은 컴파일러가 이 메서드의 타입 안전성을 검증할 수 있고 `@SafeVarargs` 도 안달아도 된다.

```java
// 코드 32-5 제네릭 varargs 매개변수를 List로 대체한 예 - 타입 안전하다
static <T> List<T> pickTwoV2(T a, T b, T c) {
    switch (ThreadLocalRandom.current().nextInt(3)) {
        case 0: return List.of(a, b);
        case 1: return List.of(a, c);
        case 2: return List.of(b, c);
    }
    throw new AssertionError();
}

List<String> strings = pickTwoV2("좋은", "빠른", "저렴한");
```

배열 없이 제네릭만 사용하므로 타입 안전하다.

### 정리

가변인수와 제네릭은 궁합이 좋지 않다.

가변인수 기능은 배열을 노출하여 추상화가 완벽하지 못하고, 배열과 제네릭의 타입 규칙이 서로 다르기 때문이다.

제네릭 varargs 매개변수는 타입 안전하지 않지만, 허용된다.

메서드에 제네릭 (혹은 매개변수화된) varargs 매개변수를 사용하고자 한다면, 먼저 그 메서드가 타입 안전한지 확인한 다음 `@SafeVarargs` 애너테이션을 달아 사용하는 데 불편함이 없게끔 하자!

타입 안전한 지 확인하는 방법

- varargs 매개변수 배열에 아무것도 저장하지 않는다.
- 그 배열(혹은 복제본)을 신뢰할 수 없는 코드에 노출하지 않는다.