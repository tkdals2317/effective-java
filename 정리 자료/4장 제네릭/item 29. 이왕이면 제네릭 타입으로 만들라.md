# item 29. 이왕이면 제네릭 타입으로 만들라

<aside>
💡 스택 코드를 제네릭으로 변경해보자

</aside>

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
        Object result = elements[--size];
        elements[size] = null;
        return elements[--size];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

위 소스 코드를 제네릭 타입으로 변경 해보자

위 코드는 클라이언트가 pop한 객체를 형변환해야 된다는 문제가 있는데 이는, 런타임 오류가 날 위험이 있다.

### 일반 클래스를 제네릭 클래스로 만드는 첫 단계, 클래스 선언에 타입 매개 변수를 추가하자

```java
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new E[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        E result = elements[--size];
        elements[size] = null;
        return elements[--size];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

이렇게 바꾸면 오류가 하나 발생한다.

![Untitled](https://user-images.githubusercontent.com/49682056/224526791-2d8716aa-1233-421d-adf2-a02958842bfd.png)

E와 같은 실체화 불가 타입으로 배열을 만들 수 없어 생기는 오류이다.

이는 배열을 사용하는 코드를 제네릭으로 만들려 할 때 마다 마주쳐야하는 에러이다.

### 배열을 사용한 코드를 제네릭으로 만드는 방법 1

생성자에서 Object 배열을 생성한 다음 제네릭 배열로 형변환 하는 방법이다.

```java
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public Stack() {
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    ...
}
```

컴파일러는 타입 안전한지 증명할 방법이 없지만 프로그래머는 할 수 있다.

문제의 배열 elements는 private 필드에 저장되고, 클라이언트로 반환되거나 다른 메서드에 전달되는 일이 전현 없다.

push 메서드를 통해 배열에 저장되는 원소의 타입도 항상 E다. 따라서 이 비검사 형변환은 확실히 안전하므로 `@SuppressWarnings("unchecked")` 로 해당 경고를 숨긴다.

이렇게되면 명시적 형변환 하지 않아도 ClassCastException 걱정 없이 사용할 수 있다.

### 배열을 사용한 코드를 제네릭으로 만드는 방법 2

elements 필드 타입을 E[]에서 Object[]로 바꾸는 방법이다.

```java
public class Stack<E> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
				// 경고가 뜨는 부분
        @SuppressWarnings("unchecked") E result = (E) elements[--size];
        elements[size] = null;
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

이번에는 pop에서 경고가 뜨게 되는데 이 부분을 `@SuppressWarnings("unchecked")` 처리하여 경고를 숨겨주자

첫번 째 방법이 형변환을 배열 생성 시에 한번만 해주고 가독이 더 좋아 개발자들이 더 선호하며 자주 자주 사용하지만, 배열의 런타임 타입이 컴파일 타입과 달라 힙 오염을 일으킨다.

### 한정적 타입 매개변수를 사용한 제네릭

![Untitled 1](https://user-images.githubusercontent.com/49682056/224526788-0b601a82-25c3-4a3b-b1c6-baf5db8bb2e1.png)

`Delayed`의 하위 타입만 받는다.

클라이언트는 `DelayQueue`에서 형변환 없이 곧바로 `Delayed`의 메서드를 호출 할 수 있다.

모든 타입은 자기 자신의 하위 타입이므로 `DelayQueue<Delayed>`로도 사용할 수 있다.

### 정리

클라이언트가 직접 형변환을 해야하는 타입보다 제네릭 타입이 더 안전하고 쓰기 편하다.

그러니 새로운 타입을 설계할 때는 형변환 없이도 사용 할 수 있게 하라.

가장 좋은 방법은 제네릭 타입을 사용하는 방법이다.