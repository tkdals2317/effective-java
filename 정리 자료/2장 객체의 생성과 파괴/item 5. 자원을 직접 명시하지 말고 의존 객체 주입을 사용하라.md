# item 5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

<aside>
💡 하나의 클래스에서 하나 이상의 자원을 사용할 때 예시 코드를 보며 **의존 객체를 주입받아 사용 할 때의 이점에 대해 알아보자**

</aside>

### 의존 객체 주입

맞춤범 검사기라는 정적 유틸리티 클래스를 예를 들어보자

이 맞춤법 검사기는 사전(Lexicon)에 의존한다

이런 정적 유티릴티 클래스를 만들 때 item4 인스턴스화를 방지하기 위해 아래와 같이 구현 한 코드를 보자

```java
public class SpellChecker {
	  private static final Lexicon dictionary = ...;

    private SpellChecker() {} // 인스턴스화 방지 

    public static boolean isVaild(String word) {...}
    public static List<String> suggestions(String typo) {...}
}
```

또한 비슷하게 item3 싱글턴으로 구현 한 코드도 살펴보자

```java
public class SpellChecker {
	  private final Lexicon dictionary = ...;
    
    private SpellChecker() {} // 인스턴스화 방지 
    public static SpellChecker INSTANCE = new SpellChecker(...); // 싱글턴을 만들기 위한 public static 필드
    
    public static boolean isVaild(String word) {...}
    public static List<String> suggestions(String typo) {...}
}
```

하지만 위 두 코드는 사전을 단 하나만 사용하므로 유연하지 못하다는 단점이 있다.

실전에서는 언어별 사전이 있을 수도 있고, 테스트를 하기 위한 테스트용 사전이 필요할 수 있다. SpellChecker는 하나의 dictionary만 사용할 수 있다는 것은 큰 단점으로 보이는 코드이다.

SpellChecker가 사전(Lexicon)에 의존하는 상황에서 다양한 사전(Lexicon)을 사용하기 위해 Lexicon 필드의 final을 제거하고 다른 사전으로 교체하는 메서드를 추가하여 사용할 수 있지만

이 방식은 오류를 내기 쉽고 멀티스레드 환경에서는 사용 할 수 없다. 한 사용자가 SpellChecker를 사용하는 중간에 다른 사용자가 SpellChecker의 사전을 바꿔버린다면 원하는 동작을 하지 않을 것이다.

**이렇게 사용하는 자원에 따라 (의존하는 객체에 따라) 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.**

대신 클래스(SpellChecker)가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원(사용자가 원하는 사전)을 사용해야 한다.

이런 조건을 만족하는 간단한 패턴이 바로 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 **의존 객체 주입 방식**이 있다.

아래 코드를 보면 이해하기 쉽다.

```java
public class SpellChecker {
		// 인터페이스화 또는 상속을 받아 사전을 여러 개 만들 수 있게 한다.
    private final Lexicon dictionary;

		// SpellChecker 생성자에서 사용자로부터 사전을 주입 받는다
    public SpellChecker(Lexicon dictionary){
    	this.dictionary = Objects.requireNotNull(dictionary);
    }

    public static boolean isVaild(String word) {...}
    public static List<String> suggestions(String typo) {...}
}
```

일반적으로 스프링 프레임워크를 이용한다면 의존성 주입을 공부할 때 많이 볼 수 있는 코드이다.

**장점**

- 불변 보장한다
- 여러 클라이언트가 의존 객체들을 안심하고 공유 할 수 있다(멀티 스레드 환경)
- 의존 객체 주입은 생성자 뿐만 아니라, 정적 팩터리, 빌더 모두 똑같이 응용이 가능하다

이 패턴의 쓸만한 변형으로, 생성자에 자원을 생성하는 팩터리를 넘겨주는 방식(의존 객체 주입)이 있다

역시서 팩터리란 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다.

```java
public class TypeFactory {
    public Type createType(String type) {
        Type returnType = null;

        switch (type) {
            case "A":
                returnType = new TypeA();
                break;
            case "B":
                returnType = new TypeB();
                break;
            case "C":
                returnType = new TypeC();
                break;
        }
        return returnType;
    }

```

이런 방식은 팩터리 메서드 패턴을 구현한 것이라고 보면 된다.

대표적인 예가 Supplier<T> 인터페이스가 팩터리를 표현한 완벽한 예이다.

Supplier<T>를 입력으로 받는 메서드는 일반적으로 한정적 와일드카드 타입(모든 타입을 대신할 수 있는 타입 ex. ?)을 사용하여 팩터리의 타입 매개변수를 제한 하여야 한다.

이 방식을 사용해 클라이언트는 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩터리를 넘길 수 있다. (잘 이해가 가지 않는다)

**단점**

의존 객체 주입은 유연성과 테스트 용이성을 개선해주나, 의존성이 수 천개나 되는 큰 프로젝트에서는 오히려 코드를 어지럽게 만들기도 한다.

하지만 스프링 프레임워크에서 이런 의존 객체 주입을 아주 쉽게 해준다! 

`@Autowired` 어노테이션이나 의존 객체를 final로 만들고 생성자를 만들어준다.(`@RequiredArgsConstructor` 어노테이션을 쓰면 코드가 아주 간결해진다)

### 정리

클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.

그리고 필요한 자원을 클래스가 직접 만들게 해서도 안 된다.

대신 필요한 자원이나 그 자원을 만들어주는 팩터리를 의존 객체 주입 방식의 여러 방법(생성자, 빌더, 정적 팩터리)으로 주입 시켜주자

이 방법은 클래스의 유연성, 재사용성, 테스트 용이성을 기막히게 개선 해준다