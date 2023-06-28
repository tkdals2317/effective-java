# item 81. wait와 notify보다는 동시성 유틸리티를 애용하라

<aside>
💡 wait와 notify가 무엇인지 알아보고 동시성 유틸리티를 사용하는 것을 추천하는 이유를 알아보자

</aside>

### wait과 notify란?

wait 메서드와 notify 메서드는 스레드 간의 동기화와 통신을 위해 사용된다.

이 메서드들은 `Object` 클래스에 정의되어 있으며, 동기화된 블록 내에서 호출되어야 한다.

**wait() 메서드**

현재 실행중인 스레드를 일시적으로 중지시키고, 다른 스레드가 특정 조건을 만족할 때까지 기다리도록 한다.

이 메서드를 호출하면 스레드는 객체의 모니터링 락(lock)을 해제하고 대기 상태로 들어간다.

```java
synchronized (object) {
	while (condition) {
			object.wait();
	}
	// 조건을 만족한 후 실행될 코드
}
```

condition이 만족되지 않으면 스레드는 wait() 메서드를 호출하여 대기상태로 들어간다.

다른 스레드에서 notify() 메서드 또는 notifyAll() 메서드를 호출하여 조건이 만족되었다는 신호를 보내면, 대기 중인 스레드 중 하나가 실행을 재개합니다.

**notify() 메서드**

wait() 메서드에 의해 일시중지된 스레드 중 하나를 깨워 실행을 재개 시키는 메서드다.

호출되는 메서드는 객체의 모니터링 락을 얻어야 하므로, `synchronized`  블록 내에서 호출되어야 한다.

notify() 메서드는 임의의 스레드를 깨우므로 어떤 스레드가 깨어날지는 예측할 수 없다.

또한 깨어난 스레드가 실제로 실행될지 여부는 운영체제에 의존적이다.

**notifyAll() 메서드**

wait() 메서드에 의해 일시중지된 모든 스레드를 깨워 실행을 재개 시키는 메서드다.

notfiyAll() 메서드도 `synchronized` 블록 내에서 호출되어야하고 notify와 같이 어떤 스레드가 실제로 실행될지는 운영체제에 의존적이다.

### 고수준의 동시성 유틸리티 java.util.concurrent

java.util.concurrent의 고수준 유틸리티는 세 범주로 실행자 프레임워크, 동시성 컬렉션(concurrent collection), 동기화 장치(synchronizer) 나눌 수 있다.

실행자 프레임워크는 item 80에서 다루었기 때문에 생략하고 동시성 컬렉션과 동기화 장치에 대해 알아보자

**********동시성 컬렉션**********

List, Queue, Map과 같은 표준 컬렉션 인터페이스에 동시성을 가미해 구현한 고성능 컬렉션이다.

높은 동시성에 도달하기 위해 동기화를 각자의 내부에서 수행한다.

따라서 **동시성 컬렉션에서 동시성을 무력화하는 것은 불가능하며, 외부에서 락을 추가로 사용하면 오히려 속도가 느려진다.**

동시성을 무력화하지 못하므로 여러 메서드를 원자적으로 묶어 호출하는 것은 불가능하다.

그래서 여러 기본 동작을 하나의 원자적으로 묶는 ‘상태 의존적 수정’ 메서드들이 추가되었다.

이 메서드들은 매우 유요해서 자바 8에서는 일반 컬렉션 인터페이스에도 디폴트 메서드 형태로 추가되었다.

예를 들어 Map의 putIfAbsent(key, value) 메서드는 주어진 키에 매핑된 값이 아직 없을 때만 새값을 집어넣는다.

기존값이 있다면 그대로 반환하고, 없었다면 null을 반환한다.

이 메서드 덕에 스레드 안전한 정규화 맵(cononicalizing map)을 쉽게 구현할 수 있다.

String.intern의 동작을 흉내내어 ConcurrentMap으로 구현한 메서드를 살펴보자

intern 메서드에 대해서는 아래에서 확인하자

