# item 46. 스트림에서는 부작용 없는 함수를 사용하라

<aside>
💡 스트림을 제대로 사용하는 방법과 스트림 API의 핵심인 수집기에 대해 알아보자

</aside>

### 스트림 패러다임의 핵심

스트림 패러다임의 핵심은 계산을 일련의 변환(transfomation)으로 재구성하는 부분이다.

이때 각 변환 단계는 가능한 이전 단계의 결과를 받아 처리하는 **순수 함수**여야한다.

순수 함수란 오직 입력만이 결과에 영향을 주는 함수를 말한다. 다른 가변 상태를 참조하지 않고, 함수 스스로도 다른 상태를 변경하지 않는다.

이렇게 하려면 스트림 연산에 건네는 함수 객체는 모든 부작용이 없어야 한다.

### 잘못 사용한 Stream API 활용 예제

```java
// 코드 46-1 스트림 패러다임을 이해하지 못한 채 API만 사용했다 따라 하지 말 것!
Map<String, Long> freq = new HashMap<>();
try (Stream<String> words = new Scanner(file).tokens()) {
    words.forEach(word -> {
        freq.merge(word.toLowerCase(), 1L, Long::sum);
    });
}
```

위 코드는 스트림 코드를 가장한 반복적 코드이다.

스트림 API의 이점을 살리지 못하여 같은 기능의 반복적 코드보다 길고, 읽기 어렵고, 유지보수에도 좋지 않다.

위 코드는 모든 연산이 종단 연산인 forEach에서 일어나는데, 이때 외부 상태(빈도표)를 수정하는 람다를 실행시키면서 문제가 생긴다.

forEach가 그저 스트림이  수행한 연산 결과를 보여주는 일 이상을 하는 것(람다가 상태를 수정함)은 나쁜 코드이다.

forEach연산은 자바의 for-each 반복문과 비슷하게 생겼는데, forEach연산은 종단 연산 기능 중 가장 적고 가장 ‘덜’ 스트림답다.

**forEach 연산은 스트림 계산 결과를 보고할 때만 사용하고, 계산하는 데는 쓰지말자**

### 올바르게 작성한 Stream API 활용 예제

```java
// 코드 46-2 스트림을 제대로 활용해 빈도표를 초기화한다.
Map<String, Long> freq;
try (Stream<String> words = new Scanner(file).tokens()) {
    freq = words.collect(groupingBy(String::toLowerCase, counting()));
}
```

앞서와 같은 일을 하지만, 스트림 API를 제대로 활용했다. 그 뿐만아니라 간결하고 명확하다.

### 수집기(Collector)

스트림을 사용하려면 꼭 배워야하는 개념이 바로 수집기(collector)이다.

`java.util.stream.Collectors` 클래스에 대해 알아보자.

책에서는 Collector 인터페이스를 잠시 잊고, 축소 전략을 캡슐화한 블랙박스 객체라고 생각하라고 하는데,

여기서 축소(reduction)는 원소들을 객체 하나에 취한한다는 뜻이다.

수집기가 생성하는 객체는 일반적으로 컬렉션이며, 그래서 “**collector**”라는 이름을 쓴다.

수집기를 사용하면 스트림의 원소를 손쉽게 컬렉션으로 모을 수 있다.

수집기는 총 세가지로, 리스트를 반환하는 `toList()`, 집합을 반환하는 `toSet()`, 프로그래머가 지정한 컬렉션 타입을 반환하는 `toCollection(collectionFactory)`가 있다.

**빈도표에서 가장 흔한 단어 10개를 뽑아내는 스트림 파이프라인 예제**

```java
// 코드 46-3 빈도표에서 가장 흔한 단어 10개를 뽑아내는 파이프라인
List<String> topTen = freq.keySet().stream()
        .sorted(comparing(freq::get).reversed()) // comparing() : 키 추출 함수를 받는 비교자 생성 메서드
        .limit(10)
        .collect(toList()); // 리스트로 반환한다.
```

### Collectors의 메서드

위에서 언급한 세개의 수집기 외에 나머지 36개의 메서드들이 있는데 간략하게 알아보자.

스트림의 각 원소는 키 하나와 값 하나에 연관되어 있다(Map<K, V>). 그리고 다수의 스트림 원소가 같은 키에 연관될 수 있다.(Map<Key, List<V>>)

**toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper)**

가장 간단한 맵 수집기로, 스트림 원소를 키에 매핑하는 함수와 값에 매핑하는 함수를 인수로 받는다.

