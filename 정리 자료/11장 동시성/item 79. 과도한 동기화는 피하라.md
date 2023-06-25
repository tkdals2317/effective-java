# item 79. 과도한 동기화는 피하라

<aside>
💡 과도한 동기화로부터 올 수 있는 문제점에 대해 알아보고 어떻게 그런 문제점을 회피할 수 있는 지에 대해 알아보자

</aside>

### 과도한 동기화로부터 올 수 있는 문제점

- 과도한 동기화는 성능을 떨어뜨린다.
- 교착상태에 빠트릴 수 있다.
- 예측할 수 없는 동작을 낳기도 한다.

### 응답 불가와 안전 실패를 피하려면 동기화 메서드나 동기화 블록 안에서는 제어를 절대로 클라이언트에 양도하면 안된다.

예를 들어 동기화된 영역 안에서는 재정의할 수 있는 메서드를 호출하면 안되며, 클라이언트가 넘겨준 함수 객체를 호출해서도 안된다.

동기화된 영역을 포함한 클래스 관점에서는 이런 메서드는 모두 바깥 세상에서 온 외계인이다.

외계인 메서드(alien method)가 하는 일에 따라 동기화된 영역은 예외를 일으키거나, 교착상태에 빠지거나, 데이터를 훼손할 수도 있다.

### 동기화 블록 안에서 외계인 메서드를 호출하는 ObservableSetV1

```java
// 코드 79-1 잘못된 코드. 동기화 블록 안에서 외계인 메서드를 호출한다.
public class ObservableSetV1<E> extends ForwardingSet<E> {

    public ObservableSetV1(Set<E> set) {
        super(set);
    }

    private final List<SetObserverV1<E>> observers = new ArrayList<>();

    public void addObserver(SetObserverV1<E> observer) {
        synchronized (observers){
            observers.add(observer); 
        }
    }

    public boolean removeObserver(SetObserverV1<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }

    private void notifyElementAdded(E element) {
        synchronized (observers) {
            for (SetObserverV1<E> observer : observers) {
                observer.added(this, element); 
            }
        }
    }

    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added) {
            notifyElementAdded(element);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for(E element : c) {
            result |= add(element); // notifyElementAdded를 호출한다.
        }
        return result;
    }
}
```

어떤 집합(Set)을 감싼 래퍼 클래스이고, 이 클래스의 클라이언트는 집합에 원소가 추가되면 알림을 받을 수 있다.(관찰자 패턴)

관찰자들은 addObserver와 removeObserver 메서드를 호출해 구독을 신청하거나 해지한다. 두 경우 모두 콜백 인터페이스의 인스턴스를 메서드에 건넨다.

```java
// 구조적으로 BiConsumer와 똑같다. 커스텀 인터페이스를 정의한 이유는 이름이 더 직관적이고 다중 콜백을 지원하도록 확장할 수 있어서이다.
@FunctionalInterface
public interface SetObserverV1<E> { 
    // ObservableSet 원소가 더해지면 호출된다.(여기서 외계인 메서드는 이 메서드에 해당한다)
    void added(ObservableSetV1<E> set, E element);
}
```

그렇다면 테스트를 해보자

```java
private static void observableSetV1Method1() {
        ObservableSetV1<Integer> setV1 = new ObservableSetV1<>(new HashSet<>());

        setV1.addObserver((s, e) -> System.out.println(e));

        for (int i = 0; i < 100; i++) {
            setV1.add(i);
        }
    }
```

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/1b42c31b-ff0f-41f4-9f9c-000bbef9f3c7)

정상적으로 구독이 되었고 추가될 때마다 값을 정상적으로 출력하는 것을 확인할 수 있다.

### `ConcurrentModificationException` 예외가 발생하는 상황

그렇다면 특정 조건에 구독을 해지하는 관찰자를 추가해보자

```java
private static void observableSetV1Method2() {
        ObservableSetV1<Integer> setV1 = new ObservableSetV1<>(new HashSet<>());
        setV1.addObserver(new SetObserverV1<>() {
            @Override
            public void added(ObservableSetV1<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    set.removeObserver(this);
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            setV1.add(i);
        }
        // 23번째에서 java.util.ConcurrentModificationException 발생
    }
```

