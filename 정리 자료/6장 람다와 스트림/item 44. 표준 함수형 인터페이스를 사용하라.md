# item 44. 표준 함수형 인터페이스를 사용하라

<aside>
💡 표준 함수형 인터페이스의 종류를 알아보고 사용법을 알아보자

</aside>

### 직접 구현 대신 표준 함수형 인테페이스를 사용하라

`LinkedHashMap`의 protected 메서드인 removeEldestEntry 메서드를 재정의하면 캐시로 사용할 수 있다.

```java
public class CacheLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > 100;
    }
}

public static void main(String[] args) {
    CacheLinkedHashMap<Integer, Integer> map = new CacheLinkedHashMap<>();
    for (int i = 0; i < 200; i++) {
        map.put(i, i);
    }
    System.out.println(map); // {100=100, 101=101, 102=102, 103=103, ..., 199=199}

    LinkedHashMap<Integer, Integer> map2 = new LinkedHashMap<>();
    for (int i = 0; i < 200; i++) {
        map2.put(i, i);
    }
    System.out.println(map2); // {0=0, 1=1, 2=2, 3=3, 4=4, 5=5, 6=6, 7=7, 8=8, 9=9, 10=10, 11=11, ... 199=199}
}
```

하지만 이를 람다를 사용하면 훨씬 더 잘 해낼 수 있다. `LinkedHashMap`을 오늘날 다시 구현한다면 함수 객체를 받는 정적 팩터리나 생성자를 제공했을 것이다.

removeEldestEntry 선언을 보면 `Map.Entry<K,V>`을 받아 boolean을 반환해야 할 것 같지만, 파라미터를 받은 `Map.Entry<K,V>`를 사용하지 않고 size()만을 호출하여 원소의 수를 알아낸다.

이는 removeEldestEntry가 인스턴스 메서드이기 때문에 가능한 방식이다.

하지만 생성자에 넘기는 함수 객체는 이 맵의 인스턴스 메서드가 아니다.

팩터리나 생성자를 호출할 때는 맵의 인스턴스가 존재하지 않기 때문이다. 따라서 맵은 자기 자신도 함수객체에 건네줘야 한다. 이를 반영한 인터페이스는 다음처럼 선언할 수 있다.

```java
// 코드 44-1 불필요한 함수형 인터페이스 - 대신 표준 함수형 인터페이스를 사용하라
@FunctionalInterface interface EldestEntryRemovalFunction<K, V> {
    boolean remove(Map<K,V> map, Map.Entry<K,V> eldest);
}
```

이 인터페이스도 잘 동작하긴 하지만, 굳이 사용할 이유는 없다.

java.util.fuction 패키지를 보면 다양한 용도의 표준 함수형 인터페이스가 있다.

**필요한 용도에 맞는게 있다면, 직접 구현하지 말고 표준 함수형 인터페이스를 활용하라**

### java.util.function 패키지

표준 함수형 인터페이스들은 유용한 디폴트 메서드를 많이 제공하므로 다른 코드와의 상호운용성도 크게 좋아진다.

예를 들어 Predicate 인터페이스는 프레디키트(predicate)들을 조합하는 메서드를 제공한다.

앞의 LinkedHashMap 예에서 직접 만든 EldestEntryRemovalFunction 대신 표준 인터페이스인 `BiPredicate<Map<K,V>, Map.Entry<K,V>>`를 사용 할 수 있다.

java.util.function 패키지에는 총 43개의 인터페이스가 담겨 있다.

![Untitled](https://user-images.githubusercontent.com/49682056/231444500-5b90fca6-a87b-45b2-9fa4-d6835c7eea18.png)

기본 인터페이스는 6개로 모두 참조 타입용이다.

| 인터페이스 | 함수 시그니처 | 예 | 설명 |
| --- | --- | --- | --- |
| UnaryOperator | T apply(T t) | String::toLowerCase | 입력과 리턴 타입이 동일 |
| BinaryOperator | T apply(T t1, T t2) | BigInteger::add | 입력(2개)과 리턴 타입이 동일 |
| Predicate | boolean test(T t) | Collection::isEmpty | 값을 전달받아 true/false를 반환 |
| Function<T, R> | R apply(T t) | Arrays::asList | 값을 다른 값으로 변환해 반환 |
| Supplier<T> | T get() | Instant::now | 입력값 없이 리턴값만 존재 |
| Consumer<T> | void accept(T t) | System.out::println | 값을 받아서 처리만하고 결과 리턴을 하지 않음 |

기본 인터페이스는 기본 타입인 int, long, double용으로 각 3개씩 변형이 생겨난다.

그 이름도 기본 인터페이스의 이름 앞에 해당 기본 타입을 붙여 지었다

ex) 

int를 받는 Predicate는 `IntPredicate`

long을 받아 long을 반환하는 Predicate는 `LongBinaryOperator`

