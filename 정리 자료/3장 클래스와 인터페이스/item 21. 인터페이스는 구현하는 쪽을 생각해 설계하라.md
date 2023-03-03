# item 21. 인터페이스는 구현하는 쪽을 생각해 설계하라

<aside>
💡 인터페이스 디폴트 메서드에 대해 알아보고 디폴트 메서드를 사용할 시 문제점도 알아보자

</aside>

### 제목을 보면서 들은 생각

인터페이스는 구현하는 쪽? 여기서 구현하는 쪽이라는 건 뭘까?

말이 너무 어렵다… 

### 디폴트 메서드

이 장을 이해하기 위해서는 디폴트 메서드가 정확히 어떤 역할을 하는 건지 알고 지나가야 할 것 같아

디폴트 메서드에 대해 정리해봤다.

자바 7까지는 일반적으로 인터페이스의 메서드는 구현 대신 선언만 해놓고 이 인터페이스를 implements한 클래스에서 그 메서드를 구현할 수 있게 되있었다.

⇒ 자바 7까지는 인터페이스에 구현한 메서드가 있으면 컴파일 오류가 났음

하지만 자바 8 이후로는 디폴트 메서드가 추가되면서 인터페이스에서도 메서드를 구현할 수 있게 바뀌었다.

그리고 이 디폴트 메서드는 구현체에서 override를 하여 재정의를 할 수도 있다.

**디폴트 메서드의 필요성**

- 단순화
    
    공통적으로 사용되는 메서드는 인터페이스에서 기본 메서드로 정의하면 더 이상 클래스에서 추상 메서드를 구현하지 않아도 된다. 그로 인해 불필요한 작업은 줄이고 개발 속도는 높아지므로 작업이 단순화할 수 있다.
    
- 람다식
    
    Java 8 이전에는 컬렉션 객체를 순회해야 하는 경우 반복자(Iterator) 객체를 생성하고 hasNext() 메서드를 사용했습니다. 다음 예제는 컬렉션 객체를 순회하는 가장 일반적인 방법이다.
    
    ```java
    ArrayList aList = new ArrayList<String>();
    aList.add("One");
    aList.add("Two");
    aList.add("Three");
    
    // iterator() 메서드를 호출하여 Iterator 객체를 가져옵니다.
    Iterator iterator = aList.iterator();
    
    // hasNext() 메서드를 사용하여 컬렉션 객체를 순회합니다.while( iterator.hasNext() ){
    // next() 메서드를 사용하여 컬렉션 객체의 현재 요소를 가져옵니다.
      System.out.println( iterator.next() );
    }
    ```
    
    이제 위 예제처럼 반복자 객체를 생성하지 않아도 됩니다. Java 8부터 Iterable 인터페이스에 forEach() 기본 메서드가 추가되었기 때문입니다.
    
    Iterable 인터페이스의 forEach() 메서드를 사용하여 위 소스코드를 간결하게 만들 수 있습니다.
    
    ```java
    public class Main {
      public static void main(String args[]) {
        ArrayList aList = new ArrayList<String>();
        aList.add("One");
        aList.add("Two");
        aList.add("Three");
    
        aList.forEach((item) -> System.out.println(item));
      }
    }
    ```
    
    자료 출처 : 
    
    [[Java]기본 메서드 또는 디폴트 메서드(Default Method)](https://developer-talk.tistory.com/463)
    

### 모든 상황에서 불변식을 해치지 않는 디폴트 메서드를 작성하기는 어렵다

아무리 범용적으로 만든 디폴트 메서드라도 예외가 발생할 수 있다.

대표적인 예로 Collection 인터페이스에 추가된 removeIf 메서드가 있다.

```java
default boolean removeIf(Predicate<? super E> filter) {
    Objects.requireNonNull(filter);
    boolean removed = false;
    final Iterator<E> each = iterator();
    while (each.hasNext()) {
        if (filter.test(each.next())) {
            each.remove();
            removed = true;
        }
    }
    return removed;
}
```

위 removeIf 메서드는 매우 범용적인 메서드이지만 그렇다고 모든 Collection 구현체와 잘 어우러지는 것은 아니다.

`org.apache.commons.collections4.collection.SynchronizedCollection` 의 Collection 구현체는 클라이언트가 제공한 객체로 락을 거는 능력을 추가로 제공한다.

즉, 모든 메서드에서 주어진 락 객체를 동기화한 후 내부 컬렉션 객체에 기능을 위임하는 래퍼 클래스이다.

디폴트 메서드로 구현된 Collection인터페이스의 removeIf의 구현은 동기화에 관해 아무것도 모르므로 락 객체를 사용할 수 없다.

그 결과 여러 스레드가 공유하는 환경에서 한 스레드가 removeIf를 호출하면 `ConcurrentModificationException` 를 발생하거나 다른 예기치 못한 결과로 이어질 수 있다.

**해결책**

자바 플랫폼 라이브러리에서도 이런 문제를 예방하기 위해 일련의 조치를 취했다.

예를 들어 구현한 인터페이스의 디폴트 메서드를 정의하고, 다른 메서드에서는 디폴트 메서드를 호출하기 전에 필요한 작업을 수행하도록 했다.

Collections.synchronizedCollection을 반환하는 package-private 클래스들은 removeIf를 **재정의**하고, **이를 호출하는 다른 메서드들은 디폴트 구현을 호출하기 전에 동기화를 하도록 했다**.

```java
static class SynchronizedCollection<E> implements Collection<E>, Serializable {
        ...
        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            synchronized (mutex) {return c.removeIf(filter);}
        }
        ...
    }
```

하지만 자바 플랫폼에 속하지 않은 제 3의 컬렉션 구현체들은 이런 언어 차원의 인터페이스 변화에 발맞춰 수정될 기회가 없었으며, 그중 일부는 여전히 수정되고 있지 않다.(ex. `org.apache.commons.collections4.collection.SynchronizedCollection` )

### 디폴트 메서드는 컴파일에 성공하더라도 기존 구현체에 런타임 오류를 일으킬 수 있다.

기존 인터페이스에 디폴트 메서드로 새 메서드를 추가하는 일은 꼭 필요한 경우가 아니면 피해야한다.

디폴트 메서드가 기존 구현체들과 충돌할 수 있기 때문이다.

그리고 디폴트 메서드는 인터페이스로부터 메서드를 제거하거나 기존 메서드의 시그니처를 수정하는 용도가 아님을 명심해야 한다.

### 정리

디폴트 메서드라는 도구가 생겼더라도 남발하지말고 인터페이스를 설계할 때는 세심한 주의를 기울이자