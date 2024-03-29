# item 77. 예외를 무시하지 말라

### 예외를 무시하는 것은 예외의 존재 의미를 없애는 것과 같다

```java
// catch 블록을 비워두면 예외가 무시된다. 아주 의심스러운 코드이다.
try {

} catch (SomeException e) {

}
```

위 코드처럼 예외를 무시하는 코드를 작성하지 말자.

API 설계자가 메서드 선언에 예외를 명시하는 까닭은, 그 메서드를 사용할 때 적절한 조치를 취해달라고 말하는 것이다.

**위 코드와 같이 catch 블록을 비워두면 예외가 존재할 이유가 없어진다.**

### 예외를 무시해야 되는 경우

FileInputStream을 닫을 때 입력 전용 스트림이기에 파일의 상태를 변경하지 않았으니 복구할 것이 없으며, 스트림을 닫는 다는 것은 필요한 정보는 다 읽었다는 뜻이니 남은 작업을 중단할 이유도 없다.

대신 그렇다고 아무 조치를 안하기보다 파일을 닫지 못했다는 사실을 로그로 남기는 것이 좋다.

**예외를 무시하기로 했다면 catch 블록 안에 그렇게 결정한 이유를 주석으로 남기고 예외 변수의 이름도 ignored로 바꿔놓도록 하자**

```java
Future<Integer> f = exec.submit(planarMap::chromaticNumber);
int numColors = 4; // 기본값, 어떤 지도라도 이 값이면 충분하다.
try {
    numColors = f.get(1L, TimeUnit.SECONDS);
} catch (TimeoutException | ExcutionException ignored) {
    // 기본 값을 사용한다.(색상 수를 최소화하면 좋지만, 필수는 아니다)
}
```

 

이번 아이템의 내용은 검사와 비검사 예외에 똑같이 적용된다.

예측할 수 있는 예외 상황이든 프로그래밍 오류든, 빈 catch 블록으로 못 본 척 지나치면 프로그램은 오류를 내재한 채 동작하게 된다.

그러다 어느 순간 문제의 원인과 아무 상관없는 곳에서 갑자기 죽어버릴 수도 있다.

### 정리

예외를 적절히 처리하면 오류를 완전히 피할 수 있다.

무시하지 않고 바깥으로 전파되게만 놔둬도 최소한의 디버깅 정보를 남긴 채 프로그램이 신속히 중단되게 할 수 있다.