위 메서드는 0부터 23까지 출력한 후 관찰자 자신을 구독해지한 다음 종료할 것으로 보이나 실제로는 `ConcurrentModificationException` 이 발생하고 터져버린다.

![Untitled 1](https://github.com/tkdals2317/effective-java/assets/49682056/936185c8-b919-48b8-96c0-c3a7c2a5b8fd)

관찰자의 added 메서드 호출이 일어난 시점이 notifyElementAdded가 관찰자들의 리스트를 순회하는 도중이기 때문이다.

1. add로 원소 추가
2. 구독한 사람들에게 알림을 알려야겠다(notifyElementAdded) 3.
3. 구독한 사람들 목록을 다 돌면서 알려야지(리스트 순회, 락이 잡힘)
4. 오잉? 동기화된 리스트를 순회하고 있는데 구독한 사람이 사라지려고하네!(변경 감지) 
5. `ConcurrentModificationException` 발생

added 메서드는 ObservableSet의 removeObserver를 호출하고, 이 메서드는 다시 observers.remove 메서드를 호출한다. 문제는 여기서 발생한다.

리스트에서 원소를 제거하려는데 마침 지금은 이 리스트를 순회하는 도중이다. 즉, 허용하지 않는 동작이다.

notifyElementAdded 메서드에서 수행하는 순회는 동기화 블록 안에 있으므로 동시 수정이 일어나지 않도록 보장하지만, 정작 자신이 콜백을 거쳐 되돌아와 수정하는 것까지는 막지는 못한다.

### 교착상태에 빠지게 되는 상황

다음으로는 구독해지를 하는 관찰자를 작성하는데 removeObserver를 직접 호출하지 않고 실행자 서비스(ExcutorService)를 사용하여 다른 스레드에게 부탁해보자

```java
private static void observableSetV1Method3() {
        ObservableSetV1<Integer> setV1 = new ObservableSetV1<>(new HashSet<>());
        // 코드 79-2 쓸데없이 백그라운드 스레드를 사용하는 관찰자
        setV1.addObserver(new SetObserverV1<Integer>() {
            @Override
            public void added(ObservableSetV1<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(()-> set.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            setV1.add(i);
        }
        // 23번째에서 교착상태에 빠진다
    }
```

위 코드의 결과는 예외는 나지 않지만 교착상태에 빠지게 되서 프로그램이 종료되지 않는다.

![Untitled 2](https://github.com/tkdals2317/effective-java/assets/49682056/5cd0eb0d-1ab2-42ec-ad9f-cf9a8ea60aa8)

백그라운드 스레드가 set.removeObserver를 호출하면 관찰자를 잠그려 시도하지만 락을 얻을 수 없다. 메인스레드가 이미 락을 쥐고 있기 때문이다.

그와 동시에 메인스레드는 백그라운드 스레드가 관찰자를 제거하기만을 기다리는 중이다. ⇒ 교착상태에 빠진다.

### 최악의 상황 : 데이터 회손

앞서의 두 예(예외와 교착상태)에서는 운이 좋았다. 동기화 영역이 보호하는 자원(관찰자)은 외계인 메서드(added)가 호출 될 때 일관된 상태였으니 말이다.

만약 똑같은 상황이지만 불변식이 임시로 깨진 경우라면 어떻게 될까?

자바 언어의 락은 재진입을 허용하므로 교착상태에 빠지지는 않는다.

예외를 발생시킨 첫 번째 예에서라면 외계인 메서드를 호출하는 스레드는 이미 락을 쥐고 있으므로 다음번 락 획득도 성공한다.

락이 보호하는 데이터에 대해 개념적으로 관련이 없는 다른 작업이 진행 중인데도 말이다.

이렇게 락이 제구실을 하지 못하게 되므로써 응답 불가(교착 상태)가 될 상황을 최악의 상황인 안전 실패(데이터 훼손)으로 변모 할 수 잇다.

### 최악의 상황을 피하는 방법 : 리스트를 복사하여 사용하자

외계인 메서드 호출을 동기화 블록 바깥으로 옮기면 된다.

notifyElementAdded 메서드에서라면 관찰자 리스트를 복사해 쓰면 락 없이도 안전하게 순회할 수 있다.

```java
// 코드 79-3 외계인 메서드를 동기화 바깥으로 옮겼다.
private void notifyElementAdded(E element) {
    List<SetObserverV2<E>> snapshot = null;
    synchronized (observers) {
        snapshot = new ArrayList<>(observers);
    }
    for (SetObserverV2<E> observer : snapshot) {
        observer.added(this, element);
    }
}
```

이 방식을 적용하면 앞서의 두 예제에서의 예외발생과 교착상태 증상이 사라진다.

동기화 영역 바깥에서 호출되는 외계인 메서드를 열린 호출(open call)이라 한다.

외계인 메서드는 얼마나 오래 실행될지 알 수가 없는데, 동기화 영역 안에서 호출된다면 그동안 다른 스레드는 보호된 자원을 사용하지 못하고 대기해야만 한다.

따라서 열린 호출은 실패 방지 효과 외에도 동시성 효율을 크게 개선해준다.

### 최선의 방법

외계인 메서드 호출을 동기화 블록 바깥으로 옮기는 방법이 최선의 방법이다.

자바의 동시성 컬렉션 라이버러리의 `CopyOnWriteArrayList` 가 이 목적으로 설계된 리스트로 ArrayList를 구현한 클래스로, 내부를 변경하는 작업은 항상 깨끗한 복사본을 만들어 수행하도록 구현 했다.

내부의 배열은 절대 수정되지 않으니 순회할 때 락이 필요 없어 매우 빠르다.

다른 용도에서는 느릴지라도 수정할 일이 드물고 순회만 빈번히 일어나는 관찰자 리스트 용도로는 적합하다.

```java
// 코드 79-4 CopyOnWriteArrayList를 사용해 구현한 스레드 안전하고 관찰 가능한 집합
public void addObserver(SetObserverV3<E> observer) {
        observers.add(observer);
}

public boolean removeObserver(SetObserverV3<E> observer) {
        return observers.remove(observer);
}

private void notifyElementAdded(E element) {
    for (SetObserverV3<E> observer : observers) {
        observer.added(this, element);
    }
}
```

위와 같이 동기화했던 부분만 삭제해주면 된다.

### 기본 규칙 : **동기화 영역에서는 가능한 한 일을 적게 하자**

락을 얻고, 공유 데이터를 검사하고, 필요하면 수정하고, 락을 놓는다. 오래 걸리는 작업이라면 아이템 78의 지침을 어기지 않으면서 동기화 영역 바깥으로 옮기는 방법을 생각해보자

**성능에 관하여**

동기화가 초래하는 진짜 비용 ⇒ 락을 얻는 데 드는 CPU 시간 X, 경쟁하느라 낭비하는 시간 O (즉, 병렬로 실행할 기회를 잃고, 모든 코어가 메모리를 일관되게 보기 위한 지연시간이 진짜 비용이다.)

**가변 클래스를 작성할  때의 두 가지 선택지**

1. 동기화를 하지 않고 그 클래스를 동시에 사용해야 하는 클래스가 외부에서 알아서 동기화 하게 하라
    
    ex) java.util
    
2. 동기화를 내부에서 수행해 스레드 안전한 클래스로 만들라( 단, 클라이언트가 외부에서 객체 전체에 락을 거는 것 보다 동시성을 월등히 개선할 수 있는 경우에만 )
    
    ex) java.utl.concurrent
    

위 규칙을 지키지 않아 생긴 클래스에 StringBuffer와 java.util.Random 등이 있다.

StringBuffer ⇒ StringBuilder

java.util.Random ⇒ java.util.concurrent.ThreadLocalRandom 

으로 대체 되었다.

### 정리

교착 상태와 데이터 훼손을 피하려면 동기화 영역 안에서 외계인 메서드를 절대 호출하지 말자.

일반화해 이야기하면, 동기화 영역 안에서의 작업은 최소한으로 줄이자.

가변 클래스를 설계할 때는 스스로 동기화해야 할지 고민하자.

멀티코어 세상인 지금은 과도한 동기화를 피하는게 과거 어느 때보다 중요하다. 합당한 이유가 있을 때만 내부에서 동기화하고, 동기화했는지 여부를 문서에 명확히 밝히자