```java
// 코드 46-4 toMap 수집기를 이용하여 열거 타입 상수에 매핑한다.
public static final Map<String, Operation> stringToEnum =
        Stream.of(Operation.values()).collect(
                Collectors.toMap(Object::toString, e -> e));
```

이 수집기는 스트림의 각 원소가 고유한 키에 매핑되어 있을 때 적합하다.

스트림 원소 다수가 같은 키를 사용한다면 파이프라인이 `IllegalStateException`을 던지며 종료된다.

**toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction)**

키 매핑 함수, 값 매핑 함수, 병합 함수 총 3개의 인수로 받는 수집기이다.

여기서 병합 함수의 형태는 `BinaryOperator<U>` 이며 U는 해당 맵의 값 타입이다.

같은 키를 공유하는 값들은 이 병합 함수를 통해 기존 값에 합쳐진다.

```java
List<Album> albums = new ArrayList<>();
albums.add(new Album("1번째 앨범", "이상민", 10));
albums.add(new Album("2번째 앨범", "이상민", 20));
albums.add(new Album("3번째 앨범(명반)", "이상민", 500));
albums.add(new Album("4번째 앨범(망작)", "이상민", 2));
albums.add(new Album("에넥도트", "이센스", 500));
albums.add(new Album("에넥도트 2", "이센스", 2));

// 코드 46-5 각 키와 해당 키의 특정 원소를 연관 짓는 맵을 생성하는 수집기
Map<String, Album> topHits= albums.stream().collect(
        // Collectors.maxBy 는 Comparator<T> 를 받아 BinaryOperator<T> 를 돌려준다
        toMap(Album::getArtist, album -> album, maxBy(comparing(Album::getSales))));// Collectors의 maxBy와 BinaryOperator의 maxBy가 이름이 겹쳐서 혼돈하기 쉽다. 주의하자

==== output ====
{이센스=에넥도트, 이상민=3번째 앨범(명반)}
```

인수 3개를 받는 toMap은 어떤 키와 그 키에 연관된 원소들 중 하나를 골라 연관 짓는 맵을 만들 때 유용하다.

여기서 비교자로 BinaryOperator에서 정적 임포트한 maxBy라는 정적 팩터리 메서드를 사용했다.

maxBy는 Comparator<T>를 입력받아 BinaryOperator<T>를 돌려준다.

이 경우 비교자 생성 메서드인 comparing이 maxBy에 넘겨줄 비교자를 반환하는 데, 자신의 키 추출 함수로는 Album::getSales를 받았다.

앨범 스트림을 맵으로 바꾸는데, 이 맵은 각 음악가와 그 음악가의 베스트 앨범을 짝지은 것이다.

또한 인수가 3개인 toMap은 충돌이 나면 마지막 값을 취하는(last-write-win) 수집기를 만들 때도 유용하다.

```java
// 코드 46-7 마지막에 쓴 값을 취하는 수집기
Map<String, Album> lastAlbum = albums.stream().collect(
                toMap(Album::getArtist, album -> album, (oldVal, newVal) -> newVal));
```

**toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction,  Supplier<M> mapFactory)**

마지막으로 네번 째 인수로 맵 팩터리를 받는 toMap으로 해당 맵 팩터리 인수로 EnumMap이나 TreeMap처러 원하는 특정 맵 구현체를 직접 지정할 수 있다.

**그 외 toMap의 변종** 

위 세 `toMap` 메서드의 변종인 `toConcurrentMap`은 병렬 실행된 후 결과로 `ConcurrentHashMap` 인스턴스를 생성한다.

**groupingBy(Function<? super T, ? extends K> classifier)**

또 다른 Collecotors의 메서드인 groupingBy를 알아보자

이 메서드는 입력으로 분류 함수(classifier)를 받고 출력으로는 원소들을 카테고리별로 모아 놓은 맵을 담은 수집기를 반환한다.

 

분류 함수는 입력받은 원소가 속하는 카테고리를 반환한다.

그리고 이 카테고리가 해당 원소의 맵 키로 사용된다.

다중정의된 groupingBy 중 형태가 가장 간단한 것은 분류 함수 하나를 인수로 받아 맵을 반환한다.

반환된 맵에 담긴 각각의 값은 해당 카테고리에 속하는 원소들을 모두 담은 리스트다.

```java
// 아이템 45에 아나그램 프로그램에 사용한 수집기(알파벳화한 단어를 알파벳화 결과가 같은 단어들의 리스트로 매핑하는 맵을 생성한다)
words.collect(groupingBy(AnagramsV3::alphabetize))
```