### Operator 인터페이스

**반환값과 인수의 타입이 같은 함수를 뜻한다.**

`Operator` 인터페이스는 인수가 1개인 `UnaryOperator`와 2개인 `BinaryOperator`로 나뉜다.


![Untitled 1](https://user-images.githubusercontent.com/49682056/231444484-5212eb4f-97f8-42c3-a431-f9a0ebc2805e.png)

![Untitled 2](https://user-images.githubusercontent.com/49682056/231444489-8a9ba4f6-477d-4366-9073-66ff5f454077.png)

`UnaryOperator`는 `Function`을 상속받고 `BinaryOperator`는 `BiFunction`을 상속 받는다.

| interface | 인터페이스 시그니처 or 함수형 메서드 시그니처 | 상속 |
| --- | --- | --- |
| UnaryOperator | T apply(T t) | Function<T,T> |
| BinaryOperator | T apply(T t, U u) | BiFunction<T,T,T> |
| DoubleBinaryOperator | double applyAsDouble(double left, double right) |  |
| DoubleUnaryOperator | double applyAsDouble(double operand) |  |
| IntBinaryOperator | int applyAsInt(int left, int right) |  |
| IntUnaryOperator | int applyAsInt(int operand) |  |
| LongBinaryOperator | long applyAsLong(long left, long right) |  |
| LongUnaryOperator | long applyAsLong(long operand) |  |

```java
UnaryOperator<String> rowerCaseString = String::toLowerCase;

String str = "IDENTITY";
String apply = rowerCaseString.apply(str);
System.out.println(apply); // identity

BinaryOperator<BigInteger> binaryOperatorAdd = BigInteger::add;
BinaryOperator<Integer> binaryOperatorSum = Integer::sum;
BigInteger bigInteger = BigInteger.TEN; // 10
BigInteger binaryOperatorAddApply = binaryOperatorAdd.apply(bigInteger, bigInteger);
System.out.println(binaryOperatorAddApply); // 20
```

### Predicate 인터페이스

인수 하나를 받아 boolean을 반환하는 함수를 뜻한다.

![Untitled 3](https://user-images.githubusercontent.com/49682056/231444491-d807f1dd-8560-41f3-9edf-8ba482dffe32.png)
인수를 두개를 받는 변형인 `BiPredicate<T, U>`가 있다.

| interface | 함수형 메서드 시그니처 |
| --- | --- |
| BiPredicate | boolean test(T t, U u) |
| DoublePredicate | boolean test(double value) |
| IntPredicate | boolean test(int value) |
| LongPredicate | boolean test(long value) |

```java
Predicate<Collection<Object>> predicate = Collection::isEmpty;
```

### Supplier 인터페이스

인수를 받지 않고 값을 반환(혹은 제공)하는 함수를 뜻한다.

![Untitled 4](https://user-images.githubusercontent.com/49682056/231444493-44419e7f-493c-456b-bbc3-8e0f4dffed04.png)

`BooleanSupplier`인터페이스는 boolean을 반환하도록 한 Supplier의 변형이다.

| interface | 함수형 메서드 시그니처 |
| --- | --- |
| BooleanSupplier | boolean getAsBoolean() |
| DoubleSupplier | double getAsDouble() |
| IntSupplier | int getAsInt() |
| LongSupplier | long getAsLong() |

```java
Supplier<Instant> supplier = Instant::now;
```

### Consumer 인터페이스

인수를 하나 받고 반환값은 없는(특히 인수를 소비하는) 함수를 뜻한다.

![Untitled 5](https://user-images.githubusercontent.com/49682056/231444497-0516f0d7-60d1-46c1-87c1-58267ee956a8.png)
인수를 두개 받는 변형이 BiConsumer<T,U>가 있다.

객체 참조와 기본 타입 하나, 즉 인수를 두개 받는 변형인 `ObjDoubleConsumer<T>`, `ObjIntConsumer<T>`, `ObjLongConsume<T>`가 존재한다.

| interface | 함수형 메서드 시그니처 |
| --- | --- |
| BiConsumer | void accept(T t, U u) |
| DoubleConsumer | void accept(double value) |
| IntConsumer | void accept(int value) |
| LongConsumer | void accept(int long) |
| ObjDoubleConsumer | void accept(T t, double value) |
| ObjIntConsumer | void accept(T t, int value) |
| ObjLongConsumer | void accept(T t, long value) |

```java
Consumer<String> consumer = System.out::print;
```

### Function 인터페이스

인수와 반환 타입이 다른 함수를 뜻한다.(입력과 결과 타입이 항상 다르다)

![Untitled 6](https://user-images.githubusercontent.com/49682056/231444499-e838777d-61e0-42e3-b2dd-eab7994741e8.png)

유일하게 Function만 반환 타입만 매개변수화 되었다.

`LongFunction<int[]>`은 long인수를 받아 int[]를 반환한다.

기본 타입을 반환하는 변형이 총 **9개**가 더있다.

입력과 결과 타입이 모두 기본타입이면 접두어로 SrcToResult를 사용한다.(**총 6개**)

예를 들어 long을 받아 int를 반환하면 `LongToIntFunction`이 되는 식이다.(Long(Src)ToInt(Result)Function)

나머지는 입력이 객체 참조이고 결과가 int, long, double인 변형들로, 앞서와 달리 입력을 매개변수화하고 접두어로 ToResult를 사용한다.(**총 3개**) 

예를 들어 `ToLongFunction<int[]>`은 int[]인수를 받아 long을 반환한다.(ToLong(Result))

인수를 두개를 받는 변형인 BiFunction<T,U,R>이 있다.

`BiFunction`에는 다시 기본타입을 반환하는 세개의 변형 `ToIntBiFuction.<T, U>`, `ToLongBiFunction<T,U>`, `ToDoubleBiFunction<T,U>`가 있다.

| interface | 함수형 메서드 시그니처 |
| --- | --- |
| BiFunction | R apply(T t, U u) |
| DoubleFunction | R apply(double value) |
| DoubleToIntFunction | int applyAsInt(double value) |
| DoubleToLongFunction | long applyAsLong(double value) |
| IntFunction | R apply(int value) |
| IntToDoubleFunction | double applyAsDouble(int value) |
| IntToLongFunction | long applyAsLong(int value) |
| LongFunction | R apply(long value) |
| LongToDoubleFunction | double applyAsDouble(long value) |
| ToDoubleBiFunction | double applyAsDouble(T t, U u) |
| ToDoubleFunction | double applyAsDouble(T value) |
| ToIntBiFunction | int applyAsInt(T t, U u) |
| ToIntFunction | int applyAsInt(T value) |
| ToLongBiFunction | long applyAsLong(T t, U u) |
| ToLongFunction | long applyAsLong(T value) |

```java
Function<Integer,String> function = (i)-> Integer.toString(i);
```

### 표준 함수형 인터페이스 대부분은 기본 타입만 지원한다.

그렇다고 기본 함수형 인터페이스에 박싱된 기본 타입을 넣어 사용하지는 말자.

동작은 하지만 계산량이 많은 경우 성능이 처참히 느려질 수 있다.

### 표준 함수형 인터페이스 대신 직접 구현한 함수형 인터페이스를 작성해야 할 때도 있다.

자주 보아온 `Comparator<T>` 인터페이스가 대표적인 예이다.

구조적으로 `ToIntBiFunction<T, U>`와 동일하다. 

하지만 표준 함수형 인터페이스를 사용하지 않고 독자적인 인터페이스로 만든 이유가 있다.

1. API에서 굉장히 자주 사용되는데, 지금의 이름이 그 용도를 아주 훌륭히 설명해준다.
2. 구현하는 쪽에서 반드시 지켜야 할 규약을 담고있다.
3. 비교자들을 변환하고 조합해주는 유용한 디폴트 메서드를 듬뿍 담고있다.

위 특성을 정리하면 다음 세가지인데,  이 중 하나 이상을 만족하면 전용 함수형 인터페이스를 구현해야 하는 건 아닌지 고민해보자

- 자주 쓰이며, 이름 자체가 용도를 명확히 설명해준다.
- 반드시 따라야하는 규약이 있다.
- 유용한 디폴트 메서드를 제공할 수 있다.

반드시 직접 구현한 함수형 인터페이스인 경우 `@FunctionalInterface` 애너테이션을 붙여주자.

이는 다음과 같은 목적이 있다.

1. 해당 클래스의 코드나 설명 문서를 읽을 이에게 그 인터페이스가 람다용으로 설계된 것임을 알려준다.
2. 해당 인터페이스가 추상 메서드 오직 하나만 가지고 있어야 컴파일 되게 해준다.
3. 유지보수 과정에서 누군가 실수로 메서드를 추가하지 못하게 막아준다.

### 함수형 인터페이스를 API에서 사용할 때 주의할 점

서로 다른 함수형 인터페이스를 같은 위치의 인수로 받는 메서드들을 다중정의 해서는 안된다.

⇒ 클라이언트에게 불필요한 모호함만 안겨줄 수 있다.

ex) ExecuterService의 submit메서드는 `Callable<T>`을 받는 것과 `Runnable<T>`을 받는 것을 다중정의 했다. ⇒ 올바른 메서드를 알려주기 위해 형변환을 해야 한다.

### 정리

입력값과 반환값에 함수형 인터페이스 타입을 활용하라.

보통은 java.util.function 패키지의 표준 함수형 인터페이스를 사용하는 것이 가장 좋은 선택이다.

단, 위에서 말한 조건에 해당하는 상황이라면 직접 작성한 함수형 인터페이스를 만들어 쓰는 편이 나을 수 있음을 잊지말자