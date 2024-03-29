# item 70. 복구할 수 있는 상황에는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라

<aside>
💡 각 예외를 적재적소에 사용하는 법을 알아보자

</aside>

### 자바의 문제 상황을 알리는 타입(throwable) 3가지

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/12a52dd6-61e3-458c-9a45-d2039fd3c4ce)
[https://devlog-wjdrbs96.tistory.com/351](https://devlog-wjdrbs96.tistory.com/351)

- 검사 예외(Checked Exception)
    - 컴파일러에 의해 확인되는 예외로, 메소드를 호출할 때 반드시 예외 처리를 해주어야한다.
    - 주로 파일 입출력, 네트워크 연결 등과 같이 외부 자원에 대한 작업에서 발생
    - IOException, SQLException
- 런타임 예외(Unchecked Exception)
    - 컴파일 단계에서 확인되지 않으며, 실행 시간에 발생하는 예외
    - 컴파일러가 예외 처리를 강제하지 않기 때문에, 프로그래머의 실수나 논리적 오류에 의해 발생할 수있음
    - NullPointerException, ArrayIndexOutOfBoundsException
- 에러(Error)
    - 시스템 레벨에서 발생하는 예외로, 프로그래머가 직접적으로 대처하기 어려우므로 일반적으로는 예외 처리를 하지 않고, 시스템 자체에서 대처할 수 있도록 하게 함
    - OutOfMemoryError, StackOverflowError

### 호출하는 쪽에서 복구하리라 여겨지는 상황이라면 검사 예외를 사용하라

이것이 검사와 비검사 예외를 구분하는 기본 규칙이다.

예외를 잡기만 하고 별다른 조치를 취하지 않을 수 있지만 좋지 않은 방법이다.

### 프로그래밍 오류를 나타날 때는 런타임 예외를 사용하자

런타임 예외는 전제조건을 만족하지 못했을 때(클라이언트가 API의 명세에 기록된 제약을 지키지 못하였을 때) 발생한다.

복구가능한 상황이라고 믿는 다면 검사 예외를, 그렇지 않다면 런타임 예외를 사용하자.

확신할 수 없는 상황이라면 비검사 예외를 선택하는 편이 낫다.

### 직접 구현하는 비검사 throwable은 모두 RuntimeException의 하위 클래스여야 한다.

Error 클래스를 상속해 하위 클래스를 만드는 일은 자제하자.

### throwable의 직접 사용은 최대한 사용하지 말자

정상적인 검사 예외보다 나을 게 하나도 없다.

예외의 메서드는 주로 그 예외를 일으킨 상황에 관한 정보를 코드 형태로 전달하는 데 쓰인다.

이런 메서드가 없다면 오류 메시지를 파싱해 정보를 빼내야 하는데, 대단히 나쁜 습관이다.

throwable 클래스들은 대부분 오류 메시지 포맷을 상세히 기술하지 않는데, 이는 JVM이나 릴리스에 따라 포맷이 달라질 수 있다는 뜻이다.

따라서 메시지 문자열을 파싱해 얻은 코드는 깨지기 쉽고 다른 환경에서 동작하지 않을 수 있다.

### 정리

- 복구할 수 있는 상황이면 검사 예외를 던지자
- 프로그래밍 오류나 확실하지 않다면 비검사 예외를 던지자
- 검사예외도 아니고 런타임 예외도 아닌 throwable은 정의하지도 말자
- 검사 예외라면 복구에 필요한 정보를 알려주는 메서드도 제공하자