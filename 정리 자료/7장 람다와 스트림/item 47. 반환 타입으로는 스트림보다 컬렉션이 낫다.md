# item 47. 반환 타입으로는 스트림보다 컬렉션이 낫다

<aside>
💡 원소 시퀀스를 반환하는 방법에 대해 알아보자

</aside>

### 원소 시퀀스를 반환하는 메서드

즉 일련의 원소를 반환하는 메서드는 수없이 많다.

자바 칠까지는 이런 메서드의 반환형을 `Collection` , `Set` , `List` 같은 컬렉션 인터페이스 혹은 `Iterable` 인터페이스를 썻다.

반환 원소들이 기본타입이거나 성능에 민감한 상황이라면 배열을 썼다.

### 스트림은 반복을 지원하지 않지만 지원한다?

스트림은 반복(iteration)을 지원하지 않는다. (for-each를 못쓴다고 생각하면 될 거 같다)

따라서 스트림과 반복을 알맞게 조합해야 좋은 코드가 나온다.

API를 스트림만 반환하도록 짜놓으면 반환된 스트림을 for-each로 반복하길 원하는 사용자는 불편함을 가지게 될 것이다.

사실 Stream 인터페이스는 Iterable 인터페이스가 정의한 추상 메서드를 전부 포함할 뿐만 아니라, Iterable 인터페이스가 정의한 방식대로 동작한다.

그럼에도 for-each로 스트림을 반복할 수 없는 까닭은 바로 Stream이 Iterable을 확장(extends)하지 않아서다.

### Stream을 Iterable로 바꿔보자

Stream의 iterable 메서드에 메서드 참조를 건네서 해결해보자

```java
for (ProcessHandle ph : ProcessHandle.allProcesses()::iterator) {
    // 프로세스를 처리한다.
}
```

아쉽게도 위 코드는 컴파일 오류를 낸다.

이 오류를 잡으려면 매개변수화된 Iterable로 적절히 형변환해주어야 한다.

```java
// 코드 47-2 스트림 반복하기 위한 '끔직한' 우회 방법
for (ProcessHandle ph : (Iterable<ProcessHandle>) ProcessHandle.allProcesses()::iterator) {
	  // 프로세스를 처리한다.
}
```

하지만 위 코드는 실전에 쓰기에는 너무 난잡하고 직관성이 떨어진다.

이런 문제는 자바의 타입 추론을 이용한 어댑터 메서드를 사용하여 개선할 수 있다.

```java
for (ProcessHandle ph : iterableOf(ProcessHandle.allProcesses())) {
		// 프로세스를 처리한다.
}

// 코드 47-3 Stream<E>를 Iterable<E>로 중개해주는 어댑터
private static <E> Iterable<E> iterableOf(Stream<E> stream) {
    return stream::iterator;
}
```

### Stream을 반환하냐 Iterator를 반환하냐 둘 중의 tradeoff : 해결책은 어댑터!

아이템 45의 아나그램 프로그램에서 스트림 버전은 사전을 읽을 때 Files.lines() 메서드를 이용했고 (Stream을 반환)

```java
try (Stream<String> words = Files.lines(dictionary)) {
...
}
```

