# item 35. ordinal 메서드 대신 인스턴스 필드를 사용하라

<aside>
💡 열거 타입에서 제공하는 ordinal 메서드 대신 인스턴스 필드를 사용해야하는 이유를 알아보자

</aside>

### ordinal 메서드

대부분의 열거 타입 상수는 자연스럽게 하나의 정숫값에 대응된다.

그리고 모든 열거 타입은 해당 상수가 그 열거 타입에서 몇 번째 위치인지를 반환하는 ordinal이라는 메서드를 제공한다.

### ordinal을 잘못 사용한 예

다음 코드는 합주단의 종류를 연주자가 1명이 솔로부터 10명이 디텍트까지 정의한 열거 타입이다.

```java
// 코드 35-1 ordinal을 잘못 사용한 예 - 따라하지 말 것
public enum Ensemble {
    SOLO, DUET, TRIO, QUARTET, QUINTET,
    SEXTET, SEPTET, OCTET, NONET, DECTET;

    public int numberOfMusicians() {
        return ordinal() + 1;
    }

    public static void main(String[] args) {
        for (Ensemble ensemble : Ensemble.values()) {
            System.out.println(ensemble.numberOfMusicians());
        }
    }
}
```

열거 타입 상수와 연결된 정수값이 필요하면 ordinal 메서드를 사용하고 싶은 유혹에 빠진다.

위 코드는 정상적으로 동작하지만 유지보수하기가 끔찍한 코드이다.

**위 코드의 단점**

- 상수의 선언을 바꾸는 순간 numberOfMusicians가 오동작한다.
- 이미 사용중인 정수와 같은 상수는 추가할 방법이 없다. ex) 똑같이 8명이 연주하는 복 4중주는 추가할 수 없다.
- 중간의 값도 비워둘수 없고 더미값을 사용하면 코드가 깔끔하지도 않을 뿐더러 실용적이지 못하다 ex) 12명이 연주하는 3중 4중주를 추가할 때 11명으로 구성된 연주가 없으므로 더미 상수를 추가해야 된다.

### 해결책 : 인스턴스 필드를 사용하자

열거 타입 상수에 연결된 값은 ordinal 메서드로 얻지말고 인스턴스 필드에 저장하자

```java
public enum EnsembleV2 {
    SOLO(1), DUET(2), TRIO(3), QUARTET(4),
    QUINTET(5), SEXTET(6), SEPTET(7), OCTET(8),
    DOUBLE_QUARTET(8), NONET(9), DECTET(10),
    TRIPLE_QUARTET(12);

    private final int numberOfMusicians;

    EnsembleV2(int numberOfMusicians) {
        this.numberOfMusicians = numberOfMusicians;
    }

    public int numberOfMusicians() {
        return numberOfMusicians;
    }

    public static void main(String[] args) {
        for (EnsembleV2 ensemble : EnsembleV2.values()) {
            System.out.println(ensemble.numberOfMusicians());
        }
    }
}
```

### 정리

ordinal 메서드는 대부분의 프로그래머가 이 메서드는 사용할 일이 없다고 API 문서에 적혀있다.

이 메서드는 EnumSet이나 EnumMap 같이 열거타입 기반의 범용 자료구조에 쓸 목적으로 설계되었다고 하니 이런 용도가 아니면 사용하지 말자