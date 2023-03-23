# item 27. 비검사 경고를 제거하라

### 비검사 경고의 종류

제네릭을 사용하면 수많은 컴파일러 경고를 만나게 된다.

- 비검사 형변환 경고
- 비검사 메서드 호출 경고
- 비검사 매개변수화 가변 인수 타입 경고
- 비검사 변환 경고

![Untitled](https://user-images.githubusercontent.com/49682056/223996891-0d50a9b2-4262-4393-8007-674d4b786e2c.png)

컴파일러가 알려준다.

위 경고는 자바 7부터 지원하는 `<>`연산자만으로 해결 할 수있다.

```java
Set<Lark> exaltation = new HashSet<>();
```

### 할 수 있는 모든 비검사 경고를 제거하라

비검사 경고를 제거하면 타입안전성을 보장할 수 있다.

### **경고를 제거할 수는 없지만 타입 안전하다고 확신 할 수 있다면 `@SuppressWarnings("unchecked")` 애너테이션으로 경고를 숨길 수 있다.**

경고를 숨길 수 는 있지만 런타임 환경에 ClassCastException을 던질 수 있다는 점을 주의 하자

`**@SuppressWarnings("unchecked")` 는 개별 지역변수 선언부터 클래스 전체까지 어떤 선언에도 달 수 있지만항상 가능한 한 좁은 범위에 적용하자**

보통은 변수 선언, 아주 짧은 메서드, 생성자가 될 것이다.

![Untitled 1](https://user-images.githubusercontent.com/49682056/223996880-72eff6a9-8f75-476d-b9a0-e39a46e4ba64.png)

위 코드는 ArrayList의 toArray 메서드로 `@SuppressWarnings("unchecked")` 가 달려있다.

위 코드를 가능한 좁은 범위로 `@SuppressWarnings("unchecked")` 를 달고 싶지만 선언이 아닌 return문에는 어노테이션을 달 수 없다.

대신 return하는 객체를 지역변수로 선언하여 애너테이션을 달면 범위를 최소화 할 수 있다.

```java
public <T> T[] toArray(T[] a) {
    if (a.length < size) {
        // Make a new array of a's runtime type, but my contents:
        @SuppressWarnings("unchecked") T[] result = (T[]) Arrays.copyOf(elementData, size, a.getClass());
        return result;
    }
    System.arraycopy(elementData, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;
    return a;
}
```

**`@SuppressWarnings("unchecked")` 애너테이션을 사용할 때면 그 경고를 무시해도 안전한 이유를 항상 주석으로 남기자**

### 정리

비검사 경고를 무시하면 런타임 환경에서 ClassCastException이 일어날 수 있으니 최대한 없애자

만약 경고를 없앨 방법을 찾지 못했다면, 그 코드가 안전함을 증명하고 가능한 한 범위를 좁혀 `@SuppressWarnings("unchecked")` 애너테이션으로 경고를 숨기고 경고를 숨기로한 근거를 주석으로 남기자