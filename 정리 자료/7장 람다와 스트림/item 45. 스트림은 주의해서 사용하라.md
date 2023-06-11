# item 45. 스트림은 주의해서 사용하라

<aside>
💡 스트림 API에 대해 알아보고 스트림을 사용할 시 주의해야 할 점을 알아보자

</aside>

### 스트림 API

스트림 API는 다량의 데이터 처리 작업을 돕고자 자바 8에 추가 되었다.

**Stream API 핵심 추상 개념**

1. 스트림은 데이터 원소의 유한 혹은 무한 시퀀스를 뜻한다.
2. 스트림 파이프라인은 이 원소들로 수행하는 연산 단계를 표현하는 개념이다.

대표적으로 컬렉션, 배열, 파일, 정규표현식 패턴 매처(matcher), 난수 생성기 등이 있다.

스트림 안의 데이터 원소들은 객체 참조나 기본 타입 값이다. 기본 타입 값으로는 `int`, `long`, `double`을 지원한다.

### 스트림 API의 특징

스트림 파이프라인은 **소스 스트림**에서 시작하여 **종단 연산**으로 끝나며, 그 사이에 하나 이상의 **중간 연산**이 있을 수 있다.

- **중간 연산**은 스트림을 어떠한 방식으로 변환(transform)한다.
    
    각 원소에 함수를 적용하거나 특정 조건을 만족 못하는 원소를 걸러낼 수 있다.
    
    중간 연산들은 모두 한 스트림을 다른 스트림으로 변환하는데, 변환된 스트림의 원소 타입은 변환 전 원소 타입과 같을 수도 있고 다를 수도 있다.
    

- **종단 연산**은 마지막 중간 연산이 내놓은 스트림에 최후에 연산을 가한다.
    
    원소를 정렬해 컬렉션에 담거나, 특정 원소를 하나 선택하거나, 모든 원소를 출력하는 식이다.
    

- **스트림 파이프라인은 지연 평가(lazy evaluation)된다.**
    
    평가는 종단 연산이 호출될 때 이뤄지며, 종단 연산에 쓰이지 않는 데이터 원소는 계산에 쓰이지 않는다.
    
    이러한 지연 평가가 무한 스트림을 다룰 수 있게 하는 열쇠이다.
    
    반드시 종단 연산을 빼먹는 일이 없도록 하자
    

- **스트림 API는 메서드 연쇄를 지원하는 플루언트 API(fluent API)다.**
    
    파이프라인 하나를 구성하는 모든 호출을 연결하여 단 하나의 표현식으로 완성 할 수 있다.
    
    파이프라인 여러 개를 연결해 하나의 표현식 하나로 만들 수 있다.
    

- **스트림 파이프라인은 순차적으로 수행된다.**
    
    파이프라인을 병렬로 실행하려면 파이프라인을 구성하는 스트림 중 하나에서 parallel 메서드를 호출해주기만 하면 되나, 효과를 볼 수 있는 상황은 많지 않다.(아이템 48)
    

스트림 API를 제대로 사용하면 프로그램이 짧고 깔끔해지지만, 잘못 사용하면 읽기 어렵고 유지보수도 힘들어진다.

### 스트림의 올바른 사용법

