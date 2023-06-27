# item 80. 스레드보다는 실행자, 태스크, 스트림을 애용하라

<aside>
💡 스레드 대신 실행자 프레임워크를 활용하는 방법을 알아보자

</aside>

### java.util.concurrent 패키지의 등장

이 패키지는 실행자 프레임워크(Excutor Framework)라고 하는 인터페이스 기반의 유연한 태스크 실행 기능을 담고있다.

단 한줄로 작업큐를 생성할 수 있다.

```java
ExcutorService exec = Excutors.newSingleThreadExcutor();
```

다음은 실행자에 실행할 태스크(task)를 넘기는 방법이다.

```java
exec.execute(runnable);
```

다음으로 실행자를 우아하게 종료시키는 방법이다.

```java
exec.shutdown();
```

### 실행자 서비스의 주요 기능

- 특정 태스크가 완료되기를 기다린다 ⇒ 코드 79-2에서 본 get 메서드
- 태스크 모음 중 아무것 하나(invokeAny 메서드) 혹은 모든 태스크(invokeAll 메서드)가 완료되기를 기다린다.
- 실행자 서비스가 종료하기를 기다린다 ⇒ awaitTermination 메서드
- 완료된 태스크를의 결과를 차례를 받는다 ⇒ ExecutorCompletionService 이용
- 태스크를 특정 시간에 혹은 주기적으로 실행하게 한다 ⇒ ScheduledThreadPoolExecutor 이용

큐를 둘 이상의 스레드가 처리하고 싶다면 간단히 다른 정적 팩터리를 이용하여 다른 종료의 실행자 서비스(스레드 풀)를 생성하면 된다.

스레드 풀의 스레드 개수는 고정할 수도 있고 필요에 따라 늘어나거나 줄어들게 설정할 수 있다.

⇒ 필요한 실행자 대부분은 java.util.concurrent.Executors의 정적 팩터리를 이용해 만들 수 있다. 

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/93c67783-bd69-48ca-b74d-eb621635ae13)

평범하지 않은 실행자를 원한다면 ThreadPoolExcutor 클래스를 직접 사용해도 된다.

이 클래스로는 스레드 풀 동작을 결정하는 거의 모든 속성을 설정할 수 있다.

![Untitled 1](https://github.com/tkdals2317/effective-java/assets/49682056/c6340b0e-3819-4ebb-bc73-c5b5a02a2106)

작은 프로그램이나 가벼운 서버라면 Executors.newChachedThreadPool이 일반적으로 좋은 선택이다.

특별히 설정할 게 없고 일반적인 용도에 적합하게 동작한다.

대신 무거운 프로덕션 서버에는 적합하지 않은데 이유는 요청받은 태스크들이 큐에 쌓이지 않고 즉시 스레드에 위임돼 실행되고 가용한 스레드가 없다면 새로 하나를 생성하기 때문에 CPU를 많이 사용하게 되기 때문이다.

![Untitled 2](https://github.com/tkdals2317/effective-java/assets/49682056/034c406c-c863-4434-a400-3dbfd9504c6a)

무거운 프로덕션 서버에서는 스레드 개수를 고정한 Executors.newFiexedThreadPool을 선택하거나 완전히 통제할 수 있는 ThreadPoolExecutor를 직접 사용하는 편이 낫다.

![Untitled 3](https://github.com/tkdals2317/effective-java/assets/49682056/f77b6389-8af1-4d60-8753-8bb545c5a9a5)

스레드를 직접 사용하는 것은 최대한 삼가자

실행자 프레임워크는 스레드를 직접 다루는 것과 달리 작업 단위와 실행 매커니즘이 분리된다.

### 태스크

작업 단위를 나타내는 핵심 추상 개념이 태스크다

Runnable과 Callable이 태스크이다.

### 실행자 서비스

태스크를 수행하는 일반적인 매커니즘이 바로 실행자 서비스다.

태스크 수행을 실행자 서비스에 맡기면 원하는 태스크 수행 정책을 선택할 수 있고, 생각이 바뀌면 언제든 변경할 수 있다.

핵심은 실행자 프레임워크가 작업 수행을 담당해준다는 것이다.

### 포크-조인 태스크

자바 7이되면서 실행자 프레임워크는 포크-조인 태스크를 지원하도록 확장되었다.

포크-조인 태스크는 포크-조인 풀이라는 특별한 실행자 서비스가 실행해준다.

포크-조인 태스크, 즉 ForkJoinTask의 인스턴스는 작은 하위 태스크로 나뉠 수 있고, 

ForkJoinPool을 구성하는 스레드들이 이 태스크들을 처리하며, 일을 먼저 끝낸 스레드는 다른 스레드의 남은 태스크를 가져와 대신 처리할 수 있다.

⇒ 높은 처리량과 낮은 지연시간

### 정리

스레드를 직접 다루지말고 실행자 프레임워크를 활용해보자

현재 우리 프로젝트의 엑셀 다운로드 로직이 스레드로 동작하는 데 나중에 이것도 실행자를 통해 실행시키는 방법으로 리팩토링 해봐야겠다.