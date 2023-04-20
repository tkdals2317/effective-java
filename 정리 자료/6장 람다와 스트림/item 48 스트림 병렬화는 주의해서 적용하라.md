# item 48. 스트림 병렬화는 주의해서 적용하라

<aside>
💡 스트림 병렬화를 지양하는 이유를 알아보자

</aside>

### 메르센 소수 생성 프로그램으로 보는 병렬처리 예제

```java
// 코드 48-1 스트림을 사용해 처음 20개의 메르센 소수를 생성하는 프로그램
public static void main(String[] args) {
    primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
            //.parallel()
            .filter(mersenne -> mersenne.isProbablePrime(50))
            .limit(20)
            .forEach(System.out::println);
}
static Stream<BigInteger> primes() {
    return Stream.iterate(TWO, BigInteger::nextProbablePrime);
}
```

위 로직을 병렬로 처리하려고 parrallel()을 붙히면 오히려 성능이 더 안좋아진다. (출력도 못함)

**느려진 원인은?**

스트림 라이브러리가 이 파이프라인을 병렬화하는 방법을 찾지 못해서이다.

**데이터 소스가 Stream.iterate거나 중간 연산으로 limit을 쓰면 파이프라인 병렬화로는 성능 개선을 기대할 수 없다.**

### 병렬화의 효과가 좋은 케이스

- ArrayList, HashMap, ConcurrentHashMap의 인스턴스인 경우
- 배열
- int 범위
- long 범위

위 자료구조들은 모든 데이터를 원하는 크기로 정확하고 손쉽게 나눌 수 있어서 일을 다수의 스레드로 분배하기에 좋다는 특징이 있다.

나누는 작업은 Spliterator가 담당하며, Spliterator 객체는 Stream이나 Iterable의 spiterator 메서드를 얻어 올 수 있다.

그리고 다른 공통점은 원소들을 순차적으로 실행할 때의 참조 지역성이 뛰어나다는 것이다.

⇒ 참조 지역성이 낮으면 스레드는 주 메모리에서 캐시 메모리로 전송되어 오기를 기다리며 시간을 멍하니 보내게 된다.

### 종단 연산의 병렬 수행 효율 영향

종단 연산에서 수행하는 작업량이 파이프라인 전체에서 많은 비중을 차지하면서 순차적인 연산이라면 파이프라인 병렬 수행의 효과는 제한 될 수 밖에 없다.

종단 연산에서 병렬화에 가장 적합한 것은 축소(reduction)이다.

축소는 파이프라인에서 만들어진 모든 원소를 하나로 합치는 작업을 말한다. (reduce 메서드 중 하나, min, max, count, sum)

또는 anyMatch, allMatch, noneMatch처럼 조건에 맞으면 바로 반환되는 메서드들도 병렬화에 적합하다.

반면 collect 메서드는 컬렉션들을 합치는 부담이 크므로 병렬화에 적합하지 않다.

### 직접 구현한 병렬화의 이점을 제대로 누리려면 Spliterator를 재정의하고 결과 스트림의 병렬화 성능을 강도 높게 테스트하라

하지만 고효율의 Spliterator를 구현하는 것은 쉽지 않다. ㅠㅠ

### 성능뿐만 아니라 결과 자체가 잘못되거나 예상 못한 동작이 발생할 수 있다.

안전 실패(결과가 잘못되거나 오동작하는 것)은 병렬화한 파이프라인이 사용하는 mapper, filters, 혹은 프로그래머가 작성한 함수객체가 명세대로 동작하지 않을 때 벌어질 수 있다.

### 조건만 만족한다면 성능 향상을 얻을 수 있다.

```java
// 코드 48-2 소수 계산 스트림 파이프라인 - 병렬화에 적합하다
static long pi(long n) {
    return LongStream.rangeClosed(2, n)
            .mapToObj(BigInteger::valueOf)
            .filter(i -> i.isProbablePrime(50))
            .count();
}

// 코드 48-3 소수 계산 스트림 파이프라인 - 병렬화 버전
static long pi(long n) {
    return LongStream.rangeClosed(2, n)
            .parallel()
            .mapToObj(BigInteger::valueOf)
            .filter(i -> i.isProbablePrime(50))
            .count();
}
```

책에서는 3.37배 빨라졌다고 한다.

### 무작위 수들로 이뤄진 스트림을 병렬화하려거든 ThreadLocalRandom(Random)보다는 SplittableRandom 인스턴스를 사용하자

병렬 처리용으로 설계되어 성능이 선형적으로 증가한다.

### 정리

게산도 올바르게 수행하고 성능도 빨라질 거라는 확신 없이는 스트림 파이프라인 병렬화는 시도하지 말자