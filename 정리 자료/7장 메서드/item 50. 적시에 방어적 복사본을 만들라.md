# item 50. 적시에 방어적 복사본을 만들라

<aside>
💡 방어적 복사본을 통해서 불변식을 지키고 클라이언트로 부터의 공격을 막아보자

</aside>

### 불변식을 지키지 못하는 클래스

```java
// 코드 50-1 기간을 표현하는 클래스 - 불변식을 지키지 못했다.
public final class PeriodV1 {
    private final Date start;
    private final Date end;

    /**
     * @param start 시작 시간
     * @param end 종료 시간
     * @throws IllegalArgumentException 시작 시간이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발생한다.
     */
    public PeriodV1(Date start, Date end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
        }
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
```

어떤 객체든 그 객체의 허락 없이는 외부에서 내부를 수정하는 일은 불가능하다. 하지만 주의를 기울이지 않으면 자기도 모르게 내부를 수정하도록 허락하는 경우가 생긴다.

위 클래스는 한번 값이 정해지면 변하지 않도록 할 생각이었지만 손 쉽게 그 불변식을 깨트릴 수 있다.

```java
// 코드 50-2 PeriodV1 인스턴스의 내부를 공격해보자
Date start = new Date();
Date end = new Date();

PeriodV1 periodV1 = new PeriodV1(start, end);
end.setYear(78); // periodV1의 내부를 수정했다!
```

![Untitled](https://user-images.githubusercontent.com/49682056/234605938-d6ee939c-459a-4103-92e2-1702e61c66b8.png)

Date는 낡은 API이니 새로운 코드를 작성할 때는 더 이상 사용하면 안된다.(책에 나와있는데… 우리 프로젝트를 보니 눈물이 났다.. 또르르)

### 생성자에서 방어적 복사를 해보자

**외부 공격으로부터 Period 인스턴스 내부를 보호하려면 생성자에서 받은 가변 매개변수 각각을 방어적으로 복사(defensive copy) 해야한다.**

Period 인스턴스 안에서는 원본이 아닌 복사본을 사용한다.

```java
// 코드 50-3 수정한 생성자 - 매개변수 방어적 복사본을 만든다.
public PeriodV2(Date start, Date end) {
    this.start = new Date(start.getTime());
    this.end = new Date(end.getTime());
    if (start.compareTo(end) > 0) {
        throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
    }
}
```

![Untitled 1](https://user-images.githubusercontent.com/49682056/234605918-8e4d42fd-4cd2-457a-bbaa-49fae12a7748.png)

PeriodV1과 달리 내부의 값이 바뀌지 않았다.

**매개변수 유효성을 검사하기 전에 방어적 복사본을 만들고, 이 복사본으로 유효성 검사를 한 점을 주목하자.**

순서가 부자연스러워보이겠지만 반드시 이렇게 작성해야 한다.

멀티스레딩 환경이라면 원본 객체의 유효성을 검사한 후 복사본을 만드는 그 찰나의 취약한 순간에 다른 스레드가 원본 객체를 수정할 위험이 있기 때문이다.(TOCTOU 공격)

그리고 방어적 복사에 Date의 clone을 사용하지 않은 점도 주목하자.

Date는 final 필드가 아니므로 clone이 Date가 정의한 게 아닐 수 있다. 

⇒ clone이 악의를 가진 하위 클래스의 인스턴스를 반환할 수도 있다.

⇒ 이 하위 클래스는 start와 end 필드의 참조를 private 정적 리스트에 담아뒀다가 공격자에게 이 리스트를 접근하는 길을 열어줄 수도 있다.

**매개변수가 제 3자에 의해 확잘 될 수 있는 타입이라면 방어적 보갓본을 만들 때 clone을 사용해서는 안된다.**

### 두번 째 공격

생성자를 수정하면 앞의 공격을 막을 수 있지만, Period 인스턴스는 아직 변경이 가능하다. 접근자 메서드가 내부의 가변 정보를 직접 드러내기 때문이다.

```java
// 코드 50-4 Period 인스턴스를 향한 두번 째 공격
Date start2 = new Date();
Date end2 = new Date();

PeriodV2 period = new PeriodV2(start2, end2);
period.getEnd().setYear(78);

System.out.println(period);
```

![Untitled 2](https://user-images.githubusercontent.com/49682056/234605928-50887552-b451-4dcc-8af6-61d250d2a97d.png)

### 접근자도 가변 필드의 방어적 복사본을 반환하자

```java
// 코드 50-5 수정한 접근 자 - 필드의 방어적 복사본을 반환한다.
public Date getStart() {
    return new Date(start.getTime());
}

public Date getEnd() {
    return new Date(end.getTime());
}
```

![Untitled 3](https://user-images.githubusercontent.com/49682056/234605933-cb101d59-3bd9-45a5-aee8-7e2b9a7886e7.png)

이렇게 접근자에도 가변 필드의 방어적 복사본을 반환하게 되면 Period는 완벽한 불변으로 거듭난다.

네이티브 메서드나 리플렉션 같이 언어외적인 수단을 동원하지 않고는 불변식을 위배할 수 없다.

모든 필드가 객체안으로 완벽하게 캡슐화 되었다.

생성자와 달리 접근자 메서드에서는 방어적 복사에 clone을 사용해도 된다. 

Period가 가지고 있는 Date 객체는 java.util.Date임이 확실하기 때문이다.

그렇더라도 아이템 13에서 설명한 이유 때문에 인스턴스를 복사하는 데는 일반적으로 생성자나 정적 팩터리를 쓰는게 좋다.

### 되도록 불변 객체를 조합해 객체를 구성해야 방어적 복사를 할 일이 줄어든다.

Period 예제의 경우 자바 8 이상으로 개발해도 된다면, Instant(혹은 LocalDateTime 이나 ZoneDateTime)을 사용하라

이전 버전의 자바를 사용한다면 Date 참조대신 Date.getTime()이 반환하는 long 정수를 사용하는 방법도 있다.

방어적 복사에는 성능 저하가 따르고, 또 항상 쓸 수 있는 것도 아니다.

- 호출자가 컴포넌트 내부에서 수정하리 않으리라 확신하면 방어적 복사를 생략할 수 있다. (매개변수나 반환값을 수정하지 말라라는 내용을 문서화하자)
- 다른 패키지에서 사용한다고 해서 넘겨받은 가변 매개변수를 항상 방어적으로 복사해 저장해야 하는 것은 아니다.(메서드나 생성자의 매개변수로 넘기는 행위가 그 객체의 통제권을 명백히 이전함을 뜻하기도 한다)

### 정리

클래스가 클라이언트로 받은 혹은 클라이언트로 반환하는 구성요소가 가변이라면 그 요소는 반드시 방어적으로 복사해야한다.

복사 비용이 너무 크거나 클라이언트가 요소를 잘못 수정할 일이 없음을 신뢰한다면 방어적 복사 대신 책임이 클라이언트에게 있음을 문서에 명시하도록 하자