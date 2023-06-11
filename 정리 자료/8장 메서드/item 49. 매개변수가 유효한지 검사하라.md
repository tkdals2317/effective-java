# item 49. 매개변수가 유효한지 검사하라

<aside>
💡 매개변수의 유효성 체크 로직의 중요성을 알아보자

</aside>

### 오류는 가능한 한 빨리 잡아야 한다.

오류를 발생한 즉시 잡지 못하면 해당 오류를 감지하기 어려워지고, 감지하더라도 오류의 발생 지점을 찾기 어려워진다.

메서드 몸체가 실행되기 전에 매개변수를 확인한다면 잘못된 값이 넘어왔을 때 즉각적이고 깔끔한 방식으로 예외를 던질 수 있다.

매개변수 검사를 제대로 하지 못하면 생길 수 있는 문제점

1. 메서드가 수행되는 중간에 모호한 예외를 던지며 실패할 수 있다.
2. 메서드가 잘 수행되지만, 잘못된 결과를 반환 할 수 있다.
3. 2번과 같은 상황으로 해당 메서드와 관계 없는 다른 메서드에서 오류를 낼 수 있다.

### public과 protected 메서드는 매개변수 값이 잘못됐을 때 던지는 에외를 문서화해야 한다.

`@throws` 자바독 태그를 사용하자

보통 `IllegalArgumentException`, `IndexOutOfBoundsException`, `NullPointerException` 중 하나가 될 것이다.

매개변수의 제약을 문서화한다면 그 제약을 어겼을 때 발생하는 예외도 같이 기술해야 한다.

이런 간단한 방법으로 API 사용자가 제약을 지킬 가능성을 크게 높일 수 있다.

```java
/**
* (현재 값 mod m) 값을 반환한다. 이 메서드는 항상 음이 아닌 BigInteger를 반환한다는 점에서 remainder 메서드와 다르다.
*
* @param m m 계수(양수여야 한다.)
* @return 현재 값 mod m
* @throws  ArithmeticException m이 0보다 작거나 같으면 발생한다.
*/
public BigInteger mod(BigInteger m){
  if(m.signum() <= 0) {
      throw new ArithmeticException("계수(m)은 양수여야 합니다. " + m);
  }
  //... 계산 수행
}
```

m이 null 일 때 NullPointerException을 던지지만 문서화하지 않은 이유는 BigInteger 클래스 수준에서 기술했기 때문이다.

### 자바의 null 검사 기능

**자바 7에 추가된 java.util.Objects.requireNonNull 메서드는 유연하고 사용하기도 편하니, 더 이상 null 검사를 수동으로 하지 않아도 된다.**

원하는 예외 메시지도 지정할 수 있고, 입력을 그대로 반환하므로 값을 사용하는 동시에 null 검사를 수행할 수 있다. (반환 값을 사용하지 않고 순수한 null 검사 목적으로 사용해도 된다.)

![Untitled](https://user-images.githubusercontent.com/49682056/234017031-a8bab6e8-a06d-410e-aeff-d70aefe706d3.png)

```java
// 코드 49-1 자바의 null 검사 기능 사용하기
BigInteger bigInteger = Objects.requireNonNull(m, "m");
```

자바 9에서 추가된 Objects의 범위 검사 기능도 더해졌다.

`checkFromIndexSize`, `checkFromToIndex`, `checkIndex` 메서드인데, null 검사 메서드만큼 유연하지는 않다.

예외 메세지를 지정할 수 없고, 리스트와 배열 전용으로 설계되었다. 또한 닫힌 범위는 다루지 못한다.

![Untitled 1](https://user-images.githubusercontent.com/49682056/234017016-6a067b8f-1b29-4a00-be6f-64b96ac938ca.png)

위 메서드들은 `IndexOutOfBoundsException` 예외를 뱉는다.

### public이 아닌 메서드라면 단언문(assert)를 사용해 매개변수 유효성을 검증 할 수 있다.

```java
// 코드 49-2 재귀 정렬용 private 도우미 함수
private static void sort(long a[], int offset, int length) {
    assert a != null;
    assert offset >= 0 && offset <= a.length;
    assert length >= 0 && length <= a.length - offset;
    //...계산 수행
}
```

여기서 핵심은 이 단언문들은 자신이 단언한 조건이 무조건 참이라고 선언한다는 것이다.

단언문은 몇 가지 면에서 일반적인 유효성 검사와 다르다.

1. 실패하면 AssertionError를 던진다.
2. 런타임에 아무런 효과도, 아무런 성능 저하도 없다.(-ea 혹은—enableassetions 플래그를 설정 안할 시)

예제 소스 코드를 돌려보면서 처음에는 왜 assert가 false인데 아무런 예외도 안뱉지하고 의아했는데 vmoption에 -ea 플래그를 설정해주니 콘솔창에 AssertionError가 떴다.

![Untitled 2](https://user-images.githubusercontent.com/49682056/234017026-134f018b-3909-4d55-abaa-610116c5aee1.png)

![Untitled 3](https://user-images.githubusercontent.com/49682056/234017029-ad9c677c-7b31-468b-98e1-e5ebd6b09d36.png)

### 메서드가 직접 사용하지는 않으나 나중에 쓰기 위해 저장하는 매개변수는 더 신경써서 검사하자

입력받은 int 배열의 List 뷰를 반환하는 메서드를 보자

```java
// 코드 20-1 골격 구현 클래스 AbstractList
static List<Integer> intArrayAsList(int[] a) {
    Objects.requireNonNull(a);
    return new AbstractList<>() {
        @Override
        public Integer get(int i) {
            return a[i];
        }

        @Override
        public Integer set(int i, Integer val) {
            int oldVal = a[i];
            a[i] = val;
            return oldVal;
        }

        @Override
        public int size() {
            return a.length;
        }
    };
}
```

여기서 a의 null 검사를 하지 않는다면, 새로 생성한 List 인스턴스를 반환하고 클라이언트가 돌려받은 List를 사용하려 할 때 비로소 NullPointerException이 발생한다.

이때가 되면 List를 어디서 가져왔는 지 추적하기 어려워 디버깅이 어려워질 수 있다.

### 정리

메서드나 생성자를 작성할 때면 그 매개변수들에 어떤 제약이 있을지 생각해야한다.

그 제약들을 문서화하고 메서드 코드 시작 부분에서 명시적으로 검사해주자.