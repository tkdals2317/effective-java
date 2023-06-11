# item 58. 전통적인 for문 보다는 for-each 문을 사용하라

<aside>
💡 전통적인 for 문 보다 향상된 for 문(for-each)이 나은 이유를 알아보자

</aside>

### 전통적인 for문

```java
// 코드 58-1 컬렉션 순회하기 - 더 나은 방법이 있다.
for (Iterator<Element> i = c.iterator(); i.hasNext(); ) {
    Element e = i.next();
    // e와 i로 무언가를 한다.
}

// 코드 58-2 배열 순회하기 - 더 나은 방법이 있다.
for (int i = 0; i < a.length; i++) {
    // a[i]로 무언가를 한다.
}
```

- while 문 보다는 낫지만 가장 좋은 방법은 아니다.
- 쓰이는 요소의 종류가 늘어나면 오류가 생길 가능성이 높아진다.
- 잘못된 변수를 사용했을 때 컴파일러가 잡아주리라는 보장도 없다.
- 컬렉션이냐 배열이냐에 따라 코드 형태가 상당히 달라지므로 주의해야한다.

### for-each 문 (향상된 for문)

위 문제들은 for-each 문을 사용하면 모두 해결된다.

```java
// 코드 58-3 컬렉션과 배열을 순회하는 올바른 관용구
for (Element e : elements) {
    // e 로 무언가를 한다.
}
```

- 반복자와 인덱스 변수를 사용하지 않으니 코드가 깔끔해지고 오류가 날 일도 없다.
- 하나의 관용구로 컬렉션과 배열을 모두 처리할 수 있어 어떤 컨테이너를 다루는지 신경쓰지 않아도 된다.
- for-each 문을 사용한다고 속도가 저하되는 것도 아니다.
- 컬렉션과 배열은 물론 Iterable 인터페이스를 구현한 객체면 무엇이든 순회할 수 있다.

### 중첩 컬렉션에서의 전통적인 for문의 문제점

```java
// 코드 58-4 버그를 찾아보자
enum Suit {CLUB, DIAMOND, HEART, SPADE}
enum Rank {ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING}

static Collection<Suit> suits = Arrays.asList(Suit.values());
static Collection<Rank> ranks = Arrays.asList(Rank.values());

List<Card> deck = new ArrayList<>();
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); ) {
        deck.add(new Card(i.next(), j.next()));
    }
}
```

**문제점** 

바깥 컬렉션(suits)의 next 메서드가 너무 많이 불린다.

숫자가 바닥나면 반복문에서 `NoSuchElementException`을 던진다.

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/0e194190-1117-406d-bdc1-6af03daee37e)

컬렉션의 크기가 안쪽 컬렉션 크기의 배수라면 이 반복문은 예외를 던지지 않고 종료한다. 

```java
// 코드 58-5 같은 버그, 다른 증상
for (Iterator<Face> i = faces.iterator(); i.hasNext();) {
    for (Iterator<Face> j = faces.iterator(); j.hasNext();) {
        System.out.println(i.next() + " " + j.next());
    }
}

==== output ====
ONE ONE
TWO TWO
THREE THREE
FOUR FOUR
FIVE FIVE
```

위 코드는 예외를 던지지 않지만 가능한 조합을 모두 출력하지 않고 “ONE ONE” 부터 “SIX SIX” 단 여섯쌍만 출력하고 끝나버린다.

이 문제를 해결하기 위해서는 바깥 원소를 저장하는 변수를 하나 추가해야한다.

```java
// 코드 58-6 문제는 고쳤지만 보기 좋지 않다. 더 나은 방법이 있다.
List<Card> deck2 = new ArrayList<>();
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
    Suit suit = i.next();
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); ) {
        deck2.add(new Card(suit, j.next()));
    }
}
```

하지만 for-each 문을 사용하면 이 문제는 간단하게 해결된다.

```java
// 코드 58-7 컬렉션이나 배열의 중첩 반복을 위한 권장 관용구
List<Card> deck3 = new ArrayList<>();
for (Suit suit : suits) {
    for (Rank rank : ranks) {
        deck3.add(new Card(suit, rank));
    }
}
```

### for-each 문을 사용하지 못하는 상황

- 파괴적인 필터링 - 컬렉션을 순회하면서 선택된 원소를 제거해야 한다면 반복자의 remove 메서드를 호출해야 한다. 자바 8부터는 Collection의 removeIf 메서드를 사용해 컬렉션을 명시적으로 순회하는 일을 피할 수 있다.
- 변형 - 리스트를 순회하면서 그 원소의 값 일부 혹은 전체를 교체해야 한다면 리스트의 반복자나 배열의 인덱스를 사용해야한다. (ex. 정렬)
- 병렬 반복 - 여러 컬렉션을 병렬로 순회해야 한다면 각각의 반복자와 인덱스 변수를 사용해 엄격하고 명시적으로 제어해야 한다.

### 정리

전통적이 for문 보다 for-each가 더 명료하고, 유연하고 버그를 예방해주고 성능저하도 없다.

가능한 모든 곳에서 for 문이 아닌 for-each 문을 사용하자