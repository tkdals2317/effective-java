# item 26. 로 타입은 사용하지 말라

<aside>
💡 **로 타입을 사용해서는 안되는 이유를 알아보자**

</aside>

### 용어 정리

클래스와 인터페이스 선언에 타입 매개변수(type parameter)가 쓰이면, 이를 **제네릭 클래스** 혹은 **제네릭 인터페이스**라고 한다. 그리고 이 둘을 통틀어 **제네릭 타입**이라고 한다.

각각의 제네릭 타입은 일련의 매개변수화 타입을 정의한다. 

먼저 클래스(혹은 인터페이스) 이름이 나오고, 이어서 꺽쇠 괄호 안에 실제 타입 매개변수를 나열한다.

ex) `List<String>` ⇒ 원소 타입이 `String`인 리스트를 뜻하는 매개변수화 타입, 여기서 `String`이 정규 타입 매개변수 E에 해당하는 실제 매개변수이다.

제네릭 타입을 하나정의하면 그에 딸린 로 타입(raw type)도 함께 정의된다.

**로 타입 :** 제네릭 타입에서 타입 매개변수를 전혀 사용하지 않은 경우 ex) `List<E>`의 로 타입은 `List`

⇒ 타입 선언에서 제네릭 타입정보가 전부 지워진 것 처럼 동작, 제네릭이 생기기 전 코드와 호환되도록 하기 위한 어쩔 수없 는 방법이기 때문이다.

| 한글 용어 | 영문 용어 | 예 |
| --- | --- | --- |
| 매개변수화 타입 | parameterized type | `List<String>` |
| 실제 타입 매개변수 | actual type parameter | `String` |
| 제네릭 타입 | generic type | `List<E>` |
| 정규 타입 매개변수 | formal type parameter | `E` |
| 비한정적 와일드카드 타입 | unbounded wildcard typ | `List<?>` |
| 로 타입 | raw type | `List` |
| 한정적 타입 매개변수 | bounded type parameter | `<E extends Number>` |
| 재귀적 타입 한정 | recursive type bound | `<T extends Comparable<T>>` |
| 한정적 와일드카드 타입 | bounded wildcard type | `List<? extends Number>` |
| 제네릭 메서드 | generic method | `static <E> List<E> asList(E[] a)` |
| 타입 토큰 | type token | `String.class` |

### 제네릭이 지원하기 전의 자바

```java
// Stamp 인스턴스만 취급한다.
private final Collection stamps = ...;

..
stamps.add(new Coin()); // 정상적으로 컴파일, unchecked call 경고
```

Stamp외 다른 클래스의 인스턴스를 넣어도 오류 없이 컴파일되고 실행된다. (물론 unchecked call 경고는 뜬다.)

