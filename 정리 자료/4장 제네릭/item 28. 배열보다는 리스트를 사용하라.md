# item 28. 배열보다는 리스트를 사용하라

<aside>
💡 배열과 제네릭의 차이점을 알아보고 타입 안전성을 챙길 수 있는 방법을 알아보자

</aside>

### 배열과 제네릭 타입의 차이점

**차이점 1. 배열은 공변이나 제네릭은 불공변이다.**

공변 : `Sub`가 `Super`의 하위 타입이라면 배열 `Sub[]`는 배열 `Super[]`의 하위 타입이 된다.

불공변 : 다른 타입 `Type1`과 `Type2`가 있을 때, `List<Type1>`은 `List<Type2>`의 하위타입도 아니고 상위 타입도 아니다.

배열은 런타임 중에 실패

```java
public static void main(String[] args) {
    Object[] objectArray = new Long[1];
    objectArray[0] = "타입이 달라 넣을 수 없다"; // ArrayStoreException을 런타임 중에 내뱉는다.
}
```

![Untitled](https://user-images.githubusercontent.com/49682056/224105695-fbd2bfda-e71c-4d7c-ae78-8e40fb557565.png)

제네릭은 컴파일 단계에서 에러가 난다.

```java
List<Object> ol = new ArrayList<Long>();
ol.add("타입이 달라 넣을 수 없다");
```

![Untitled 1](https://user-images.githubusercontent.com/49682056/224105685-b3ac44cf-75fe-4f87-a4c0-2aefa96c7c4a.png)

둘 다 Long용 저장소에 String을 넣을 수 없지만, 런타임보다는 컴파일 단계에서 바로 알 수 있는 것이 낫다.

**차이점 2. 배열은 실체화 되나 제네릭은 실체화 되지 않는다.**

배열은 실체화 된다는 말일 무슨 말일까?

배열은 런타임에도 자신이 담기로 한 원소의 타입을 인지하고 확인하다. 하지만 제네릭의 경우 런타임에는 타입 정보가 소거된다. (소거는 제네릭이 지원하기 전의 레거시 코드와 제네릭을 함께 사용할 수 있게 해주는 메커니즘)

즉, 원소 타입을 컴파일 타임에만 검사하며 런타임에는 알 수 없다.

**위 두 가지 차이점으로 인해 배열과 제네릭은 잘 어울리지 않는다.**

배열은 제네릭 타입, 매개변수화 타입, 타입 매개변수로 사용할 수 없다. (`new List<E>[]`, `new List<String[]`, `new E[]`)

⇒ 이유는  타입 안전하지 않기 때문에(ClassCastException이 발생할 수 있다)

### 배열을 제네릭으로 만들 수 없어 불편한 경우

1. 제네릭 컬렉션에서 자신의 원소타입을 담은 배열을 반환하는 경우 불가능
2. 제네릭 타입과 가변인수 메서드를 함께 사용하는 경우 (실체화 불가 타입 경고) ⇒ `@SafeVarags` 로 대처 가능

### 배열 `E[]` 대신 컬렉션인 `List<E>`를 사용하자

배열로 형변환 할 때 제네릭 배열 생성 오류나 비검사 형변환 경고를 해결 할 수 있다.

코드가 조금 복잡해지고 성능은 나빠질 수도 있지만, 타입 안전성과 상호운용성이 좋아진다.

### 배열을 컬렉션으로 바꾸는 방법을 예제를 통해 알아보자

생성자에서 컬렉션을 받는 Chooser 클래스

컬렉션 안의 원소 중 하나를 무작위로 반환하는 choose메서드를 제공한다.

1. **제네릭 사용 전의 코드**

```java
public class Chooser {
    private final Object[] choiceArray;

    public Chooser(Collection choiceArray) {
        this.choiceArray = choiceArray.toArray();
    }
    
    // 반환된 Object 타입을 형변환 하여 사용해야 한다.
    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
```

이 클래스를 사용하려면 choose 메서드를 호출 할 때 마다 반환된 Object 타입을 형변환 하여 사용해야 한다.

다른 타입의 원소가 있다면 형변환 오류가 발생한다.

1. **제네릭 적용**

```java
public class Chooser<T> {
    private final T[] choiceArray;

    public Chooser(Collection<T> choiceArray) {
        this.choiceArray = (T[]) choiceArray.toArray();
    }

    //반환된 Object 타입을 형변환 하여 사용해야 한다.
    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
```

![Untitled 2](https://user-images.githubusercontent.com/49682056/224105693-5738b178-3e7e-47f0-bb7b-2d5d4c53bc5b.png)

T가 무슨 타입인지 모르므로 컴파일러는 이 형 변환이 런타임에도 안전한지 보장 할 수 없어 경고를 내게 된다.

1. **컬렉션 리스트로 바꿔보자**

```java
public class Chooser<T> {
    private final List<T> choiceList;

    public Chooser(Collection<T> choices) {
        this.choiceList = new ArrayList<>(choices);
    }

    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceList.get(rnd.nextInt(choiceList.size()));
    }
}
```

배열을 리스트로 바꿈으로써 비검사 형변환 경고를 없앨 수 있다. 런타임 중에 ClassCastExcetption을 만날 일은 없다.

### 정리

**배열 : 공변, 실체화 가능, 컴파일 단계에서 타입 안전성 X, 런타임에는 타입 안전성 O**

**제네릭 : 불공변, 실체화 X, 런타임 중에 타입 정보 소거, 컴파일 단게에서는 타입 안전성 O, 런타임에서는 타입 안전성 X**

둘을 섞어 쓰다가 컴파일 오류나 경고를 만나면, 가장 먼저 배열에서 리스트로 변경하는 방법을 적용해보자