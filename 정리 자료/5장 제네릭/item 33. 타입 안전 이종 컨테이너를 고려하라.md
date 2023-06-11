# item 33. 타입 안전 이종 컨테이너를 고려하라

<aside>
💡 여러개의 타입 매개변수를 넣을 수 있는 타입 안전 이종 컨테이너에 대해 알아보자

</aside>

### 단일원소 컨테이너

`Set<E>`, `Map<K, V>` 등의 컬렉션과 같이 매개변수화 되는 대상은 원소가 아닌 컨테이너 자신이다.

따라서 하나의 컨테이너에서 매개변수화할 수 있는 타입의 수가 제한된다.

쉽게 말해 `Set<Integer>`에는 Integer 하나의 타입 매개변수만 넣을 수 있고, `Map<String, Integer>`으로는 Key값에는 String 타입매개변수만 value 값으로는 Integer 타입 매개변수만 받을 수 있다.

이를 단일 원소 컨테이너라고 한다.

### 타입 안전 이종 컨테이너란?

제네릭을 활용하여 컨테이너 대신 키를 매개변수화하여 컨테이너에 값을 넣거나 뺄때 매개변수화한 키를 함께 제공하여 제네릭 타입 시스템 값의 타입이 키와 같음을 보장해주는 컨테이너를 타입 안전 이종 컨테이너라고 한다.

말이 너무 어렵다.

하나씩 뜯어서 이해한 바로 설명해보겠다.

(1) 제네릭을 활용하여 컨테이너 대신 키를 매개변수화하고 

⇒ Set<Integer>의 <> 안의 타입 매개변수인 Integer를 매개변수화 한다. (`Integer.class` 를 파라미터로 넘긴다.) 

(2) 컨테이너에 값을 넣거나 뺄때 매개변수화한 키를 함께 제공하여

⇒ `favorites.putFavorite(String.class, "Java");` 

⇒ `String favoriteString = favorites.getFavorite(String.class);`

(3) 제네릭 타입 시스템 값의 타입이 키와 같음을 보장해주는 컨테이너를 타입 안전 이종 컨테이너라고 한다.

⇒ 한 개의 컨테이너에 여러개의 매개변수화 타입을 담을 수 있고 타입 안전성이 있는 컨테이너를 타입 안전 이종 컨테이너라 불른다. 

⇒ `favorites.putFavorite(Long.class, "문자열을 넣으면 컴파일 에러가 난다!");`

### Favorites 클래스 예제로 보는 타입 안전 이종 컨테이너 패턴

```java
// 코드 33-3 타입 안전 이종 컨테이너 패턴 - 구현
public class Favorites {
    private Map<Class<?>, Object> favorites = new HashMap<>();

    // 코드 33-4 동적 형변환으로 런타임 타입 안전성 확보
    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), type.cast(instance));
    }

    public <T> T getFavorite(Class<T> type) {
        return type.cast(favorites.get(type));
    }
}
```

```java
// 코드 33-2 타입 안전 이종 컨테이너 패턴 - 클라이언트
public static void main(String[] args) {
    Favorites favorites = new Favorites();
    favorites.putFavorite(String.class, "Java");
    favorites.putFavorite(Integer.class, 9);
    favorites.putFavorite(Class.class, Favorites.class);
    
		// favorites.putFavorite(Long.class, "문자열을 넣으면 컴파일 에러가 난다!");
    String favoriteString = favorites.getFavorite(String.class);
    Integer favoriteInteger = favorites.getFavorite(Integer.class);
    Class<?> favoriteClass = favorites.getFavorite(Class.class);

    System.out.printf("%s %x %s%n", favoriteString, favoriteInteger, favoriteClass);
}
```

위 코드에서 두 가지를 짚고 넘어가자

1. **Favorites가 사용하는 private 멤버 변수인 favorites의 타입은 `Map<Class<?>, Object>`이다.**
    
    ⇒ 키값을 와일드 카드 타입으로 `Class<?>`로 사용함으로서 다양한 타입을 지원하는 힘이 여기서 나온다.
    
2. **두번 째 favorites 맵의 값 타입은 단순히 Objects다.**
    
    ⇒ 키와 값 사이의 타입 관계를 보증하지 않는다. 즉, 모든 값이 키로 명시한 타입임을 보증하지 않는다.
    
    ⇒ 하지만 이 관계가 성립함을 알고 있고, 어떤 타입의 값도 넣을 수 있게 해준다.
    

여기서 putFavorite보다 getFavorite 메서드를 더 중요하다고 설명한다.

Class 객체에 해당하는 값을 맵에서 꺼낼 때 꺼낸 객체가 반환해야할 객체는 맞지만, **잘못된 컴파일 타입을 가지고 있다. (Object 타입)**

그래서 우리는 Class 객체에 맞는 타입인 T 타입으로 **형변환**을 해줘야 하는데 이때 **동적인 형변환 메서드인 cast 메서드**를 사용한다.

사실 클라이언트 코드가 깔끔히 컴파일 된다면 getFavorite이 호출하는 cast가 발생하는 ClassCastException은 볼 일 없을 것이다.

키값과 다른 타입의 인스턴스를 넣으면 컴파일 단계에서 에러가 나기 때문이다.