[String.intern() 이란? 언제 사용하는가?](https://simple-ing.tistory.com/3)

```java
private static final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

// 코드 81-1 ConcurrentMap으로 구현한 동시성 정규화 맵 - 최적은 아니다.
public static String internV1(String s) {
    String previousValue = map.putIfAbsent(s, s);
    return previousValue == null ? s : previousValue;
}
```

ConcurrentHashMap은 get 같은 검색 기능에 최적화 되어있기 때문에 get을 먼저 호출하여 필요할 때만 putIfAbsent를 호출하여 더 빠르게 최적화 해보자

```java
// 코드 81-2 ConcurrentMap으로 구현한 동시성 정규화 맵 - 더 빠르다.
public static String internV2(String s) {
    String result = map.get(s);
    if (result == null) {
        result = map.putIfAbsent(s, s);
        if (result == null) {
            result = s;
        }
    }
    return result;
}
```

ConcurrentHashMap은 동시성이 뛰어나며 속도도 빠르다.

동시성 컬렉션은 동기화한 컬렉션보다 나으니 동시성 컬렉션을 사용하도록 하자.

Collections.synchronizedMap보다는 ConcurrentHashMap을 사용하는 게 훨씬 좋다.

컬렉션 인터페이스 일부는 작업이 성공적으로 완료될 때까지 기다리도록 확장되었다.

예를 들어 BlockingQueue에 추가된 메서드 중 take는 큐의 원소를 꺼낸다.

이때 만약 큐가 비었다면 새로운 원소가 추가될 때까지 기다린다.

이런 특성 덕에 BlockingQueue는 작업 큐(생산자-소비자 큐)로 쓰기에 적합하다.

작업 큐는 하나 이상의 생산자(producer) 스레드가 작업(work)을 큐에 추가하고, 하나 이상의 소비자(consumer) 스레드가 큐에 있는 작업을 꺼내 처리하는 형태이다.

ThreadPoolExcutor를 포함한 대부분의 실행자 서비스(아이템 80) 구현체에서 이 BlockingQueue를 사용한다.

### 동기화장치

동기화 장치는 스레드가 다른 스레드를 기다릴 수 있게 하여, 서로 작업을 조율할 수 있게 해준다.

가장 자주 쓰이는 동기화 장치는 CountDownLatch와 Semaphore이다.

CyclicBarrier와 Exchanger는 그보다 덜 쓰인다.

**CountDownLatch**

카운트다운 래치는 일회성 장벽으로, 하나 이상의 스레드가 또 다른 하나 이상의 스레드 작업이 끝날 때까지 기다리게 한다.

CountDownLatch의 유일한 생성자는 int 값을 받으며, 이 값이 래치의 countDown 메서드를 몇번 호출해야 대기중인 스레드들을 깨우는 지를 결정한다.

어떤 동작들을 동시에 시작해 모두 완료하기까지의 시간을 재는 간단한 프레임워크를 구축한다고 해보자.

1. 이 프레임워크는 메서드 하나로 구성되며, 이 메서드는 동작들을 실행할 실행자와 동작을 몇개나 수행할 수 있는지를 뜻하는 동시성 수준(concurrency)을 매개변수로 받는다.
2. 타이머 스레드가 시계를 시작하기 전에 모든 작업자 스레드는 동작을 수행할 준비를 마친다.
3. 마지막 작업자 스레드가 준비를 마치면 타이머 스레드가 ‘시작 방아쇠’를 당겨 작업자 스레드들이 일을 시작하게 한다.
4. 마지막 작업자 스레드가 동작을 마치자마자 타이머 스레드는 시계를 멈춘다.

이상의 기능을 wait과 notfiy만으로 구현하려면 아주 난해하고 지저분한 코드가 탄생하지만, CountDownLatch를 놀랍도록 직관적으로 구현할 수 있다.

```java
// 코드 81-3 동시 실행 시간을 재는 간단한 프레임워크
public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
    CountDownLatch ready = new CountDownLatch(concurrency);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(concurrency);

    for (int i = 0; i < concurrency; i++) {
        executor.execute(()->{
            // 타이머에게 준비를 마쳤음을 알린다.
            ready.countDown();
            try {
                // 모든 작업자 스레드가 준비될 때까지 기다린다.
                start.await();
                action.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // 타이머에게 작업을 마쳤음을 알린다.
                done.countDown();
            }
        });
    }

    ready.await(); // 모든 작업자가 준비될 때까지 기다린다.
    long startNanos = System.nanoTime();
    start.countDown(); // 작업자들을 깨운다.
    done.await(); // 모든 작업자가 일을 끝마치기를 기다린다.
    return System.nanoTime() - startNanos;
}
```

위 코드는 카운트 다운 래치를 3개 사용한다.

1. ready 래치는 작업자 스레드들이 준비가 완료됐음을 타이머 스레드에 통지할 때 사용한다.
2. 통지를 끝낸 작업자 스레드들은 두번째 래치인 start가 열리기를 기다린다.
3. 마지막 작업자 스레드가 ready.countDown을 호출하면 타이머 스레드가 시작 시각을 기록하고 start.countDown을 호출하여 기다리던 작업자 스레드를 깨운다.
4. 그 직후 타이머 스레드는 세 번째 래치인 done이 열리기를 기다린다.
5. done 래치는 마지막 남은 작업자 스레드가 동작을 마치고 done.countdown을 호출하면 열린다. 
6. 타이머 스레드는 done 래치가 열리자마자 깨어나 종료 시각을 기록한다.

**그 외 세부사항**

time 메서드에 넘겨진 실행자(executor)는 concurrency 매개변수로 지정한 동시성 수준만큼의 스레드를 생성할 수 있어야 한다.

⇒ 안그러면 메서드는 스레드 기아 교착상태로 메서드는 끝나지 않는다.

InterruptedException을 캐치한 작업자 스레드는 Thread.currentThread().interupt() 관용구를 사용해 인터럽트(interupt)를 되살리고 자신은 run 메서드에서 빠져나온다.

System.nanoTime 메서드를 사용해 시간을 잰 것에 주목하자.

**시간 간격을 잴 때는 항상 System.currentTimeMillis가 아닌 System.nanoTime을 사용하자.**

System.nanoTime은 더 정확하고 정밀하며 시스템의 실시간 시계의 시간 보정에 영향받지 않는다.

### wait와 notify를 사용해야만 할 때 주의점

wait 메서드는 스레드가 어떤 조건이 충족되기를 기다리게 할 때 사용한다.

락 객체의 wait 메서드는 반드시 그 객체를 잠근 동기화 영역 안에서 호출해야한다.

wait를 사용하는 표준 방식은 다음과 같다.

```java
// 코드 81-4 wait 메서드를 사용하는 표준 방식
synchronized (object){
    while (<조건이 충족되지 않았다>)
        object.wait(); // (락을 놓고, 깨어나면 다시 잡는다.)

    ... // 조건이 충족되었을 떄의 동작을 수행한다.
}
```

**wait 메서드를 사용할 때는 반드시 대기 반복문(wait loop) 관용구를 사용하라. 반복문 밖에서는 절대 호출하지 말자**

이 반복문은 wait 호출 전후로 조건이 만족하는지를 검사하는 역할을 한다.

대기 전에 조건을 검사하여 조건이 이미 충족되었다면 wait를 건너뛰게 한 것은 응답 불가 상태를 예방하는 조치다.

만약 조건이 이미 충족되었는데 스레드가 notitfy(혹은 notifyAll) 메서드를 먼저 호출한 후 대기 상태로 빠지면 스레드를 다시 깨울 수 있다고 보장할 수 없다.

한편, 대기 후에 조건을 검사하여 조건이 충족되지 않았다면 다시 대기하게 하는 것은 안전 실패를 막는 조치이다.

만약 조건이 충족되지 않았는데 스레드가 동작을 이어가면 락이 보호하는 불변식을 깨뜨릴 위험이 있다.

조건이 만족되지 않아도 스레드가 깨어날 수 있는 상황 몇가지에 대해 알아보자

- 스레드가 notify를 호출한 다음 대기중이던 스레드가 깨어나는 사이에 다른 스레드가 락을 얻어 그 락이 보호하는 상태를 변경한다.
- 조건이 만족되지 않았음에도 다른 스레드가 실수로 혹은 악의적으로 notfiy를 호출한다. 공개된 객체를 락으로 사용해 대기하는 클래스는 이런 위험에 노출된다. 외부에 노출된 객체의 도익화된 메서드 안에서 호출하는 wait는 모두 이 문제에 영향을 받는다.
- 깨우는 스레드는 지나치게 관대해서, 대기 중인 스레드 중 일부만 조건이 충족되어도 notifyAll을 호출해 모든 스레드를 깨울 수 있다.
- 대기 중인 스레드가 (드물게) notfify 없이도 깨어나는 경우가 있다. 허위 각성이라는 현상이다. (몽유병이여 뭐여,,?)

이와 관련하여 notify와 notifiyAll  중 무엇을 선택하느냐 문제도 있다.

일반적으로 언제나 notifyAll을 사용하라는 게 합리적이고 안전한 조언이 될 것이다.

⇒ 깨어나야 하는 모든 스레드가 깨어남을 보장하니 항상 정확한 결과를 얻을 것이다.

⇒ 다른 스레드가 깨어날 수 있긴 하지만, 정확성에 영향을 주지는 않는다.

⇒ 깨어난 스레드들은 기다리던 조건이 충족되었는지 확인하여, 충족되지 않으면 다시 대기할 것이다.

모든 스레드가 같은 조건을 기다리고, 조건이 한번 충족될때마다 단 하나의 스레드만 혜택을 받을 수 있다면 notifyAll 대신 notify를 사용해 최적화할 수 있다.

또 외부로 공개된 객체에 대해 실수로 혹은 악의적으로 notify를 호출하는 상황을 대비하기 위해 wait을 반복문 안에서 호출 했듯, notify 대신 notifyAll을 사용하면 관련없는 스레드가 실수로 혹은 악의적으로 wait를 호출하는 공격으로 부터 보호 할 수 있다.

### 정리

- wait과 notify를 직접 사용하는 것을 동시성 ‘어셈블리 언어’로 프로그래밍하는 것에 비유할 수 있다.
- 반면 java.util.concurrent는 고수준 언어에 비유할 수 있다. 코드를 새로 작성한다면 wait와 notify를 쓸 이유가 없다.
- 이들을 사용하는 레거시 코드를 유지보수해야 한다면 wait는 항상 표준 관용구에 따라 while문 안에서 호출하도록 하자.
- 일반적으로 notify보다는 notifyAll을 사용해야 한다. 혹시라도 notify를 사용한다면 응답 불가 상태에 빠지지 않도록 각별히 주의하자