![Untitled](https://user-images.githubusercontent.com/49682056/222758940-bb7a714f-5b31-4b27-8af2-e5f77c54a03a.png)


```java
public void cancelAll(){
    for (Iterator i = stamps.iterator(); i.hasNext();) {
        // Coin 인스턴스가 들어가있는 경우 ClassCastException 발생
        Stamp stamp = (Stamp) i.next(); 
        stamp.cancel();
    }
}
```

런타임에서 꺼내서 사용 시 Coin 인스턴스 일 시 ClassCastException 발생한다.

### 매개변수화 된 컬렉션 타입으로 타입 안정성을 확보하자

제네릭을 사용하여 위 코드를 바꿔보자

```java
private final Collection<Stamp> stamps = new ArrayList<>() {};
```

이렇게 stamps에는 Stamp 인스턴스만 넣어야 함을 선언해주자

![Untitled 1](https://user-images.githubusercontent.com/49682056/222758923-7ba42642-7f3b-447b-8652-b9d09a6e0b14.png)

컴파일 단계에서 타입을 체크하여 다른 타입의 인스턴스를 넣으려면 컴파일 오류가 난다. 

로타입을 쓰는 걸 언어적으로 막아 놓지는 않았지만 **로타입을 쓰면 제네릭이 안겨주는 안정성과 표현력을 모두 잃게 된다.**

그러므로 로타입은 사용하지 않는 것이 좋다. 

### `List<Object>`처럼 임의의 객체를 허용하는 매개변수화 타입은 괜찮다

**로 타입 `List`와 `List<Object>`의 차이점**

`List` : 제네릭 타입에서 완전히 발을 뺀 것

`List<Object>` : 모든 타입을 허용한다는 것을 컴파일러에 명확히 전달

매개변수로 `List`를 받는 메서드에 `List<String>`을 넘길 수 있지만, `List<Object>`는 넘길수 없다.

⇒ 제네릭의 하위 타입 규칙 때문이다.

`List<String>`은 로타입인 `List`의 하위 타입이지만, `List<Object>`는 하위 타입이 아니다.

**`List<Objcet>` 같은 매개변수화 타입을 사용할 때와 달리 List 같은 로 타입을 사용하면 타입 안정성을 잃게된다.**

```java
public static void main(String[] args) {
    List<String> strings = new ArrayList<>();
    unsafeAdd(strings, Integer.valueOf(42));
    String s = strings.get(0); // ClassCastException
}

private static void unsafeAdd(List list, Object o) {
    // unchecked call 경고 
    list.add(o); 
}
```

컴파일은 되나 로 타입인 `List`를 사용하여 unchecked call 경고가 뜬다.

![Untitled 2](https://user-images.githubusercontent.com/49682056/222758930-07978f9d-872e-4ae9-a1e1-93f85adb3e85.png)

`String s = strings.get(0);` 부분을 실행 할 때 `Integer`를 `String`으로 변환을 시도하다 `ClassCastException`이 발생하게 된다.

![Untitled 3](https://user-images.githubusercontent.com/49682056/222758932-2d5d1639-38d4-40f0-a7ff-50dc71696934.png)

### 제네릭 타입을 쓰고 싶지만 실제 타입 매개변수를 신경 쓰고 싶지 않을 때는 와일드 카드 타입을 대신 사용하자

```java
// 잘못된 예 - 모르는 타입의 원소도 받는 로 타입을 사용했다.
static int numElementsInCommon(Set set1, Set set2){
    int result = 0;
    for (Object o1 : set1) {
        if (set2.contains(o1))
            result++;
    }
    return result;
}
```

동작은 하나 역시 타입 안정성이 떨어진다.

대신 비한정적 와일드 카드 타입을 대신 사용하는 게 좋다.

```java
static int numElementsInCommonWildCard(Set<?> set1, Set<?> set2){...}
```

제네릭 타입인 `Set<E>`의 비한정적 와일드 카드 타입은 `Set<?>`이다.

어떤 타입이라도 담을 수 있는 가장 범용적인 매개변수화 `Set` 타입이다.

**와일드 카드는 타입 안정성이 있다고 하는데 왜 일까?**

로 타입 컬렉션에는 아무 원소나 넣을 수 있으니 타입 불변식을 훼손하기 쉽다. 반면 와일드 카드를 사용한 `Collection<?>`에는 (null 이외에는) 어떤 원소도 넣을 수 없다. 그리고 꺼낼 수 있는 객체의 타입도 전혀 알수 없게 했다.

![Untitled 4](https://user-images.githubusercontent.com/49682056/222758936-1cfbb54b-75f2-4506-98e8-827dc7ee53ca.png)

### 로타입을 사용할 수 있는 몇 가지 예외

1. **class 리터럴에는 로 타입을 써야한다.**
    
    자바 명세에는 class 리터럴에 매개변수화 타입을 사용하지 못하게 했다. (배열과 기본 타입은 허용)
    
    `List.class`, `String[].class`, `int.class` 는 허용하나 `List<String>.class`나 `List<?>.class` 는 허용하지 않는다.
    
2. **instanceof 연산자**
    
    런타임에는 제네릭 타입 정보가 지워지므로 `instanceof` 연산자는 비한정적 와일드카드 타입 이외의 매개변수화 타입을 적용할 수 없다.
    
    그리고 로 타입이든 비한정적 와일드카드 타입이든 `instanceof`는 완전히 똑같이 동작한다.
    
    비한정적 와일드 카드 타입의 꺽쇠 괄화와 물음표는 역할 없이 코드만 지저분하게 만드므로 차라리 로 타입을 쓰는게 깔끔하다.
    
    아래는 제네릭 타입에 `instanceof`를 사용하는 올바른 예이다.
    
    ```java
    if (o instanceof Set) {
    	Set<?> s = (Set<?>) o;
    	...
    }
    ```
    
    <aside>
    📍 o 타입이 Set타입임을 확인한 다음 와일드카드 타입인 Set'<'?`>`으로 형변환해야 한다. (로 타입인 Set이 아니다)
    이는 검사 형변환(checked cast)이므로 컴파일러 경고가 뜨지 않는다.
    
    </aside>
    

### 정리

**로타입을 사용하면 타입 안정성이 떨어져 런타임에 예외가 발생할 수 있으니 되도록이면 사용하지 말자!**

`Set<Object>` : 어떤 타입의 객체도 저장할 수있는 매개변수화 타입(안전)

`Set<?>` : 모든 종의 타입 객체만 저장할 수 있는 와일드카드 타입(안전)

`Set` : 제네릭 타입 시스템에 속하지 않는다.(안전하지 않음)