# item 13. clone 재정의는 주의해서 진행하라

<aside>
💡 clone 메소드를 재정의해야 할 때 주의해야 할 점을 알아보고, clone을 대신 할 수 있는 방법인 복사 생성자, 복사 팩터리에 대해 알아보자

</aside>

### Cloneable 인터페이스

Cloneable이란 **복제해도 되는 클래스임을 명시하는 요도의 믹스인 인터페이스**이다.

clone 메서드는 Cloneable 인터페이스에 선언된 것이 아니고 Object에 있으며 접근 지시자도 protected로 되어있다.

Cloneable 인터페이스는 Object의 clone의 동작방식을 결정한다.(인터페이스를 굉장히 이례적으로 사용한 예이니 따라하지는 말자)

**Cloneable을 구현한 클래스의 인스턴스에서 clone을 호출하면 그 객체의 필드들을 복사한 객체를 반환하며, 그렇지 않은 클래스의의 인스턴스에서 호출하면 `CloneNotSupportedException`을 던진다.**

그리고 Cloneable을 구현한 클래스는 clone 메서드를 public을 제공한다. (Cloneable 인터페이스를 통해 clone메서드의 접근 지시자가 변경된다)

### clone 메서드의 허술한 일반 규약

> 이 객체의 복사본을 생성해 반환한다. ‘복사’의 정확한 뜻은 그 객체를 구현한 클래스에 따라 다를 수 있다. 일반적인 의도는 다음과 같다. 어떤 객체 x에 대해 다음 식은 참이다.
> 
> 
> 
> x.clone() != x (새로운 객체를 반드시 만들어야 되는 것 같다.)
> 
> 또한 다음 식도 참이다.
> 
> x.clone().getClass() == x.getClass()
> 
> **하지만 이상의 요구를 반드시 만족해야 한는 것은 아니다.**
> 
> 한편 다음 식도 일반적으로 참이지만, 역시 필수는 아니다.
> 
> x.clone().equals(x)
> 
> 관례상, 이 메서드가 반환하는 객체는 super.clone을 호출해 얻어야 한다. **이 클래스와 (Object를 제외한) 모든 상위 클래스가 이 관례를 따른다면 다음 식은 참이다.**
> 
> x.clone().getClass() == x.getClass()
> 
> 관례상, 반환된 객체와 원본 객체는 독립적이어야 한다. 이를 만족하려면 super.clone으로 얻은 객체의 필드 중 하나 이상을 반환 전에 수정해야 할 수도 있다.
> 

강제성이 없다는 점만 빼면 생성자 연쇄와 비슷한 메커니즘으로, clone 메서드가 super.clone이 아닌, 생성자를 호출해 얻은 인스턴스를 반환해도 문제가 없다.

### 제대로 된 clone 메서드를 구현해보자

제대로 동작하는 clone 메서드를 가진 상위 클래스를 상속해 Cloneable을 구현해보자.

1. **모든 필드가 기본타입이거나 불변 객체를 참조한 상태의 clone을 재정의하는 방법**

```java
public class PhoneNumber implements Cloneable{
    private short areaCode;
    private short prefix;
    private short lineNum;

    @Override
    protected PhoneNumber clone() {
        try {
            return (PhoneNumber) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // 일어날 수 없는 일이다.
        }
    }
}
```

먼저 super.clone을 호출하여 리턴한다. 

그리고 super.clone을 호출한 상태의 인스턴스는 Object이므로 반환하기전에 PhoneNumber를 반환하게끔 타입을 PhoneNumber로 변환 시켜 반환한다.(공변 반환 타이핑)

try-catch 블록으로 감싼 이유는 Object의 clone 메서드가 검사 예외인 `CloneNotSupportedException` 을 던지도록 선언되어 있기 때문이다.

1. **가변 객체를 잠조하는 경우의 clone을 재정의하는 방법**

```java
public class Stack implements Cloneable {
    private Object[] elements; // 문제의 가변객체!!!!!!
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        ...
    }

    public void push(Object e) {
				...
    }

		**public Object pop() {
        ...
    }

    private void ensureCapacity() {
        ...
    }

		@Override
    protected Stack clone() {
        try {
            return (Stack) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```

단순히 1번 방법으로 super.clone의 결과를 반환하면 size 필드는 올바른 값을 가지지만, elements 필드는 원본 Stack의 인스턴스와 똑같은 배열을 참조하게 된다.

원본이나 복제본 중 하나를 수정하면 다른 하나도 수정되어 불변식을 해치게 된다.

```java
@Override
protected Stack clone() {
    try {
				Stack result = (Stack)super.clone();
				result.elements = elements.clone();
        return result;
    } catch (CloneNotSupportedException e) {
        throw new AssertionError();
    }
}
```

가장 쉬운 방법은 위 코드 처럼 elements 배열의 clone을 재귀적으로 호출해주는 방법이다.

배열의 clone은 런타임 타입과 컴파일 타입 모두가 원본 배열과 똑같은 배열을 반환한다.

**따라서 배열을 복제할 때는 배열의 clone 메서드를 사용하라고 권장한다.(배열은 clone 기능을 제대로 사용하는 유일한 예)**

1. **복잡한 가변 상태를 갖는 클래스일 경우의 방법 1 (clone을 재귀적으로 호출하는 것만으로는 충분하지 않을 경우)**

