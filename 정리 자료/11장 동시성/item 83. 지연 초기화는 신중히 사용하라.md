# item 83. 지연 초기화는 신중히 사용하라

<aside>
💡 지연초기화에 대해 알아보고 멀티 스레드 환경에서 안전하게 지연 초기화하는 방법을 알아보자

</aside>

### 지연초기화란?

지연 초기화(lazy initialization)는 필드의 초기화 시점을 그 값이 처음 필요할 때까지 늦추는 기법이다.

그래서 값이 전혀 쓰이지 않으면 초기화도 결코 일어나지 않는다.

이 기법은 정적 필드와 인스턴스 필드 모두에 사용할 수 있다.

주로 최적화 용도로 쓰이지만, 클래스와 인스턴스 초기화 때 발생하는 위험한 순환 문제를 해결하는 효과도 있다.

### 필요할 때까지는 지연 초기화를 하지말라

클래스 혹은 인스턴스 생성 시의 초기화 비용은 줄지만 그 대신 지연 초기화하는 필드에 접근하는 비용이 커진다.

지연 초기화하려는 필드들 중 결국 초기화가 이뤄지는 비율에 따라, 실제 초기화에 드는 비용에 따라, 초기화된 각 필드가 얼마나 빈번히 호출하느냐에 따라 지연 초기화가 성능을 느리게 만들 수 있다.

멀티스레드 환경에서는 지연초기화를 하기가 까다롭다.

지연 초기화하는 필드를 둘 이상의 스레드가 공유한다면 어떤 형태로든 반드시 동기화해야 하지 않으면 심각한 버그로 이어진다.

**대부분의 상황에서 일반적인 초기화가 지연 초기화보다 낫다.**

```java
// 코드 83-1 인스턴스 필드를 초기화하는 일반적인 방법
private final FieldType field = computeFieldValue();
```

### 스레드 안전한 지연초기화 기법

1. **지연 초기화가 초기화 순환성(initialization circularity)을 깨뜨릴 것 같으면 synchronized를 단 접근자를 사용하자.**

```java
// 코드 83-2 인스턴스 필드의 지연 초기화 - synchronized 접근자 방식
private FieldType field;

private synchronized FieldType getField(){
    if (field == null)
        field = computeFieldValue();
    return field;
}
```

일반 초기화와 위의 지연초기화는 정적 필드에도 똑같이 적용된다.

물론 필드와 접근자 메서드 선언에 static 한정자를 추가해야 한다.

1. **성능 때문에 정적 필드를 지연 초기화해야 한다면 지연 초기화 홀더 클래스(lazy initialization holder class) 관용구를 사용하자**

클래스는 클래스가 처음 쓰일 때 비로소 초기화된다는 특성을 이용한 관용구이다.

```java
// 코드 83-3 정적 필드용 지연 초기화 홀더 클래스 관용구
private static class FiledHolder {
  static final FieldType field = computeFieldValue();
}

private static FieldType getField() {return FiledHolder.field;}
```

getFiled가 처음 호출되는 순간 FieldHolder.field가 처음 읽히면서, 비로소 FieldHolder 클래스 초기화를 촉발한다. 

이 관용구의 멋진 점은 getField 메서드가 필드에 접근하면서 동기화를 전혀 하지 않으니 성능이 느려질 거리가 전혀 없다는 것이다.

일반적인 VM은 오직 클래스를 초기화할 때만 필드 접근을 동기화할 것이다.

클래스 초기화가 끝난 후에는 VM이 동기화 코드를 제거하여, 그 다음부터는 아무런 검사나 동기화 없이 필드에 접근하게 된다.

1. **성능 때문에 인스턴스 필드를 지연초기화 해야 한다면 이중 검사(double check) 관용구를 사용하라**

이 관용구는 초기화된 필드에 접근할 때 동기화 비용을 없애준다.

필드의 값을 두번 검사하는 방식으로, 한 번은 동기화 없이 검사하고, (필드가 아직 초기화 되지 않았다면) 두 번째는 동기화하여 검사한다.

두 번째 검사에서 필드가 초기화되지 않았을 때만 동기화하여 검사한다.

필드가 초기화 된 이후로는 동기화하지 않으므로 해당 필드는 반드시 volatile로 선언해야 한다.

```java
// 코드 83-4 인스턴스 필드 지연 초기화용 이중검사 관용구
private volatile FieldType field;

private FieldType getField() {
    FieldType result = field;
    if (result != null) { // 첫 번째 검사 (락 사용 안함)
        return result;
    }
    
    synchronized (this) {
        if (field == null) { // 두 번째 검사 (락 사용)
            field = computeFieldValue();
        }
        return field;
    }
}
```

**result라는 지역 변수가 필요한 이유?**

이 변수는 필드가 이미 초기화된 상황(일반적인 상황)에서는 그 필드를 딱 한번만 읽도록 보장하는 역할을 한다.

반드시 필요하지는 않지만 성능을 높여주고, 저수준 동시성 프로그래밍에 표준적으로 적용되는 우아한 방법이다.

이중검산를 정적필드에도 적용할 수 있지만 지연 초기화 홀더 클래스 방식이 더 낫다.

3-1) **반복해서 초기화해도 상관없는 인스턴스 필드를 지연 초기화해야하는 경우는 단일 검사(single check)를 사용하라**

반복해서 초기화해도 상관없는 인스턴스 필드를 지연 초기화해야 하는 경우 두 번째 검사를 생략할 수 있다.

이 변종의 이름은 자연히 단일 검사(single check) 관용구가 된다.

```java
// 코드 83-5 단일검사 관용구 - 초기화가 중복해서 일어날 수 있다.
private volatile FieldType field;

private FieldType getField() {
    FieldType result = field;
    if (result == null) {
        field = result = computeFieldValue();
    }
    return field;
}
```

이중 검사와 단일검사 관용구를 수치 기본 타입 필드에 적용한다면 필드의 값을 null 대신 0과 비교하면된다.

3-2) **모든 스레드가 필드의 값을 다시 계산해도 상관 없고 필드의 타입이 long과 double을 제외한 기본타입이라면, 단일검사 필드에서 volatile 한정자를 없애도 된다.**

이런 변종을 짜릿한 단일 검사(racy single check) 관용구라 부른다.

이 관용구는 어떤 환경에서는 필드 접근 속도를 높여주지만, 초기화가 스레드당 최대 한번 더 이뤄질 수 있다.

아주 이례적인 기법으로 보통은 쓰지 않는다.

### 정리

대부분의 필드는 지연시키지 말고 곧바로 초기화해야한다.

성능 때문에 혹은 위험한 초기화 순환을 막기 위해 꼭 지연 초기화를 써야한다면 위에 설명한 상황에 맞는 관용구를 사용하자!