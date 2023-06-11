# item 53. 가변인수는 신중히 사용하라


### 가변인수

가변인수(varargs) 메서드는 명시한 타입의 인수를 0개 이상 받을 수 있다.

가변인수 메서드를 호출하면, 가장 먼저 인수의 개수와 길이가 같은 배열을 만들고 인수들을 이 배열에 저장하여 가변인수 메서드에 건네준다.

간단한 가변인수 메서드 예제를 보자

```java
// 코드 53-1 간단한 가변인수 활용 예
static int sum(int... args) {
    int sum = 0;
    for (int arg : args) {
        sum += arg;
    }
    return sum;
}
```

인수가 1개 이상이어야 할 때도 있다.

최솟값을 찾는 메서드인데 인수를 0개만 받을 수 있도록 설계하는 것은 좋지 않다.

```java
// 코드 53-2 인수가 1개 이상이어야 하는 가변인수 메서드 - 잘못 구현한 예
static int min(int... args) {
  if (args.length == 0) {
      throw new IllegalArgumentException("인수가 1개 이상 필요합니다."); // 런타임에야 배열의 길이로 알 수 있다 -> 좋지 못함
  }
  int min = args[0];
  for (int i = 1; i < args.length; i++) {
      if (args[i] < min) {
          min = args[i];
      }
  }
  return min;
}
```

인수를 0개만 넣어 호출하면 컴파일이 아닌 런타임에 실패한다.

코드도 지저분하고 명료한 for-each문도 사용할 수 없다.

다음 코드 처럼 매개변수 2개를 받게 만들자, 첫 번째로 평범한 매개변수를 받고 두 번째로 가변인수로 받으면 된다.

```java
// 코드 53-3 인수가 1개 이상이어야 할 때 가변인수를 제대로 사용하는 방법
static int min(int firstArgs, int... remainArgs) {
    int min = firstArgs;
    for (int arg : remainArgs) {
        if (arg < min) {
            min = arg;
        }
    }
    return min;
}
```

### 성능이 민감한 경우라면 매개변수 4개 이상부터 가변인수를 사용하라

```java
// 성능을 생각한다면 인수 4개 이상 부터만 가변인수가 맡게 하자
public void foo()  {}
public void foo(int a1)  {}
public void foo(int a1, int a2)  {}
public void foo(int a1, int a2, int a3)  {}
public void foo(int a1, int a2, int a3, int... rest)  {}
```

메서드가 호출될 때마다 배열을 생성하고 초기화한다. 

이런 비용을 아끼고 대부분의 상황이 3개를 넘어가는 경우가 거의 없다보니 굳이 모든 매개변수를 가변인수로 받을 필요는 없다.

### 정리

인수 개수가 일정하지 않은 메서드를 정의해야 한다면 가변인수가 받드시 필요하다. 

메서드 정의할 때 필수 매개변수는 가변인수 앞에 두고, 가변인수를 사용할 때는 성능 문제까지 고려하자.