해시테이블용 clone 메서드, 해시테이블 내부는 버킷들의 배열이고, 각 버킷은 키-값 쌍을 단는 연결 리스트의 첫 번째 엔트리를 참조하는 경우를 생각 해보자.

```java
public class HashTable implements Cloneable {
    private Entry[] buckets = ...;
 
    private static class Entry {
        final Object key;
        Object value;
        Entry next;
 
        Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

		// 잘못된 clone 메소드 - 가변 상태를 공유한다!
		@Override public HashTable clone() {
		    try {
		        HashTable result = (HashTable) super.clone();
		        result.buckets = buckets.clone();
		        return result;
		    } catch (CloneNotSupportedException e) {
		        throw new AssertionError();
		    }
		}
    ...  // 나머지 코드 생략
}
```

복제본은 자신만의 버킷 배열을 갖지만, 이 배열은 원본과 같은 연결 리스트를 참조하여 원본과 복제본 모두 예기치 않게 동작할 가능성이 생긴다.

그래서 각 버킷을 구성하는 연결 리스트를 복사하는 깊은 복사 개념의 deepCopy 메서드를 구현하여 해결한다.

```java
public class HashTable implements Cloneable {
    private Entry[] buckets = ...;
 
    private static class Entry {
        final Object key;
        Object value;
        Entry next;
 
        Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
				// 각 버킷을 구성하는 연결 리스트를 복사하는 메서드
				Entry deepCopy() {
						return new Entry(key, value, next == null ? null : next.deepCopy(); 
				}
    }

		// 잘못된 clone 메소드 - 가변 상태를 공유한다!
		@Override public HashTable clone() {
		    try {
		        HashTable result = (HashTable) super.clone();
		        result.buckets = new Entry[buckets.length];
						for (int i = 0; i < buckets.length; i++) {
								result.buckets[i] = buckets[i].deepCopy();
		        return result;
		    } catch (CloneNotSupportedException e) {
		        throw new AssertionError();
		    }
		}
    ...  // 나머지 코드 생략
}
```

재귀호출로 인한 리스트 원소 수만큼 스택 프레임을 소비하여, 리스트가 길면 스택 오버플로를 일으킬 위험이 있어 좋은 코드는 아니다.

deepCopy를 재귀 호출 대신 반복자를 써서 순회하는 방향으로 수정해보자

```java
// 각 버킷을 구성하는 연결 리스트를 복사하는 메서드
Entry deepCopy() {
		Entry result = new Entry(key, value, next);
		for (Entry p = result; p.next != null; p = p.next) {
				p.next = new Entry(p.next.key, p.next.value, p.next.next);
		}
		return result;
}
```

1. **복잡한 가변 상태를 갖는 클래스일 경우의 방법 2 (clone을 재귀적으로 호출하는 것만으로는 충분하지 않을 경우)**

고수준 API를 활용하자 에를 들어 HashTable에서, buckets 필드를 새로운 버킷 배열로 초기화 후 put 메서드를 사용하여 둘의 내용을 똑같이 해준다.

대신 put 메서드는 final이거나 private으로 지정하여 복제 과정에서 상태가 변경되는 상황을 피하도록 하자

저수준으로 처리할때보단 느리다는 단점이 있고, Cloneable 아키텍처와는 어울리지 않는 방식이다.

### 사실 상속해서 쓰기 위한 클래스는 Cloneable을 구현해서는 안된다.

1. Object의 clone을 모방하여 `CloneNotSupportedException` 을 던지게 한다.
2. clone을 동작하지 않게 구현해놓고 하위클래스에서 재정의하지 못하게 한다 .

### Cloneable을 구현한 스레드 안전클래스를 작성할 때는 clone 메서드 역시 적절히 동기화 해주어야 한다.

### 요약

- Cloneable을 구현하는 모든 클래스는 clone을 재정의하라
- 접근 제한자는 public으로 반환타입은 클래스 자신으로 변경한다.
- 가장 먼저 super.clone()을 호출 한 후, 필드에 맞게 복제를 적절히 수행한다(깊은 복사)

### 차라리 Cloneable 구현보다 복사 생성자와 복사 팩터리를 사용하자

복사 생성자란 단순히 자신과 같은 클래스의 인스턴스를 인수로 받는 생성자를 말한다.(변환 팩토리, 변환 생성자라고 표현하는 게 더 정확하다)

```java
// 복사 생성자
public Yum(Yum yum) { ... };

// 복사 팩터리
public static Yum newInstance(Yum yum) { ... };
```

Cloneable/clone 방식보다 나은 면이 많다.

언어 모순적이고 위험천만한 객체 생성 매커니즘(생성자를 쓰지 않는 방식)을 쓰지 않고, 엉성하게 문서호된 규약에 기대지 안혹, 정상적인 final 필드 용법과 충돌하지도 않으며, 불필요한 검사 예외를 던지지도 않으며, 형변환도 필요치 않다.

대표적인 변환 생성자

```java
TreeSet coptTreeSet = new TreeSet<>(s); // s는 원본 TreeSet
```

### 정리

문제가 많은 Cloneable을 확장하는 것은 지양하자(배열의  clone 제외)

대신 복제가 하고싶다면 복사 생성자, 복사 팩ㄷ터리를 사용하자