# item 18. 상속보다는 컴포지션을 사용하라

<aside>
💡 상속은 코드를 재사용하는 강력한 수단이지만, 여러 단점이 존재한다. 
상속을 사용했을 때의 단점과 그 단점을 극복할 수 있는 컴포지션에 대해 알아보자

</aside>

### 상속의 단점

상속에서 구체 클래스를 패키지 경계를 넘어, 다른 패키지의 구체 클래스를 상속하는 일은 위험하다.

**메서드 호출과 달리 상속은 캡슐화를 깨트린다.**

상위 클래스가 어떻게 구현되느냐에 따라 하위 클래스의 동작에 이상이 생길 수 있다.

```java
public class InstrumentedHashSet<E> extends HashSet<E> {
    // 추가된 원소의 수
    private int addCount = 0;

    public InstrumentedHashSet() {
    }

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}
```

```java
@Test
public void 상속을_잘못_사용한_예() {
    InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
    s.addAll(List.of("틱", "탁탁", "펑"));
    System.out.println(s.getAddCount()); // 3을 기대했지만 6이 나온다
}
```

3개의 String을 addAll() 메서드로 넣어 getAddCount() 메서드를 실행하면 3이 나오길 기대하지만 6이 출력된다.

![Untitled](https://user-images.githubusercontent.com/49682056/220915661-d5d08d30-5cef-40e4-933d-e1a5b5e227d4.png)

**WHY?** `HashSet`에서 addAll은 각 원소를 add 메서드를 호출하여 추가하는데, 이 때 사용되는 add 메서드는 는 `InstrumentedHashSet`에서 재정의한 add 메서드가 호출되면서 addCount가 2개씩 증가되기 때문이다.

이 문제를 해결하기 위해 상위 클래스의 메서드 동작을 모두 다시 구현하는 방식은 어렵고, 시간도 더 들 뿐만 아니라 성능을 떨어뜨릴 수도 있다.

또한 상위클래스에 새로운 메서드가 추가 된다면, 그 메서드에 맞춰 모든 메서드를 재정의해야 한다.

위 처럼 메서드 재정의가 문제의 원인이라 판단하고 재정의 대신 새로운 메서드를 추가하면 될 것 같지만 운나쁘게 하위 클래스에 추가한 메서드가 시그니처가 같고 반환타입이 다르면 컴파일조차 되지 않는다.

### 컴포지션을 사용하자

기존 클래스(InstrumentedHashSetWithComposition)를 확장하는 대신, 새로운 클래스(ForwardingSet)를 만들고 private 필드로 기존 클래스의 인스턴스(s)를 참조하게 하자

**기존 클래스가 새로운 클래스의 구성요소로 쓰인다는 뜻에서 이러한 설계를 컴포지션(composition)이라 한다.**

새 클래스(전달 클래스 ForwardingSet)의 인스턴스 메서드들은 기존 클래스(InstrumentedHashSetWithComposition)의 대응하는 메서드를 호출해 그 결과를 반환한다.

이 새 클래스(전달 클래스 ForwardingSet)의 메서드들 전달 메서드(forwarding method)라 부른다.

⇒ 새로운 클래스는 기존 클래스의 내부 구현 방식의 영향을 벗어나며, 심지어 기존 클래스의 새로운 메서드가 추가되더라도 전혀 영향을 받지 않는다.

예를 보면서 이해해보자 

하나는 집합 클래스 자신이고, 다른 하나는 전달 메서드만으로 이뤄진 재사용 가능한 전달 클래스이다.

```java
public class InstrumentedHashSetWithComposition<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public InstrumentedHashSetWithComposition(Set<E> s) {
        super(s);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

}
```

```java
// 전달 클래스
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;
    //set 인스턴스를 인수로 받는 생성자
    public ForwardingSet(Set<E> s) { this.s = s; }

    public void clear()               { s.clear();            }
    public boolean contains(Object o) { return s.contains(o); }
    public boolean isEmpty()          { return s.isEmpty();   }
    public int size()                 { return s.size();      }
    public Iterator<E> iterator()     { return s.iterator();  }
    public boolean add(E e)           { return s.add(e);      }
    public boolean remove(Object o)   { return s.remove(o);   }
    public boolean containsAll(Collection<?> c)
    { return s.containsAll(c); }
    public boolean addAll(Collection<? extends E> c)
    { return s.addAll(c);      }
    public boolean removeAll(Collection<?> c)
    { return s.removeAll(c);   }
    public boolean retainAll(Collection<?> c)
    { return s.retainAll(c);   }
    public Object[] toArray()          { return s.toArray();  }
    public <T> T[] toArray(T[] a)      { return s.toArray(a); }
    @Override public boolean equals(Object o)
    { return s.equals(o);  }
    @Override public int hashCode()    { return s.hashCode(); }
    @Override public String toString() { return s.toString(); }
}
```

`InstrumentedHashSetWithComposition`은 `HashSet`의 모든 기능을 정의한 Set 인터페이스를 활용해 설계되어 견고하고 아주 유연하다.

구체적으로는 Set 인터페이스를 구현했고, Set의 인스턴스를 인수로 받는 생성자를 하나 제공한다.

임의의 Set에 계측 기능(getAddCount() 메서드)을 덧씌워 새로운 Set으로 만드는 것이 이 클래스의 핵심이다.

```java
@Test
public void 컴포지션을_사용한_예() {
    InstrumentedHashSetWithComposition<String> s = new InstrumentedHashSetWithComposition<>(new HashSet<>());
    s.addAll(List.of("틱", "탁탁", "펑"));
    System.out.println(s.getAddCount()); // 3이 정상적으로 나온다
}
```

![Untitled 1](https://user-images.githubusercontent.com/49682056/220915658-b1a9c134-eb71-4e37-8220-a3d384006063.png)

그리고 이런 방식을 이용하면 Set 인스턴스를 즉정 조건하에서만 임시로 계측할 수 있다.

다른 Set 인스턴스를 감싸고(wrap)있다는 뜻이에서 `InstrumentedHashSetWithComposition` 같은 클래스를 **래퍼 클래스**라고 하며, 다른 Set에 계측 기능을 덧씌운다는 뜻에서 **데코레이터 패턴(Decorator Pattern)**이라고 한다.

컴포지션과 전달의 조합은 **넓은 의미로 위임(delecation)**이라고 부른다. 단, 엄밀히 따지면 래퍼 객체가 내부 객체에 자기 자신의 참조를 넘기는 경우만 위임에 해당한다.

### 상속은 반드시 하위 클래스가 상위 클래스의 ‘진짜’ 하위 타입인 상황에서만 쓰여야 한다.

클래스 B가 클래스 A와 is-a 관계일 때만 클래스 A를 상속해야 한다.

대부분이 상속보다는 A를 private 인스턴스로 두고, A와는 다른 API를 제공해야 하는 상황이 대다수이다. 즉, A는 B의 필수 구성요소가 아니라 구현하는 방법 중 하나 일 뿐이다.

이런 경우에는 컴포지션 방법을 사용하자

### 정리

상속은 강력하지만 캡슐화를 해친다는 문제가 있다.

상속은 상위 클래스와 하위 클래스가 순수한 is-a 관계일 때만 써야한다.

is-a 관계일 때도 안심할 수만은 없는 게, 하위 클래스의 패키지가 상위 클래스와 다르고, 상위 클래스가 확장을 고려해 설계되지 않았다면 여전히 문제가 될 수 있다.

**상속의 취약점을 피하려면 상속 대신 컴포지션과 전달을 사용하자.** 

특히 래퍼 클래스로 구현할 적당한 인터페이스가 있다면 더욱 그렇다. 래퍼 클래스는 하위 클래스보다 견고하고 강력하다.

내용이 겹친다고 무작정 상속을 받아 사용하는 건 피하도록 하자!