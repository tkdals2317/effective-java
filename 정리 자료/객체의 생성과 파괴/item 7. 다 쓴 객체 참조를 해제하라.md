# item 7. 다 쓴 객체 참조를 해제하라

### 개요

자바는 C, C++ 처럼 메모리를 직접 관리해야 되는 언어와 달리 가비지 컬렉터를 통해 메모리를 직접 해제할 일이 없다.

하지만 그럼에도 불구하고 메모리 관리에 신경을 쓰지 않는 건 절대 사실이 아니다. 

메모리 누수가 나는 코드들을 알아보고 어떤 식으로 메모리 누수를 피할 수 있는 지 알아보자

### 스택을 구현한 예제로 알아보는 메모리 누수

```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

위 스택을 구현한 소스로 프로그램을 오래하면 가비지 컬렉션 활동과 메모리 사용량이 늘어나 결국 성능저하가 발생하거나 OOM이 발생하여 프로그램이 예기치않게 종료될 수 있다.

```java
    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[--size];
    }
```

위 코드에서 스택이 커졌다가 줄어들었을 때 스택에서 꺼내진 객체들은 가비지 컬렉터가 회수하지 않는다.

프로그램에서 이 객체를 사용하지 않더라도 여전히 **“다 쓴 객체 참조”**를 여전히 갖고 있기 때문이다.

여기서 **다 쓴 참조**란 말 그대로 **앞으로 다시 쓰이지 않을 참조**를 뜻한다.

앞의 코드에서 elements 배열의 ‘활성 역역’ 밖의 참조들이 모두 여기에 해당된다. 즉 인덱스의 사이즈보다 큰 원소들이 다 쓴 객체 참조이다.

**해법** 

다 쓴 객체를 참조를 해제 시켜주면 되는데 그 방법은 간단하게 null 처리를 하면 된다.

```java
public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }
```

이렇게 null 처리를 하면 다른 이점도 따라 온다. 만약 null 처리한 참조를 실수로라도 사용하려고 하면 NPE를 던지며 종료 시킬 수 있어 프로그램의 오류를 빠르게 발견할 수 있다.

**하지만..**

객체 참조를 null 처리하는 예외적인 특수한 케이스이다.

일반적으로 **다 쓴 참조를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 유효 범위(scope) 밖으로 밀어내는 것**이다.

변수의 범위를 최소가 되게 정의했다면 이 일은 자연스럽게 이뤄진다.

그렇다면 null 처리는 언제 해야 할까?

위 Stack 클래스가 메모리 누수에 약한 이유는 스택이 자기 메모리를 직접 관리하기 때문이다.

위 스택에서는 elements라는 배열로 저장소 풀을 만들어 원소들을 관리하므로 배열의 활성 영역에 대한 속한 원소들이 사용되고 비활성 영역은 쓰이지 않는다.

즉, 스택이 사라지기 전까지는 비활성영역이건 활성영역이건 가비지 컬렉터는 비활성영역인 걸 인지하지 못한다.

그래서 비활성 영역에서 참조하는 객체는 비**활성 영역이 되는 순간 null 처리를 하여 해당 객체는 더 이상 쓰지 않음을 가비지 컬렉터에게 알려야 한다.**

**그래서 항상 자기 메모리를 직접 관리하는 클래스라면 프로그래머는 항시 메모리 누수에 주의 해야 한다!!!** 

---

### 캐시로 인한 메모리 누수

객체 참조를 캐시에 넣고 나서, 그 사실을 잊은 채, 그 객체를 다 쓴 뒤로도 한참을 그대로 놔두는 일을 자주 접할 수 있다.

**해법**

1. WeakHashMap을 사용하여 캐시를 만들면 다 쓴 엔트리는 그 즉시 제거된다. 단, WeakHashMap은 이러한 상황에서만 유용하다는 것을 기억하자
2. 시간이 지날수록 엔트리의 가치를 떨어뜨리는 방식에서 ScheduledThreadPoolExcuter와 같은 백그라운드 스레드를 사용
3. 캐시에 새 엔트리를 추가할 때 부수작업을 수행하는 방법 예를 들어, LinkedHashMap은 새로운 값을 put할 때 removeEldestEntry 메서드를 써서 가장 오래된 값을 지우고 새로운 값을 대체한다
4. java.lang.ref 패키지를 직접 활용

---

### 리스너(Listener) 혹은 콜백(Callback)

클라이언트가 콜백을 등록만하고 명확히 해지하지 않는다면, 뭔가 조치를 해주지 않는 한 콜백은 계속 쌓이게 된다.

이럴 때 약한 참조(weak reference)로 저장하면 가비지 컬렉터가 즉시 수거해간다. 

예를 들어 WeakHashMap에 키로 저장하면 된다.

---

### 정리

메모리 누수는 겉으로 잘 드러나지 않아 시스템에 수년간 잠복하는 사례도 있다.

이런 누수는 철저한 코드 리뷰나 프로파일러 같은 디버깅 도구를 동원해야만 발견되기도 한다. 그래서 이런 종류의 문제의 예방법을 익혀두는 것은 매우 중요하다!

LIKE bigfile OOM 사건..ㅠㅠㅠ 빨리 해결 됬으면 좋겠다.