![Untitled](https://user-images.githubusercontent.com/49682056/233051966-aead4606-a591-447f-8505-1de0c36fc940.png)

반복 버전에서는 스캐너를 이용했다. (Iterator를 반환)

```java
try (Scanner s = new Scanner(dictionary)) {
...
}
```

![Untitled 1](https://user-images.githubusercontent.com/49682056/233051950-50fe709e-4324-4b7a-9d91-a0e742bead10.png)

둘 중 모든 예외를 알아서 처리해준다는 점에서 Files.lines 쪽이 더 우수하다.

그래서 이상적으로는 반복버전에서도 Files.lines를 써야했다.

이는 스트림만 반환하는 API가 반환한 값을 for-each로 반복하길 원하는 프로그래머가 감수해야 할 부분이다.

반대로, API가 Iterable만 반환하면 이를 스트림 파이프라인에서 처리하려는 프로그래머가 감수해야한다.

그래서 이럴 때는 `Iterable<E>` 과 `Stream<E>` 로 중개해주는 어댑터를 만들어주면 된다. (위 코드 47-3의 반대 버전으로 보면된다)

```java
// 코드 47-4 Iterable<E>를 Stream<E>로 중개해주는 어댑터
private static <E> Stream<E> streamOf(Iterable<E> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false);
}
```

만약 객체 시퀀스를 반환하는 메서드를 작성하는데, 이메서드가 오직 스트림 파이프라인에서만 쓰이는 걸 안다면 맘 놓고 스트림을 반환하게 해주자.

반대로 반환된 객체들이 반복문에서만 쓰일 걸 안다면 Iterable을 반환하자.

### Collection을 반환하는 게 베스트인 이유

Collection 인터페이스는 Iterable의 하위 타입이고, stream 메서드도 제공하니 반복과 스트림을 모두 지원한다.

**따라서 원소 시퀀스를 반환하는 공개 API 반환 타입에는 Collection이나 그 하위 타입을 쓰는 게 일반적으로 최선이다.(이 장의 핵심 내용)**

Arrays 역시 Arrays.asList와 Stream.of 메서드로 손쉽게 반복과 스트림을 지원할 수 있다.

### 덩치가 큰 시퀀스를 메모리에 올리는 경우는 일반 컬렉션 대신 전용 컬렉션을 반환하자

반환하는 시퀀스의 크기가 메모리에 올려도 안전할 만큼 작다면 ArrayList나 HashSet 같은 표준 컬렉션 구현체를 반환하는 게 최선일 수 있다.

**하지만 단지 컬렉션을 반환한다는 이유로 덩치 큰 시퀀스를 메모리에 올려서는 안된다.**

멱집합을 예제로 전용 컬렉션 예제로 살펴보자

⇒ {a, b, c} 의 멱집합 = {{}, {a}, {b}, {c}, {a, b}, {b, c}, {a, c}, {a, b, c}} 

⇒ 원소 개수가 n개라면 2^n개 만큼의 멱집합이 생긴다.

⇒ 원소의 개수가 늘어날 수록 멱집합의 갯수는 기하급수적으로 늘어나므로 표준 컬렉션 구현체에 저장하는 것은 위험하다.

**⇒ AbstractList로 전용 클래스를 구현하자**

```java
// 코드 47-5 입력 집합의 멱집합을 전용 컬렉션에 담아 반환한다
public class PowerSet {
    public static final <E> Collection<Set<E>> of(Set<E> s) {
        List<E> src = new ArrayList<>(s);
        if (src.size() > 30)
            throw new IllegalArgumentException(
                    "집합의 원소가 너무 많습니다(최대 30개)." + s
            );

        return new AbstractList<Set<E>>() {
            @Override
            public Set<E> get(int index) {
                Set<E> result = new HashSet<>();
                for (int i = 0; index != 0; i++, index >>= 1) {
                    if ((index&1) == 1) {
                        result.add(src.get(i));
                    }
                }
                return result;
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof Set && src.containsAll((Set)o);
            }

            @Override
            public int size() {
                // 멱집합의 크기는 2를 원래 집합의 원소 수만큼 거듭제곱 것과 같다.
                return 1 << src.size();
            }
        };
    }

    public static void main(String[] args) {
        Set<String> set = Set.of("a", "b", "c");

        Collection<Set<String>> of = PowerSet.of(set);

        System.out.println(of.size());                  // 8
        System.out.println(of);                         // [[], [a], [c], [a, c], [b], [a, b], [b, c], [a, b, c]]
        System.out.println(of.contains(Set.of("a")));   // true
        System.out.println(of.contains(Set.of("d")));   // false

        Set<Integer> integerSet = new HashSet<>();
        IntStream.rangeClosed(0, 30)
                .forEach(integerSet::add);
        Collection<Set<Integer>> throwException = PowerSet.of(integerSet); // IllegalArgumentException: 집합의 원소가 너무 많습니다(최대 30개)

    }
}
```

멱집합을 구성하는 각 원소의 인덱스를 비트 벡터로 사용한다.

인덱스의 n번째 비트 값은 멱집합의 해당 원소가 원래 집합의 n번째 원소를 포함하는지 여부를 알려준다. 따라서 0부터 2^n-1 까지의 이진수와 원소 n개인 집합의 멱집합과 자연스럽게 매핑된다.

AbstarctCollection을 활용해서 Collection 구현체를 작성할 떄는 Iterable용 메서드 외 2개만 더 구현하면 된다. 

바로 contains와 size다.

contains와 size를 구현하는 게 불가능할 때는 컬렉션보다는 스트림이나 Iterable을 반환하는 편이 낫다.

원한다면 별도의 메서드를 두어 두 방식을 모두 제공해도 된다.

### 입력 리스트의 부분 리스트를 반환하는 메서드를 스트림으로 반환해보자

입력 리스트의 (연속적인) 부분 리스트를 모두 반환하는 메서드를 작성해보자.

필요한 부분리스트를 만들어 표준 컬렉션에 담는 코드는 3줄이면 충분하지만, 컬렉션은 입력 리스트 크기의 거듭제곱만큼 메모리를 차지한다.

```java
for (int start = 0; start < list.size(); start++) {
    for (int end = start+1; end <= list.size(); end++) {
        System.out.println(list.subList(start, end));
    }
}
```

![Untitled 2](https://user-images.githubusercontent.com/49682056/233051958-0ad97741-3f45-4fb2-baab-c15b7a54a50e.png)

전용 클래스를 구현하기보다 Stream으로 구현하면 좀 더 쉽게 접근할 수 있다.

**첫번 째 방법**

첫번 째 원소를 포함하는 부분리스트를 그 리스트의 프리픽스라 하고, 같은 식으로 마지막 원소를 포함하는 부분 리스트를 그 리스트의 서픽스라 하자

어떤 리스트의 부분리스트는 단순히 그 리스트의 프리픽스의 서픽스(혹은 서픽스의 프리픽스)에 빈 리스트 하나만 추가하면 된다.

```java
public class SubLists {
    public static <E> Stream<List<E>> of(List<E> list) {
        return Stream.concat(Stream.of(Collections.emptyList()),
                prefixes(list).flatMap(SubLists::suffixes));
    }

    private static <E> Stream<List<E>> suffixes(List<E> list) {
        return IntStream.rangeClosed(0, list.size()).mapToObj(start -> list.subList(start, list.size()));
    }

    private static <E> Stream<List<E>> prefixes(List<E> list) {
        return IntStream.rangeClosed(1, list.size()).mapToObj(end -> list.subList(0, end));
    }
}
```

![Untitled 3](https://user-images.githubusercontent.com/49682056/233051960-1cb43e6c-5bed-4f52-8886-86d7f14e2307.png)

Stream.concat 메서드는 반환되는 스트림에 빈 리스트를 추가한다.

flatMap 메서드는 모든 프리픽스의 모든 서픽스로 구성된 하나의 스트림을 만든다.

마지막으로 프리픽스들과 서픽스들의 스트림은 IntStream.range와 IntStream.rangeClosed가 반환하는 연속된 정숫값들을 매핑해 만들었다.

**두번째 방법**

```java
for (int start = 0; start < list.size(); start++) {
    for (int end = start+1; end <= list.size(); end++) {
        System.out.println(list.subList(start, end));
    }
}
```

아이템 45에서 작성한 데카르트 곱용 코드와 비슷하게 위 반복문으로 스트림으로 만들 수 있다. 

```java
// 코드 47-7 입력 리스트의 모든 부분리스트를 스트림으로 반환한다.
public static <E> Stream<List<E>> of(List<E> list) {
    return IntStream.range(0, list.size())
            .mapToObj(start -> IntStream.rangeClosed(start + 1, list.size())
                    .mapToObj(end -> list.subList(start, end)))
            .flatMap(x -> x);
}
```

![Untitled 4](https://user-images.githubusercontent.com/49682056/233051964-0e1565c4-3018-4275-98e2-5e78852b1d5e.png)

위 코드는 첫번째 프리픽스, 서픽스와 다르게 빈 리스트를 반환하지 않는다.

반복이 자연스러운 상황에서도 사용자는 Stream을 쓰거나 Stream을 Iterable로 변환해주는 어댑터를 이용해야한다.

이러한 어댑터는 성능상 좋지 않고 전용 Collection을 사용하는 게 더 빠르다.

 

### 정리

원소 시퀀스를 반환하는 메서드를 작성할 때는, 스트림, 반복을 모두 지원할 수 있게 만들자

컬렉션을 반환할 수 있다면 베스트!

만약 특수한 상황이라면 전용 컬렉션을 구현할 지 고민하자

컬렉션이 반환이 불가능하다면 스트림과 Iterable 둘 중 자연스러운 것을 반환하라