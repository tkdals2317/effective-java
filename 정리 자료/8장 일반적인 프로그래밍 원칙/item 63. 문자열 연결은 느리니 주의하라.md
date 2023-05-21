    # item 63. 문자열 연결은 느리니 주의하라

<aside>
💡 문자열 연결 대신 StringBuider를 사용해야 하는 이유를 알아보자

</aside>

### 문자열 연결

문자열 연결 연산자(+)는 여러 문자열을 하나로 합쳐주는 편리한 수단이다.

한 줄 짜리 출력값 혹은 작고 크기가 고정된 객체의 문자열 표현을 만들 때는 괜찮지만, 본격적으로 사용하기 시작하면 성능 저하 이슈가 있다.

**문자열 연결 연산자로 문자열 n개를 잇는 시간은 n^2에 비례한다.**

문자열은 불변이라서 두 문자열을 연결할 경우 양쪽의 내용을 모두 복사하기 때문이다.

```java
// 코드 63-1 문자열 연결을 잘못 사용한 예 - 느리다!
public static String statement() {
    long beforeTime = System.currentTimeMillis();
    String result = "";
    for (int i = 0; i < numItems(); i++) {
        result += lineForItem(i);
    }

    return result;
}
```

### StringBuilder

성능을 생각한다면 String 대신 StringBuilder를 사용하자.

```java
// 코드 63-2 StringBuilder를 사용하면 문자열 연결 성능이 크게 개선된다.
public static String statement2() {
    StringBuilder sb = new StringBuilder(numItems() * LINE_WIDTH);
    for (int i = 0; i < numItems(); i ++) {
        sb.append(lineForItem(i));
    }
    return sb.toString();
}
```

두 메서드의 성능 측정

80자짜리 1000개의 문자열을 이어붙히는 테스트를 했을 때 성능 차이가 많이 나는 것을 확인 할  수 있었다.

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/2edb0ee6-bec3-4568-bcc1-4fdefa34c7a2)

statement 메서드는 String의 수의 제곱이 비례하여 늘어나고 statement2는 선형으로 늘어나므로 문자열이 많으면 많을수록 성능 격차도 벌어진다.

StringBuuilder를 전체 결과를 담기에 충분한 크기로 초기화한 점도 성능에 도움이 된다.

**StringBuilder 생성자**

****생성자 1. StringBuilder()****

매개변수가 없는 StringBuilder() 생성자 함수는 StringBuilder 객체의 초기 용량을 16Byte로 설정된다.

****생성자 2. StringBuilder(int capacity)****

int 타입의 값을 StrintBuilder 생성자 함수에 전달하면, StringBuilder 객체의 초기 용량을 설정할 수 있다.

****생성자 3. StringBuilder(CharSequence seq)****

String 대신 CharSequence를 StringBuilder 생성자 함수에 전달할 수 있다.

CharSequence를 StringBuilder 생성자 함수에 전달하는 경우 StringBuilder 객체의 초기 용량은 (매개변수로 전달된 CharSequence의 길이 + 16)이 된다.

****생성자 4. StringBuilder(String str)****

StringBuilder 생성자 함수에 문자열을 전달할 수 있다.

String을 StringBuilder 생성자 함수에 전달하는 경우 StringBuilder 객체의 초기 용량은 (매개변수로 전달된 String의 길이 + 16)이 된다.

### 정리

String 문자열 연결은 성능 상 이슈가 있으니 많은 양의 String 데이터를 연결할 경우는 StringBuilder를 사용하자