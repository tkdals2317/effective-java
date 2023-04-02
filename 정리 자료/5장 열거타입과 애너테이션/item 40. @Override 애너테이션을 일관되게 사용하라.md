# item 40. @Override 애너테이션을 일관되게 사용하라

<aside>
💡 @Override 애너테이션의 중요성의 예를 통해 알아보자

</aside>

### `@Override` 애너테이션

- 메서드 선언에만 달 수 있다.
- 상위 타입의 메서드를 재정의했음을 뜻한다.
- 이 애너테이션을 일관되게 사용하면 여러 가지 악명 높은 버그들을 예방해준다.

### `@Override` 애너테이션을 달지 않았을 때 생기는 문제점

```java
// 코드 40-1 영어 알파벳 2개로 구성된 문자열을 표현하는 클래스 - 버그를 찾아보자
public class Bigram {

    private final char first;

    private final char second;

    public Bigram(char first, char second) {
        this.first = first;
        this.second = second;
    }

    public boolean equals(Bigram b) {
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        HashSet<Bigram> bigrams = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                bigrams.add(new Bigram(ch, ch));
            }
        }
        System.out.println(bigrams.size()); // 26을 기대하지만 260이 출력된다!
    }
}
```

위 코드에서 Set은 중복을 허용하지 않으니 26을 출력할 것을 기대하지만, 실제로는 260이 출력된다.

equals와 hashcode 메서드를 **재정의(overriding)**한 것이 아닌 **다중정의(overloading)**을 했기 때문이다.

즉, Object의 equals와 별개인 새로운 equals 메서드를 새로 정의한 것이다.

```java
@Override
public boolean equals(Object o) {
    if (!(o instanceof Bigram)) {
        return false;
    }
    Bigram b = (Bigram) o;
    return b.first == first && b.second == second;
}
```

정상적으로 동작하기 위해서는 `@Override` 애너테이션을 붙혀주고 Object의 equals의 파라미터와 동일한 Object 타입을 매개변수로 받아 타입 확인 후 형변환하여 비교를 해야한다.

### 상위 클래스의 메서드를 재정의하려는 모든 메서드에 `@Override` 애너테이션을 달자.

예외는 한 가지뿐이다.

구체 클래스에서 상위 클래스의 추상 메서드를 재정의할 때는 굳이 `@Override` 를 달지 않아도 된다.

구체 클래스인데 아직 구현하지 않은 추상 클래스가 남아있다면 컴파일러가 그 사실을 바로 알려주기 때문이다.

### 클래스 뿐만 아니라 인터페이스의 메서드를 재정의할 때도 사용할 수 있다.

디폴트 메서드를 인터페이스도 지원하게 됬으므로 인터페이스 메서드를 구현한 메서드에도 `@Override` 메서드를 다는 습관을 들이면 좋다.

### 정리

재정의한 모든 메서드에 `@Override` 애너테이션을 의식적으로 달면 개발 중 실수를 컴파일러가 바로 알려줄 것이다.

거의 모든 상황에서 재정의한 메서드는 `@Override` 애너테이션을 달자