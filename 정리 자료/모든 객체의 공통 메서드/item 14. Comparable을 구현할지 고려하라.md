# item 14. Comparable을 구현할지 고려하라

<aside>
💡 Comparable 인터페이스의 메서드인 compareTo 메서드를 재정의하는 방법을 알아보자

</aside>

### compareTo와 equals의 차이점

- compareTo는 단순 동치성 비교에 더해 순서까지 비교할 수 있다
- 제네릭하다
- Comparable을 구현했다는 것은 자연적인 순서(natureal order)가 있음을 뜻한다
- Comparable을 구현한 객체들의 배열은 `Arrays.sort(a)` 로 손쉽게 정렬이 가능하다
- 사실상 자바 플랫폼 라이브러리의 모든 값 클래스와 열거타입이 Comparable을 구현 했다

<aside>
📍 알파벳, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인터페이스를 구현하자

</aside>

### compareTo 메서드의 일반 규약

> 이 객체와 주어진 객체의 순서를 비교한다. **이 객체가 주어진 객체보다 작으면 음의 정수를, 같으면 0, 크면 양의 정수를 반환한다.** 이 객체와 비교할 수 없는 타입의 객체가 주어지면 `ClassCastException`을 던진다.

다음 설명에서 sgn(표현식) 표기는 수학에서 말하는 부호 함수(signum function)를 뜻하며, 표현식의 값이 음수, 0, 양수일 때, -1, 0, 1을 반환하도록 정의했다.

