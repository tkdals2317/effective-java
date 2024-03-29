# item 72. 표준 예외를 사용하라

<aside>
💡 표준 예외를 재사용했을 때 장점과 각 상황별 사용해야 할 표준 예외에 대해 알아보자

</aside>

### 표준 예외를 재사용하면 좋은 점

- 자바 라이브러리는 대부분 API에서 쓰기에 충분한 수의 예외를 제공한다.
- 이미 익숙해진 규약을 그대로 따르기 때문에 API가 사람이 익히고 사용하기 쉬워진다.
- 낯선 예외를 사용하지 않게 되어 읽기 쉽게 된다.
- 클래스 수가 적을 수록 메모리 사용량도 줄고 클래스를 적재하는 시간도 적게 걸린다.

### 널리 재사용되는 예외

| 예외 | 주요 쓰임 |
| --- | --- |
| IllegalArgumentException | 허용하지 않는 값이 인수로 건네졌을 때(null은 따로 NullPointerException으로 처리) |
| IllegalStateException | 객체가 메서드를 수행하기에 적절하지 않은 상태일 때 |
| NullPointerException | null을 허용하지 않는 메서드에 null 건네졌을 때 |
| IndexOutOfBoundsException | 인덱스 범위를 넘어섰을 때 |
| ConcurrentModificationException | 허용되지 않는 동시 수정이 발견되었을 때 |
| UnsupportedOperationException | 호출한 메서드를 지원하지 않을 때 |
| ArithmeticException, NumberFormatException | 복소수나 유리수를 다루는 객체를 다룰 때 |

상황에 부합한다면 항상 표준 예외를 사용하자. 예외의 이름뿐만 아니라 예외가 던져지는 맥락도 부합할 때만 재사용한다.

더 많은 정보를 제공하길 바란다면 표준 예외를 확장해도 좋다.

단, 예외는 직렬화할 수 있으므로 직렬화에 대한 부담이 많아지므로 CustomException은 새로 만들지 말자

### Exception, RuntimeException, Throwable, Error는 직접 재사용하지 말자

이 클래스들은 추상 클래스로 생각하자.

이 예외들은 다른 예외들의 상위 클래스이므로, 여러 성격의 예외들을 포괄하는 클래스이므로 안정적으로 테스트할 수 없다.

### 정리

표준 예외를 재사용하면 여러 이점이 있으니 표준 예외를 사용하자

Exception, RuntimeException, Throwable, Error는 직접 재사용하지 말자