![Untitled](https://user-images.githubusercontent.com/49682056/226186493-dde095b4-0277-4659-95e8-29ff222a0ddb.png)

그렇다면 cast를 사용하는 이유는 무엇일까?

![Untitled 1](https://user-images.githubusercontent.com/49682056/226186479-b9b40665-9f93-4064-8bab-3198d788231c.png)

cast 메서드의 시그니처가 “Class 클래스가 제네릭이라는 이점”을 완벽히 활용하기 때문이다.

![Untitled 2](https://user-images.githubusercontent.com/49682056/226186480-3ae54ef5-96a4-474c-8cc7-45c2dc1c4796.png)

Class의 cast 메서듸 반환 타입은 Class 객체의 타입 매개변수와 같다.

### Favorites 클래스에서 알아두어야 할 제약 두가지

1. **악의적인 클라이언트가 Class 객체를 (제네릭이 아닌) 로 타입으로 넘기면 Favorites 인스턴스의 타입 안전성이 쉽게 깨진다.**

하지만 비검사 경고가 뜬다.

런타임 환경에서 안전성을 얻으려면 putFavorite 메서드에서 인수로 주어진 instanse의 타입이 type으로 명시한 타입이 같은지 확인만 하면 된다.

```java
// 코드 33-4 동적 형변환으로 런타임 타입 안전성 확보
public <T> void putFavorite(Class<T> type, T instance) {
    favorites.put(Objects.requireNonNull(type), type.cast(instance));
}
```

이를 잘 활용한 사례가 Collections에 있는 checkedSet, checkedList, checkedMap 같은 메서드이다.

아래는 CheckedMap의 put 메서드이다.

![Untitled 3](https://user-images.githubusercontent.com/49682056/226186483-b8baae57-5dca-47ea-9fde-75fea0dd9cc2.png)

typeCheck 메서드로 타입을 체크하고 있는 것을 알 수 있다.

![Untitled 4](https://user-images.githubusercontent.com/49682056/226186484-d58f2193-1512-45fb-9c58-608d86173354.png)

checkedList 또한 상속받은 `CheckedCollection` 의 (위에 설명처럼 cast 메서드를 통하지는 않았어도) typeCheck 메서드를 통해 add할 때 타입 체크를 하고 값을 추가한다.

![Untitled 5](https://user-images.githubusercontent.com/49682056/226186485-5cff0a5b-9b7c-4cba-9c90-4c977f726057.png)

![Untitled 6](https://user-images.githubusercontent.com/49682056/226186486-a349c4bd-870b-4214-9744-4f2eb6f40957.png)

이 타입 체크 메서드 덕분에 런타입 시 다른 타입의 인스턴스를 추가하려하면 ClassCastException을 던진다.

```java
// checkedMap 사용 예제
Map<Integer, String> map = new HashMap<>();
map.put(1, "one");
System.out.println(map);

Map map2 = map;
map2.put("two", 2);
System.out.println(map2); 

output
{1=one}
{1=one, two=2} //?????

Map<Integer, String> map = new HashMap<>();
map = Collections.checkedMap(map, Integer.class, String.class);
map.put(1, "one");
System.out.println(map);

Map map2 = map;
map2.put("two", 2); // ClassCastException
System.out.println(map2);

output 
Exception in thread "main" java.lang.ClassCastException: Attempt to insert class java.lang.String key into map with key type class java.lang.Integer
	at java.base/java.util.Collections$CheckedMap.typeCheck(Collections.java:3692)
	at java.base/java.util.Collections$CheckedMap.put(Collections.java:3738)
	at org.example.item33.item33.main(item33.java:39)
```

1. **실체화 불가 타입에는 사용할 수 없다**

즉, `String`이나 `String[]`은 저장 할 수 있어도 `List<String>`은 저장할 수 없다.

`List<String>`의 Class 객체를 얻을 수 없기 때문이다.

만약 `List<String>.class`와 `List<Integer>`를 허용하면, 둘다 `List.class`라는 같은 Class 객체를 공유하므로 문제가 생긴다.

하지만 수퍼 타입 토큰으로 해결 하려는 시도도 있다.

스프링 프레임워크에서 `ParameterizedTypeReference`라는 클래스로 미리 구현해두었다.

![Untitled 7](https://user-images.githubusercontent.com/49682056/226186488-2ed74c60-df89-434c-bf88-7cc776f6d0c2.png)

![Untitled 8](https://user-images.githubusercontent.com/49682056/226186490-18f4953f-27de-4d66-b58d-3c06ae13540c.png)

이 `ParameterizedTypeReference` 가 눈에 익었던 이유는 우리 솔루션에서 사용하는 RestTemplate에서 exchage 메서드에서 json 반환값을 어떤 객체 형태로 받을 지 넘기는 파라미터로 쓰였다.

### 타입을 제한한 타입 안전 이종 컨테이너

한정적 타입 토큰을 활용하면 가능하다.

한정적 타입 토큰이란 단순히 한정적 타입 매개변수나 한정적 와일드카드를 사용하여 표현 가능한 타입을 제한하는 타입 토큰이다.

```java
// AnnotatedElement 인터페이스에 선언된 메서드
public <T extends Annotation> T getAnotation(Class<T> annotationType);
```

위 메서드는 대상 요소에 달려있는 애너테이션을 런타임에 읽어오는 기능을 한다.

여기서 annotationType 인수는 애너테이션 타입을 뜻하는 한정적 타입 토큰이다.

이 메서드는 토큰으로 명시한 타입의 애너테이션이 대상 요소에 달려 있다면 해당 애너테이션을 반환하고, 없다면 null을 반환한다.

![Untitled 9](https://user-images.githubusercontent.com/49682056/226186491-b37c344f-a297-4152-98d4-638addc32a57.png)

즉, 애너테이션된 요소는 그 키가 애너테이션 타입인, 타입 안전 이종 컨테이너라고 볼 수 있다.

### 정리

타입 안전 이종 컨테이너는 Class를 키로 쓰며, 이런 식으로 쓰이는 Class 객체를 타입 토큰이라고 한다.

또한 직접 구현한 키 타입도 쓸 수 있다.