1. Comparable을 구현한 클래스는 모든 x, y에 대해 sgn(x.compareTo(y)) == -sgn(y.compareTo(x))여야 한다(따라서 x.compareTo(y)는 y.compareTo(x)가 예외를 던질 때에 한해 예외를 던져야한다.

2. Comparable을 구현한 클래스는 추이성을 보장해야 한다. 즉, (x.compareTo(y) > 0 && y.compareTo(z) > 0)이면 x.compareTo(z) > 0이다.

3. Comparable을 구현한 클래스는 모든 z에 대해 x.compareTo(y) == 0이면 sgn(x.compareTo(z)) == sgn(y.compareTo(z))이다.

4. 이번 권고가 필수는 아니지만 꼭 지키는 게 좋다. (x.compareTo(y) == 0) == (x.equals(y))여야 한다. Comparable을 구현하고 이 권고를 지키지 않는 모든 클래스는 그 사실을 명시해야 한다. 다음과 같이 명시하면 적당할 것이다. 

”주의 : 이 클래스의 순서는 equals 메서드와 일관되지 않다”


compareTo를 활용하는 클래스로 정렬된 컬렉션인 TreeSet, TreeMap 그리고 검색과 정렬 알고리즘을 활용하는 유틸리티 클래스인 Collections와 Arrays가 있다. 

1, 2, 3번의 규약은 compareTo 메서드로 수행하는 동시성 검사도 equals 규약과 똑같이 반사성, 대칭성, 추이성을 충족해야 함을 뜻한다.

기존 클래스를 확장한 구체 클래스에서 새로운 값 컴포넌트를 추가했다면 compareTo 규약을 지킬 방법은 없다 ⇒ equals와 같은 방법으로 우회하는 방법으로, 확장 대신 컴포지션을 활용하자

마지막 규약을 지키지 않을 시 이 클래스의 객체를 정렬된 컬렉션에 넣으면 해당 컬렉션이 구현한 인터페이스(Collection, Set, Map)에 정의된 동작가 엇박자가 날 수 있다.

⇒정렬된 컬렉션들은 동치성을 비교할 때 equals 대신 compareTo를 사용하기 때문!

⇒BigDecimal이 위 규약을 지키지 않은 예로 HashSet과 TreeSet에서의 동작이 다르다

### compareTo 작성 요령

1. **Comparable은 타입을 인수로 받는 제네릭 인터페이스이므로 compareTo 메서드의 인수 타입은 컴파일시점에 정해진다. 즉 타입 확인 또는 형 변환이 필요없다.**
2. **compareTo 메서드는 각 필드가 동치인지 비교하는 게 아니라 그 순서를 비교한다.** 
3. **객체 참조필드를 비교하려면 compareTo를 재귀적으로 호출한다.**
4. **Comparable을 구현하지 않은 필드나 표준이 아닌 순서로 비교해야 한다면 비교자(Comparater)를 대신 사용한다.(비교자는 직접 만들거나 자바가 제공하는 것 중 골라 쓰면 된다)** 

```java
// 코드 14-1) 객체 참조 필드가 하나 뿐인 비교자
public final class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {
		public int compareTo(CaseInsensitiveString cis) {
				return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s) // 자바에서 제공하는 비교자를 사용
}
```

1. **comapreTo 메서드에서 관계 연산자 < 와 >를 사용하는 예전 방식은 피하자**
2. **가장 핵심적인 필드부터 비교하여 불필요한 비교를 피하자**

```java
public class PhoneNumber implements Comparable<PhoneNumber> {
    private Short areaCode;
    private Short prefix;
    private Short lineNum;

    @Override
    public int compareTo(PhoneNumber phoneNumber) {
        int result = Short.compare(areaCode, phoneNumber.areaCode);   // 가장 중요한 필드
        if (result == 0) {
            result = Short.compare(prefix, phoneNumber.prefix);       // 두 번째로 중요한 필드
            if (result == 0) {
                result = Short.compare(lineNum, phoneNumber.lineNum); // 세 번째로 중요한 필드
            }
        }
        return result;
    }
}
```

1. **Comparator 인터페이스가 일련의 비교자 생성 메서드와 팀을 꾸려 메서드 연쇄방식으로 비교자를 생성하자**

**기본타입의 경우** 

```java
public class PhoneNumber implements Comparable<PhoneNumber> {
    private static final Comparator<PhoneNumber> COMPARATOR = comparingInt((PhoneNumber phoneNumber) -> phoneNumber.areaCode)
            .thenComparingInt(phoneNumber -> phoneNumber.prefix)
            .thenComparingInt(phoneNumber -> phoneNumber.lineNum);

    private Short areaCode;
    private Short prefix;
    private Short lineNum;

    public int compareTo(PhoneNumber phoneNumber) {
        return COMPARATOR.compare(this, phoneNumber);
    }
}
```

가독성이 좋고 간결하다 하지만 성능 저하가 있긴 하다. 

자바의 숫자용 기본타입 모두를 커버 할 수 있다. (long, double, int, float, double)

**객체 참조 타입의 경우**

![Untitled](item%2014%20Comparable%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%80%E1%85%AE%E1%84%92%E1%85%A7%E1%86%AB%E1%84%92%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%B5%20%E1%84%80%E1%85%A9%E1%84%85%E1%85%A7%E1%84%92%E1%85%A1%E1%84%85%E1%85%A1%2074197ccfb251467aa921f7383d0e3311/Untitled.png)

![Untitled](item%2014%20Comparable%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%80%E1%85%AE%E1%84%92%E1%85%A7%E1%86%AB%E1%84%92%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%B5%20%E1%84%80%E1%85%A9%E1%84%85%E1%85%A7%E1%84%92%E1%85%A1%E1%84%85%E1%85%A1%2074197ccfb251467aa921f7383d0e3311/Untitled%201.png)

객체 참조용 비교자 생성 메서드도 있다. comparing이라는 정적 메서드 2개가 다중정의되어 있다.

- 첫 번째는 키 추출자를 받아서 그 키의 자연적 순서를 사용한다.
- 두 번째는 키 추출자 하나와 추출된 키를 비교할 비교자까지 총 2개의 인수를 받는다.

또한, thenComparing이란 인스턴스 메서드가 3개 다중정의되어 있다.

- 첫 번째는 비교자 하나만 인수로 받아 그 비교자로 부차 순서를 정한다.
- 두 번째는 키 추출자를 인수로 받아 그 키의 자연적 순서로 보조 순서를 정한다.
- 세 번째는 키 추출자 하나와 추출된 키를 비교할 비교자까지 총 2개의 인수를 받는다.

```java
public class PhoneNumber implements Comparable<PhoneNumber> {
		// 이름 정렬 후 나이 순으로 정렬
    private static final Comparator<PhoneNumber> PERSON_COMPARATOR =
            Comparator.comparing((PhoneNumber phoneNumber) -> phoneNumber.person.getName())
            .thenComparing((PhoneNumber phoneNumber) -> phoneNumber.person.getAge());
    
    private int areaCode;
    private int prefix;
    private int lineNum;

    private Person person;

    @Override
    public int compareTo(PhoneNumber phoneNumber) {
        return PERSON_COMPARATOR.compare(this, phoneNumber);
    }
}
```

![Untitled](item%2014%20Comparable%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%80%E1%85%AE%E1%84%92%E1%85%A7%E1%86%AB%E1%84%92%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%B5%20%E1%84%80%E1%85%A9%E1%84%85%E1%85%A7%E1%84%92%E1%85%A1%E1%84%85%E1%85%A1%2074197ccfb251467aa921f7383d0e3311/Untitled%202.png)

![Untitled](item%2014%20Comparable%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%80%E1%85%AE%E1%84%92%E1%85%A7%E1%86%AB%E1%84%92%E1%85%A1%E1%86%AF%E1%84%8C%E1%85%B5%20%E1%84%80%E1%85%A9%E1%84%85%E1%85%A7%E1%84%92%E1%85%A1%E1%84%85%E1%85%A1%2074197ccfb251467aa921f7383d0e3311/Untitled%203.png)

1. **값의 차를 기준으로 첫번 째 값이 크면 양수를 반환하는 compareTo나 compare 방식은 피하자**

```java
 public class HashCodeOrderComparator {
    static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            return o1.hashCode() - o2.hashCode();
        }
    };
}
```

정수 오버플로를 일으키거나 IEEE 754 부동소수점 계산 방식에 따른 오류를 낼 수 있다.

대신 아래 두 코드를 사용하자

**정적 compare 메서드를 활용한 비교자**

```java
// 정적 compare 메서드를 활용한 비교자
class HashCodeOrder {
    static Comparator<Object> staticHashCodeOrder = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            return Integer.compare(o1.hashCode(), o2.hashCode());
        }
    };
}
```

**비교자 생성 메서드를 활용한 비교자**

```java
// 비교자 생성 메서드를 활용한 비교자
class HashCodeOrder {
    static Comparator<Object> comparatorHashCodeOrder = Comparator.comparingInt(o -> o.hashCode());
}
class HashCodeOrder {
    static Comparator<Object> comparatorHashCodeOrder = Comparator.comparingInt(o -> o.hashCode());
}
```

### 정리

순서를 고려해야 하는 값 클래스를 작성한다면 꼭 Comparable 인터페이스를 구현하여, 그 인스턴스들을 쉽게 정렬하고, 검색하고, 비교 기능을 제공하는 컬렉션과 어우러지도록 해야한다.

compareTo 메서드에서 필드의 값을 비교할 때 < , > 와 같은 비교 연산자는 쓰지말아야한다.

**대신 박싱된 기본 타입 클래스가 제공하는 정적 compare 메서드나 Comparator 인터페이스가 제공하는 비교자 생성 메서드를 사용하자**