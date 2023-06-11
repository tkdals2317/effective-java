# item 73. 추상화 수준에 맞는 예외를 던지라

<aside>
💡 예외 번역, 예외 연쇄에 대해 알아보자

</aside>

메서드가 저수준 예외를 처리하지 않고 바깥으로 전파해버리면 내부 구현 방식이 노출되어 윗 레벨 API를 오염되게 된다.

다음 릴리스에서 구현 방식을 바꾸면 다른 예외가 튀어나와 기존 클라이언트 프로그램이 깨지게 할 수도 있다.

### 예외 번역(exception translation)

이 문제를 피하려면 **상위 계층에서는 저수준 예외를 잡아 자신의 추상화 수준에 맞는 예외로 바꿔 던져야한다.**

```java
// 코드 73-1 예외 번역
  try {
      //...
  } catch (LowerLevelException e) {
      throw new HigherLevelException();
  }
```

`AbstractSequentialList` get 메서드가 예외 번역의 예이다.

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/f13482ae-64b6-48e1-b983-45a390d6c402)

저수준 예외인 `NoSuchElementException` 이 발생 시 고수준 예외인 `IndexOutOfBoundsException` 로 예외를 번역하여 던진다.

### 예외 연쇄(exception chaining)

예외를 번역할 때, 저수준 예외가 디버깅에 도움이 된다면 예외 연쇄를 사용하는 게 좋다.

예외 연쇄란 문제의 근본 원인(cause)인 저수준 예외를 고수준 예외에 실어서 보내는 방식이다.

별도의 접근자 메서드(Throwable의 getCause 메서드)를 통해 언제든 저수준 예외를 꺼내 볼 수 있다.

```java
// 코드 73-2 예외 연쇄
try {
	...
} catch (LowerLevelException cause) {
    throw new HigherLevelException(cause);
}
```

고수준 예외의 생성자는 (예외 연쇄용으로 설계된) 상위 클래스의 생성자에 ‘원인’을 건네주어, 최종적으로는 Throwable 생성자까지 건네지게 한다.

```java
// 코드 73-3 예외 연쇄용 생성자
public class HigherLevelException extends RuntimeException{
    public HigherLevelException(LowerLevelException cause) {
        super(cause);
    }
}
```

대부분 표준 예외는 예외 연쇄용 생성자를 갖추고 있다.

그렇지 않은 예외라도 Throwable의 initCause 메서드를 이용해 ‘원인’을 직접 못박을 수 있다.

예외 연쇄는 문제의 원인을 (getCause 메서드로) 프로그램에서 접근 할 수 있게 해주며, 원인과 고수준 예외의 스택 추적 정보를 잘 통합해준다.

### 남용은 금물

무턱대고 예외를 전파하는 것보다야 예외 번역이 우수한 방법이지만, 가능하다면 저수준 메서드가 반드시 성공하도록 아래 계층에서는 예외가 발생하지 않도록 하는 것이 최선이다.

때론 상위 계층 메서드의 매개변수 값을 아래 계층 메서드로 건네기 전에 미리 검사하는 방법으로 이 목적을 달성할 수 있다.

### 전파 대신 로깅

차선책으로 아래 계층에서의 예외를 피할 수 없다면, 상위 계층에서는 그 예외를 조용히 처리하여 문제를 API 호출자에게까지 전파하지 않는 방법이 있다.

이 경우는 로깅을 남기는 방법으로 기록해두는 것이 좋다.

그렇게 하면 클라이언트 코드와 사용자에게 문제를 전파하지 않으면서도 프로그래머가 로그를 분석해 추가 조치를 취할 수 있게 해준다.

### 정리

아래 계층의 예외를 예방하거나 스스로 처리할 수 없고, 그 예외를 상위 계층에 그대로 노출하기 곤란하다면 예외 번역을 사용하라.

이때 예외 연쇄를 사용하면 상위 계층에는 맥락에 어울리는 고수준 예외를 던지면서 근본 원인도 함께 알려주어 오류를 분석하기에 좋다.