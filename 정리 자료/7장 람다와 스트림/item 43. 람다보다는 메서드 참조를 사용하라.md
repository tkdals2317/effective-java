# item 43. 람다보다는 메서드 참조를 사용하라

<aside>
💡 람다 대신 메서드 참조를 사용하는 법을 배워보자

</aside>

### 메서드 참조

람다가 익명 클래스보다 나은 점 중에서 가장 큰 특징은 간결함이다.

메서드 참조를 이용하면 함수 객체를 심지어 람다보다도 더 간결하게 만들 수 있다.

```java
// 람다 
map.merge("key", 1 , (count, incr) -> count + incr);
// 메서드 참조
map.merge("key", 1 , Integer::sum);
```

위 코드는 첫번 째 소스의 람다와 같은 기능을 하는 Integer 클래스의 정적 메서드 sum을 메서드 참조를 사용하여 코드를 훨씬 간결하게 만들었다.

매개변수 수가 늘어날수록 메서드 참조로 제거할 수 있는 코드양도 늘어난다.

하지만 어떤 람다에서는 매개변수의 이름 자체가 프로그래머에게 좋은 가이드가 되기도 한다.

람다는 길이는 더 길지만 메서드 참조보다 읽기 쉽고 유지보수도 쉬울 수 있다.

람다로 할 수 없는 일이라면 메서드 참조로도 할 수 없다. 

그렇더라도 메서드 참조를 사용하는 편이 보통은 짧고 간결하므로, 람다를 구현했을 때 너무 길거나 복잡하다면 메서드 참조가 좋은 대안이 되어 준다.

즉, 람다로 작성할 코드를 새로운 메서드에 담은 다음, 람다 대신 그 메서드 참조를 사용하는 식이다.

메서드 참조의 장점 

- 가독성이 좋다
- 람다와 다르게 기능을 잘 드러내는 이름을 지어줄 수 있고, 친절하게 문서로 남길 수 있다.

하지만 람다가 메서드 참조보다 간결할 때가 있다. 주로 메서드와 람다가 같은 클래스에 있을 때 그렇다.

```java
// 메서드 참조 - 너무 길다..
service.execute(GoshThisClassNameIsHumongous::action)
// 람다 - 오히려 간결하다
service.excute(()-> action()); 
```

메서드 참조 쪽은 더 짧지도, 더 명확하지도 않다. 

비슷한 예로 java.uti.function 패키지가 제공하는 제네릭 정적 팩터리 메서드인 Function.identity()를 사용하기보다 람다(x → x)를 직접 사용하는 편이 코드도 짧고 명확하다. (난 취향차인거 같다.)

### 메서드 참조의 유형 5가지

1. 정적 메서드를 가르키는 메서드 참조
    
    ex) `Integer::parseInt` ⇒ 람다로 표현하면 `str  → Integer.parseInt(str)`
    
2. 수신 객체(receiving object : 참조 대상 인스턴스)를 특정하는 한정적(bound) 인스턴스 메서드 참조
    
    근본적으로 정적 참조와 비슷하다. 즉 함수 객체가 받는 인수와 참조되는 메서드가 받는 인수가 똑같다.
    
    ex) `Instant.now()::isAfter` ⇒ 람다로 표현하면 `Instant the = Instant.now(); t → then.isAfter(t);`
    
3. 수신객체를 특정하지 않는 비한정적(unbound) 인스턴스 메서드 참조
    
    함수 객체를 적용하는 시점에 수신 객체를 알려준다. 
    
    이를 위해 수신 객체 전달용 매개변수가 매개변수 목록의 첫 번째로 추가되며, 그 뒤로는 참조되는 메서드 선언에 정의된 매개변수들이 뒤 따른다.
    
    **주로 스트림 파이프 라인에서 매핑과 필터 함수로 쓰인다.**
    
    ex) `String::toLowerCase` ⇒ 람다로 표현하면 `str → str.toLowerCase()` 
    
4. 클래스 생서자를 가리키는 메서드 참조
    
    ex) `TreeMap<K,V>::new` ⇒ 람다로 표현하면 `() → new TreeMap<K,V>()`  
    
5. 배열 생성자를 가리키는 메서드 참조
    
    ex) `int[]::new` ⇒ 람다로 표현하면 `len → new int[len]`
    

 

### 람다로는 불가능하나 메서드 참조로는 가능한 유일한 예 : 제네릭 함수 타입(genric function type)

함수형 인터페이스의 추상 메서드가 제네릭일 수 있듯이 함수 타입도 제네릭일 수 있다.

```java
public interface G1 {
    <E extends Exception> String m() throws E;
}
public interface G2 {
    <F extends Exception> String m() throws Exception;
}
public interface G extends G1, G2 {
}
```

이 때 함수형 인터페이스 G를 함수 타입으로 표현하면 다음과 같다.

`<F extends Exception> () -> String throw F`

이처럼 함수형 인터페이스를 위한 제네릭 함수 타입은 메서드 참조 표현식으로는 구현될 수 있지만, 제네릭 람다식이라는 문법이 존재하지 않기 때문에 람다식으로는 불가능하다(?)

### **정리**

**람다로 작성할 코드를 새로운 메서드에 담은 다음, 람다 대신 그 메서드 참조를 사용하는 식으로 람다를 메서드 참조로 대체 하면 코드를 더 간결하게 만들 수 있다.**

상황에 따라 람다와 메서드 참조를 잘 이용해보자

람다를 구현했을 때 너무 길거나 복잡한 경우 → **메서드 참조**

클래스이름이 너무 긴 경우, 람다식의 매개변수가 프로그래머에게 더 쉽게 이해되는 경우 → **람다**

**람다의 종류 5가지**

1. 정적 메서드를 가르키는 메서드 참조  :  `Integer::parseInt` 
2. 수신 객체(receiving object : 참조 대상 인스턴스)를 특정하는 한정적(bound) 인스턴스 메서드 참조 : `Instant.now()::isAfter` 
3. 수신객체를 특정하지 않는 비한정적(unbound) 인스턴스 메서드 참조 : `String::toLowerCase`
4. 클래스 생서자를 가리키는 메서드 참조 : `TreeMap<K,V>::new`
5. 배열 생성자를 가리키는 메서드 참조 : `int[]::new`