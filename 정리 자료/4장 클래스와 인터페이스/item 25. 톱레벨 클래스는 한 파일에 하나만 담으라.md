# item 25. 톱레벨 클래스는 한 파일에 하나만 담으라

<aside>
💡 톱레벨 클래스를 한 파일에 여러개를 담을 시 발생할 수 있는 문제점을 알아보자

</aside>

### 톱레벨 클래스를 한 파일에 담으면 생기는 문제점

소스 파일 하나에 톱레벨 클래스를 여러 개 선언하더라도 컴파일 단계에서는 문제가 생기지 않을 수 있다.

하지만 이렇게 하면 한 클래스를 여러 가지로 정의할 수 있으며, 그 중 어느 것을 사용할 지는 어는 소스파일을 먼저 컴파일 하냐에 따라 달라진다.

다음 코드는 Main 클래스 하나를 담고 있고 Main 클래스는 다른 톱레벨 클래스 2개(Utensil과 Dessert)를 참조한다.

```java
public class Main {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
}
```

집기(Utensil)와 디저트(Dessert) 클래스가 Utensil.java라는 한 파일에 정의되어 있다고 해보자

```java
class Utensil {
    static final String NAME = "pan";
}

class Dessert {
    static final String NAME = "cake";
}
```

Main을 실행하면 pancake를 출력한다.

우연히 똑같은 두 클래스를 담은 Dessert.java라는 파일을 만들었다고 해보자

```java
class Utensil {
    static final String NAME = "pot";
}

class Dessert {
    static final String NAME = "pie";
}
```

운좋게 `javac Main.java Dessert.java` 명령으로 컴파일하면 컴파일 오류가 나고 Utensil과 Dessert 클래스를 중복 정의했다고 알려줄 것이다. 

컴파일러는 Main.java를 컴파일하고, 그 안에서 Utensil 참조를 만나면 Utencil.java 파일을 살펴 Utensil과 Desert를 모두 찾아 낼 것이다.

그런 다음 두번 째 명령줄 인수로 넘어온 Desset.java를 처리하려 할때 같은 클래스의 정의가 이미 있음을 알게된다.

하지만 javac [Main.java](http://Main.java)나 javac Main.java [Utensil.java](http://Utensil.java) 명령으로 컴파일 하면 Dessert.java를 작성하기 전 처럼 pancake를 출력한다.

그러나 javac [Dessert.java](http://Dessert.java) [Main.java](http://Main.java) 명령으로 컴파일하면 potpie를 출력한다.

⇒ 컴파일 순서에 따라 동작이 달라진다.

### 해결책

단순히 톱레벨 클래스들을 서로 다른 소스 파일로 분리하자 

굳이 톱레벨 클래스를 한 파일에 담고 싶다면 정적 멤버 클래스를 사용해보자

읽기 편하고 prvate으로 선언하면 접근 법위도 최소로 관리할 수 있다.

```java
// 정적 멤버 클래스로 바꾼 예제
public class Main {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }

    private static class Utensil {
        static final String NAME = "pot";
    }

    private static class Dessert {
        static final String NAME = "tie";
    }
}
```

### 정리

소스 파일 하나에는 반드시 톱레벨 클래스(혹은 톱레벨 인터페이스)를 하나만 담자