![Untitled](https://user-images.githubusercontent.com/49682056/232054817-afa8dd92-dc07-4e44-a659-d41db69d5ac5.png)

![Untitled 1](https://user-images.githubusercontent.com/49682056/232054810-1224655f-12c5-424b-a2c3-42b1d6d6cb12.png)

```java
// 코드 45-1 사전 하나를 훑어 원소 수가 많은 아나그램 그룹들을 출력한다.
public class AnagramsV1 {
    public static void main(String[] args) throws IOException {
        File dictionary = new File(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        HashMap<String, Set<String>> groups = new HashMap<>();
        try (Scanner s = new Scanner(dictionary)) {
            while (s.hasNext()) {
                String word = s.next();
                groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()).add(word);
            }
        }

        for (Set<String> group : groups.values()) {
            if (group.size() >= minGroupSize) {
                System.out.println(group.size() + " " + group);
            }
        }
    }

    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
```

이 프로그램은 사전 파일에서 단어를 읽어 사용자가 지정한 문턱값보다 원소수가 많은 아나그램 그룹을 출력한다.

![Untitled 2](https://user-images.githubusercontent.com/49682056/232054814-aa7f2865-7e6a-41cd-aa6c-374ce501b4f5.png)

위 코드를 과하게 Stream을 사용한 코드로 바꿔보자

```java
// 코드 45-2 스트림을 과하게 사용했다. - 따라 하지 말 것!
public class AnagramsV2 {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary)) {
            words.collect(
                    Collectors.groupingBy(word -> word.chars().sorted() // alphabetize 메서드 로직
                            .collect(StringBuilder::new,
                                    (sb, c) -> sb.append((char)c),
                                    StringBuilder::append).toString())
            ).values().stream()
                    .filter(group -> group.size() >= minGroupSize)
                    .map(group -> group.size() + " " + group)
                    .forEach(System.out::println);
        }
    }
}
```

사전을 여는 부분만 제외하면 프로그램 전체가 단 하나의 표현식으로 처리된다. 

하지만 코드를 이해하기 힘들고 가독성도 많이 떨어진다.

**스트림을 과용하면 프로그램이 읽거나 유지보수하기 어려워진다.**

그럼 적절히 사용한 예제를 보자

```java
// 코드 45-3 스트림을 적절히 활용하면 깔끔하고 명료해진다.
public class AnagramsV3 {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary)) {              // 사전 파일을 열고 스트림 변수 이름을 words로 지어 각 원소가 단어임을 명확히 한다.
            words.collect(                                                  // 종단 연산                                       
                    groupingBy(AnagramsV3::alphabetize)                     // 모든 단어를 수집해 맵으로 모은다.
            ).values().stream()                                             // 맵의 values()가 반환한 값으로 새로운 Stream<List<String>을 연다.
                    .filter(group -> group.size() >= minGroupSize)          // 중간연산 : 리스트들 중 원소가 minGroupSize보다 적은 것은 필터링되 무시된다.
                    .forEach(g -> System.out.println(g.size() + " " + g));  // 종단 연산 : 필터링된 리스트를 출력한다.
        }
    }
    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
```

이 전 코드보다 훨씬 이해하기 쉽다. 

람다에서는 타입 이름이 생략되므로 람다의 매개변수 가독성을 위해 주의해서 잘 지어주자(위 코드의 g는 안좋은 예)

알파벳 순으로 정렬해주는 alphabetize 메서드과 같이 세부 구현을 주 프로그램 로직 밖으로 따로 빼줌으로써 가독성을 높였다.

**주의 해야할 점은 Stream은 char용 스트림을 지원하지 않기 때문에 cahr 값들은 Stream으로 처리하는 건 피하자**

```java
"Hello world!".chars().forEach(System.out::print);
==== output ====
721011081081113211911111410810033

"Hello world!".chars().forEach(x -> System.out.print((char) x)); // 따로 형변환을 명시적으로 해줘야한다.
==== output ====
Hello world!
```

### 스트림이라고 만능은 아니다 ! 함수 객체로는 할 수 없지만 코드 블록으로는 할 수 있는 것들

- 코드 블록에서는 범위 안의 지역변수를 읽고 수정할 수 있다. 하지만 람다에서는 final이거나 사실상 final인 변수만 읽을 수 있고, 지역 변수를 수정하는 건 불가능하다.
- 코드 블록에서는 return 문을 사용하여 메서드를 빠져나가거나, break나 continue 문으로 블록 바깥의 반복문을 종료하거나 반복을 한번 건너 뛸 수 있다. 또한 메서드 선언에 명시된 검사 예외를 던질 수 있다. 하지만 람다로는 이 중 어떤 것도 할 수 없다.

### 스트림에 적합한 케이스

- 원소들의 시퀀스를 일관되게 변환한다.
- 원소들의 시퀀스를 필터링한다.
- 원소들의 시퀀스를 하나의 연산을 사용해 결합한다. (더하기, 연결하기, 최솟값 구하기 등)
- 원소들의 시퀀스를 컬렉션에 모은다(아마도 공통된 속성을 기준으로 묶어가며)
- 원소들의 시퀀스에서 특정 조건을 만족하는 원소를 찾는다.

### 스트림이 처리하기 힘든 케이스

대표적으로 한 데이터가 파이프라인의 여러 단계를 통과할 때 이 데이터의 각 단계에서의 값들을 동시에 접근하기는 어려운 경우이다.

스트림 파이프라인은 일단 한 값을 다른 값에 매핑하고 나면 원래의 값은 잃는 구조이기 때문이다. (스트림은 일회성이라 한번 읽고 나서 버린다.)

### flatMap을 통한 데카르트곱 구현

```java
public class Deck {
    public static void main(String[] args) {
        System.out.println(newDeckV1());
        System.out.println(newDeckV2());
    }
    
    // 코드 45-4 데카르트 곱 계산을 반복 방식으로 구현
    private static List<Card> newDeckV1() {
        List<Card> result = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                result.add(new Card(suit, rank));
            }
        }
        return result;
    }
    // 코드 45-5 데카르트 곱 계산을 스트림 방식으로 구현
    private static List<Card> newDeckV2() {
        return Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toList());
    }
    
    private static class Card {
        Suit suit;
        Rank rank;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }

        @Override
        public String toString() {
            return suit.toString() + " " +rank.toString();
        }
    }

    private enum Suit {
        SPADE, HEART, DIAMOND, CLUB
    }

    private enum Rank {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE
    }
}
```

모든 조합을 구하는 데카르트 곱을 for-each 중첩으로 구현한 newDeckV1 메서드와 Stream의 flatMap 메서드를 통해 구현한 newDeckV2 메서드를 비교해보자

여기서 `flatMap` 메서드는 스트림의 원소 각각을 하나의 스트림으로 매핑한 다음 스트림들을 다시 하나의 스트림으로 합친다.

이를 **평탄화**라고 한다.

이렇게 flatMap 메서드를 사용하면 중첩 for-each도 Stream으로 표현할 수 있다.

하지만 이 둘은 취향차니 취향 껏 골라 쓰도록 하자

### 정리

스트림을 잘 사용하면 코드 가독성이 높아지고 어려운 동작들도 해결할 수 있다.

가독성을 해치지 않는 선에서 스트림을 잘 사용해보자!