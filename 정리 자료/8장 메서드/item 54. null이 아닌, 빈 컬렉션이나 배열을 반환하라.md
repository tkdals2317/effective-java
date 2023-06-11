# item 54. null이 아닌, 빈 컬렉션이나 배열을 반환하라

<aside>
💡 null을 반환했을 때 문제점과 빈 컬렉션이나 빈 배열을 반환하는 올바른 방법을 알아보자

</aside>

### null을 반환하는 코드의 문제점

```java
public class ShopV1 {

    private final List<Cheese> cheesesInStock = new ArrayList<>() {};

    // 코드 54-1 컬렉션이 비었으면 null을 반환한다.
    /**
     * @return 매장 안의 있는 모든 치즈 목록을 반환한다.
     *         단, 제고가 하나도 없다면 null을 반환한다.
     */
    public List<Cheese> getCheeses(){
        return cheesesInStock.isEmpty() ? null : new ArrayList<>(cheesesInStock);
    }

}
```

위 코드처럼 작성하게 되면 클라이언트는 항상 null 방어 코드를 작성해야 한다.

```java
ShopV1 shopV1 = new ShopV1();
List<Cheese> cheeses = shopV1.getCheeses();

if (cheeses != null && cheeses.contains(Cheese.STILTON)) { // null 방어 코드를 작성해야 한다.
    System.out.println("좋았어, 바로 그거야.");
}
```

빈 컨테이너를 할당하는 것도 비용이 드니 null을 반환하는 쪽이 낫다는 주장도 있지만 성능 차이는 거의 없을 뿐더러 빈 컬렉션과 빈 배열은 굳이 새로 할당하지 않고 반환할 수 있다.

### 빈 컬렉션을 반환하자

빈 컬렉션을 반환하는 전형적인 코드이다.

```java
// 코드 54-2 빈 컬렉션을 반환하는 올바른 예
public List<Cheese> getCheesesV1(){
    return new ArrayList<>(cheesesInStock);
}
```

가능성은 적지만 사용 패턴에 따라 빈 컬렉션 할당이 성능을 눈에 띄게 떨어 뜨릴 수 있다.

하지만 해법은 간단하다. `Collections.emptyList` 메서드로 매번 똑같은 빈 ‘불변’ 컬력션을 반환하면 된다. 

```java
// 코드 54-3 최적화 - 빈 컬렉션을 매번 새로 할당하지 않도록 했다.
public List<Cheese> getCheesesV2(){
    return cheesesInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheesesInStock);
}
```

맵이 필요하면 `Collections.emptyMap`을 사용하면 된다.

반드시 할 필요는 없으니 최적화가 필요할 때만 사용하자.

### 빈 배열을 반환해보자

배열을 쓸 때도 마찬가지로 null을 반환하는 대신 길이가 0인 배열을 반환하자.

```java
// 코드 54-4 길이가 0일 수도 있는 배열을 반환하는 올바른 방법
public Cheese[] getCheesesArrV1() {
    return cheesesInStock.toArray(new Cheese[0]);
}
```

위 코드에서 toArray 메서드에 건낸 길이 0짜리 배열은 우리가 원하는 반환 타입(Cheese[])을 알려주는 역할을 한다.

이 방식이 성능을 떨어뜨릴 것 같다면 길이가 0인 배열을 미리 선언해두고 매번 그 배열을 반환하면 된다.

길이가 0인 배열은 모두 불변이기 때문이다.

```java
// 코드 54-5 최적화 - 빈 배열을 매번 새로 할당하지 않도록 했다.
private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

public Cheese[] getCheesesArrV2() {
    return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
}
```

이 최적화 버전의 getCheesesArrV2는 항상 `EMPTY_CHEESE_ARRAY`를 인수로 넘겨 toArray를 호출한다.

따라서 cheeseInStock이 비었을 때면 언제나 `EMPTY_CHEESE_ARRAY`를 반환한다.

성능을 개선할 목적이라면 toArray에 넘기는 배열을 미리 할당하는 것은 오히려 성능이 떨어진다는 연구결과가 있어 추천하지 않는다.

```java
// 코드 54-6 나쁜 예 - 배열을 미리 할당하면 성능이 나빠진다.
public Cheese[] getCheesesArrV3() {
    return cheesesInStock.toArray(new Cheese[cheesesInStock.size()]);
}
```

### 정리

**null이 아닌 빈 배열이나 컬렉션을 반환하라.** 

null을 반환하는 API는 사용하기 어렵고 오류 처리 코드도 늘어난다. 그렇다고 성능이 좋은 것도 아니다.