**groupingBy(Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream)**

groupingBy가 반환하는 수집기가 리스트 외의 값을 갖는 맵을 생성하게 하려면, 분류 함수와 함께 다운스트림(downstream) 수집기도 명시해야한다.

다운스트림 수집기의 역할은 해당 카테고리의 모든 원소를 담은 스트림으로부터 값을 생성하는 일이다.

- 이 매개 변수를 사용하는 가장 간단한 방법은 toSet()을 넘기는 것이다.
    
    groupingBy는 원소들의 리스트가 아닌 집합(Set)을 값으로 갖는 맵을 만들어낸다.
    
- toSet() 대신 toCollection(collectionFactory)를 거네는 방법이 있다**.**
    
    이 방법을 사용하면 리스트, 집합이 아닌 컬렉션을 값으로 가지는 맵을 생성한다. 원하는 컬렉션 타입을 선택할 수 있다는 장점이 있다.
    

- 다운스트림 수집기로 counting()을 건네는 방법도 있다.
    
     ****이렇게 하면 각 카테고리(키)를 (원소를 담은 컬렉션이 아닌) 해당 카테고리에 속하는 원소의 개수(값)와 매핑한 맵을 얻는다.
    

```java
Map<String, Long> freq = words.collect(**groupingBy(String::toLowerCase, counting()**));
```

**groupingBy(Function<? super T, ? extends K> classifier, Supplier<M> mapFactory, Collector<? super T, A, D> downstream)**

다운스트림 수집기에 더해 맵 팩터리도 지정할 수 있게 해준다.

이 메서드는 점층적 인수 목록 패턴에 어긋난다.

즉, mapFactory 매개변수가 downStream 매개변수보다 앞에 놓인다.

이 버전의 groupingBy를 사용하면 맵과 그안에 담긴 컬렉션 타입을 모두 지정할 수 있다.

⇒ 예를 들어 값이 TreeSet인 TreeMap을 반환하는 수집기를 만들 수 있다. ( TreeMap<String, TreeSet<Intger>>)

**그 외** **groupingBy 변형**

위 세가지 groupingBy에 대응하는 groupingByConcurrent 메서드가 있다.

메서드의 동시 수행 버전으로, `ConcurrentHashMap` 인스턴스를 만들어준다.

**partitioningBy(Predicate<? super T> predicate) && partitioningBy(Predicate<? super T> predicate, Collector<? super T, A, D> downstream)**

분류 함수자리에 프레디키트를 받고 키가 Boolean인 맵을 반환한다.

프레디키트에 더해 다운스트림 수집기까지 입력받는 버전도 다중정의되어 있다.

### 다운스트림 수집기 전용 메서드

`counting` 메서드가 반환하는 수집기는 다운스트림 수집기 전용이다.

Stream의 count 메서드를 직접 사용하여 같은 기능을 수행할 수 있으니 collect(counting()) 형태로 사용할 일은 없다.

이  외에도 `summing`, `averaging`, `summarizing`으로 시작하는 메서드들이 int, long, double 스트림용으로 하나씩 존재한다.

그 외에도 `reducing` 메서드들, `filtering`, `mapping`, `flatMapping`, `collectingAndThen` 메서드가 있는데, 이 수집기들은 스트림 기능의 일부를 복제하여 다운스트림 수집기를 작은 스트림처럼 동작하게 만든 것이다.

### 수집과는 상관 없는 Collecotors 메서드

**minBy(Comparator<? super T> comparator)**

스트림에서 가장 작은 원소를 반환한다.

**maxBy(Comparator<? super T> comparator)**

스트림에서 가장 큰 원소를 반환한다.

**joining() && joining(CharSequence delimiter) && joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)**

문자열 등의 CharSequence 인스턴스의 스트림에만 적용할 수 있는 메서드로 원소들을 연결하는 수집기를 반환한다.

매개변수로 구분 문자, 접두문자, 접미문자를 받아 컬렉션을 출력한 듯한 문자열을 생성한다.

### 정리

스트림 파이프라인 프로그래밍의 핵심은 부작용 없는 함수 객체에 있다. 스트림뿐만 아니라 스트림 관련 객체에 건내지는 모든 함수 객체가 부작용이 없어야 한다.

종단 연산 중 forEach는 스트림이 수행한 계산결과를 보고할 때만 이용해야 한다. 계산 자체에는 사용하지 말자

스트림의 수집기를 잘 알아두고 사용하자(toList, toSet, groupingBy, joining 등등)