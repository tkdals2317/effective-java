# item 65. 리플렉션보다는 인터페이스를 사용하라

<aside>
💡 리플렉션에 대해 알아보고 리플렉션의 단점을 알아보자

</aside>

### 리플렉션

리플렉션 기능을 이용하면 프로그램에서 임의의 클래스에 접근할 수 있다.

Class 객체가 주어지면 그 클래스의 생성자, 메서드, 필드에 해당하는 Constructor, Method, Field 인스턴스를 가져올 수 있고, 이어서 인스턴스들로는 그 클래스의 멤버 이름, 필드 타입, 메서드 시그니처 등을 가져올 수 있다.

나아가 Constructor, Method, Field의 인스턴스를 이용해 각각에 연결된 실제 생성자, 메서드, 필드를 조작할 수 있다.

예를 들어 Method.invoke는 어떤 클래스의 어떤 객체가 가진 어떤 메서드라도 호출 할 수 있게 해준다.

리플렉션을 이용하면 컴파일 당시에 존재하지 않던 클래스도 이용할 수 있다.

### 리플렉션의 단점

- **컴파일타임 타입 검사가 주는 이점을 하나도 누릴 수 없다.**
    
    프로그램이 리플렉션을 사용해서 존재하지 않는 혹은 접근할 수 없는 메서드를 호출하려 시도한다면 런타임 오류가 발생한다.
    
- **리플렉션을 이용하면 코드가 지저분하고 장황해진다.**

- **성능이 떨어진다.**
    
    리플렉션을 통한 메서드 호출은 일반 메서드 호출보다 훨씬 느리다.
    

### 리플렉션을 사용해야 할 상황

코드 분석 도구나 의존관계 주입 프레임워크처럼 리플렉션을 써야하는 복잡한 애플리케이션이 몇가지 있다.

하지만 단점이 명확하기에 그런 프레임워크도 리플렉션의 사용을 줄이고 있다.

또는 런타임에 존재하지 않을 수도 있는 다른 클래스, 메서드, 필드와의 의존성을 관리할 때 적합하다.

하지만 이 경우에는 메서드가 런타임에 존재하지 않을 수 있다는 걸 감안하여야 한다.

아마도 일반적인 상황에서는 리플렉션이 필요없을 가능성이 높다.

**리플렉션은 아주 제한된 형태로만 사용해야 그 단점을 피하고 이점만 취할 수 있다.**

### **리플렉션은 인스턴스 생성에만 쓰고, 이렇게 만든 인스턴스는 인터페이스나 상위 클래스로 참조해 사용하자**

컴파일타임에 이용할 수 없는 클래스를 사용해야만 하는 프로그램은 비록 컴파일타임이라도 적절한 인터페이스나 상위 클래스를 이용할 수 있을 것이다.

다행히 이런 경우라면 **리플렉션은 인스턴스 생성에만 쓰고, 이렇게 만든 인스턴스는 인터페이스나 상위 클래스로 참조해 사용하자**

아래 예제 소스를 보면서 이해해보자

```java
// 코드 65-1 리플렉션으로 생성하고 인터페이스로 참조해 활용한다.
public static void main(String[] args) {
    // 클래스 이름을 Class 객체로 변환
    Class<? extends Set<String>> cl = null;
    try {
        cl = (Class<? extends Set<String>>) Class.forName(args[0]);
    } catch (ClassNotFoundException e) {
        fetalError("클래스를 찾을 수 없습니다.");
    }

    // 생성자를 얻는다.
    Constructor<? extends Set<String>> cons = null;
    try {
        cons = cl.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
        fetalError("매개변수가 없는 생성자를 찾을 수 없습니다.");
    }

    // 집합의 인스턴스를 만든다.
    Set<String> s = null;
    try {
        s = cons.newInstance();
    } catch (IllegalAccessException e) {
        fetalError("생성자에 접근할 수 없습니다.");
    } catch (InstantiationException e) {
        fetalError("클래스를 인스턴스화 할 수 없습니다.");
    } catch (InvocationTargetException e) {
        fetalError("생성자가 예외를 던졌습니다." + e.getCause());
    } catch (ClassCastException e) {
        fetalError("Set을 구현하지 않은 클래스입니다.");
    }

    // 생성한 집합을 사용한다.
    s.addAll(Arrays.asList(args).subList(1, args.length));
    System.out.println(s);
}

private static void fetalError(String msg) {
    System.err.println(msg);
    System.exit(1);
}
```

`Set<String>` 인터페이스의 인스턴스를 생성하는데, 정확한 클래스는 명령줄의 첫 번째 인수로 확정한다.

그리고 생성한 집합에 두번 째 이후의 인수들을 추가한 다음 화면에 출력한다.

이 기능을 통해 Set 규약을 잘 지키는 지 검사해볼 수 있고 제네릭 집합 성능 분석도구로 활용할 수 있다.

대부분의 경우 리플렉션 기능은 이정도만 사용해도 충분하다.

HashSet을 사용한 경우

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/378ee86b-0a0c-4676-8065-3f6c0ad4f9c9)

![Untitled 1](https://github.com/tkdals2317/effective-java/assets/49682056/ae5a4e4d-e3c0-43a2-8f1f-695f55ed853b)

TreeSet 클래스를 사용한 경우 

![Untitled 2](https://github.com/tkdals2317/effective-java/assets/49682056/679a2f3a-402f-40ed-bd1c-3da43031d3c1)

![Untitled 3](https://github.com/tkdals2317/effective-java/assets/49682056/01a44fb3-5b88-4e4f-a5cd-a454c98d3fe5)

**위 코드의 단점**

1. 런타임에 총 여섯가지나 되는 예외를 던질 수 있다.
    
    리플렉션이 아니였다면 컴파일타임에 다 잡아낼 수 있는 예외들이다.
    
2. 클래스 이름만으로 인스턴스를 생성하기 위해 무려 25줄이나 되는 코드를 작성했다.
    
    리플렉션이 아니였다면 생성자 호출 한줄로 끝났다.
    
3. 비검사 형변환 경고가 뜬다.
    
    형변환은 명시한 클래스가 Set을 구현하지 않았더라도 성공하나 인스턴스를 생성하려 할 때 `ClassCastException`을 던진다. 
    

리플렉션 예외를 각각 잡는 대신 모든 리플렉션 예외의 상위 클래스인 `ReflectiveOperationException` 을 잡도록하여 코드 수를 줄일 수 있다.

### 정리

리플렉션은 복잡한 특수 시스템을 개발할 때 필요한 강력한 기능이지만 단점이 많다.

컴파일타임에는 알 수 없는 클래스를 사용하는 프로그램을 작성한다면 리플렉션을 사용해야 할 것이다.

단, 되도록 객체 생성에만 사용하고, 생성한 객체를 이용할 때는 적절한 인터페이스나 컴파일타임에 알 수 있는 상위 클래스로 형변환하여 사용하자