# item 39. 명명 패턴보다 애너테이션을 사용하라

<aside>
💡 애너테이션을 사용한 JUnit의 테스트 애너테이션 코드를 보고 애너테이션 사용법을 알아보자

</aside>

### 명명 패턴의 단점

전통적으로 도구나 프레임워크가 특별히 다뤄야 할 프로그램 요소에는 딱 구분되는 명명 패턴을 적용해왔다.

예를 들어 테스트 프레임워크인 JUnit3 까지는 테스트메서드 이름을 test~로 시작하게끔 했다.

그럼 명명 패턴의 단점을 JUnit 3 버전을 예로 알아보자

1. **오타가 나면 안된다.** 
    
    오타가 나는 순간 JUnit3에서는 이 메서드를 무시하고 지나간다.
    
2. **올바른 프로그램 요소에서만 사용되리라 보증할 방법이 없다.**
    
    메서드가 아닌 클래스 이름을 TestSaftyMechanism로 지어 JUnit에 던져줬을 때, 개발자는 이 클래스에 정의된 테스트 메서드를 수행해주길 기대하지만 JUnit3은 메서드가 아닌 클래스라 테스트를 전혀 수행하지 않는다.
    
3. **프로그램 요소를 매개변수로 전달할 마땅한 방법이 없다.**
    
    특정 예외를 던져야 성공하는 테스트가 있으면, 예외의 이름을 테스트 메서드에 이름에 덧붙이는 방법도 있지만, 보기도 나쁘고 깨지기도 쉽다.
    
    컴파일러는 메서드 이름에 덧붙인 문자열이 예외를 가르키는지 알 도리가 없다.
    

### 애너테이션 활용법

1. **매개 변수가 없는 마커 애너테이션을 처리하는 프로그램**

**Test 애너테이션**

```java
/**
 * 코드 39-1 마커(marker) 애너테이션 타입 선언
 * 테스트 메서드임을 선언하는 애너테이션이다.
 * 매개변수 없는 정적 메서드 전용이다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
```

`@Test` 어노테이션에 달려있는 두 애너테이션에 대해 알아보자

두 애너테이션은 메타애너테이션이라 한다.

`@Retention(RetentionPolicy.RUNTIME)` 

- `@Test` 가 런타임에도 유지되어야 한다는 표시이다.
- 만약 이 메타에너테이션을 생략하면 테스트 도구는 `@Test` 를 인식할 수 없다.

`@Target(ElementType.METHOD)` 

- `@Test` 가 반드시 메서드 선언에서만 사용해야 한다고 알려준다.
- 따라서 클래스 선언이나, 필드 선언 등의 프로그램 요소에 사용할 수 없다.

주석의 의미

- 컴파일러가 매개변수 없는 정적 메서드라는 제약을 강제하면 좋겠지만, 그렇게 할려면 적절한 애너테이션 처리기를 직접 구현해야 한다. (관련 문서 : javax.annotation.processing API 문서)
- 적절한 애너테이션 처리기 없이 인스턴스 메서드나 매개변수있는 메서드에 달면 컴파일은 잘되나, 테스트 도구를 실행할 때 문제가 된다.

이와 같은 애너테이션을 “아무 매개변수 없이 단순히 마킹(marking)한다”라는 뜻에서 마커(marker) 애너테이션이라고 한다.

**테스트 코드**

```java
// 코드 39-2 마커 애너테이션을 사용한 프로그램 예
public class Sample {
    @Test public static void m1() {}        // 성공해야 한다.
    public static void m2() {}              // 테스트가 아니다.
    @Test public static void m3() {         // 실패해야 한다.
        throw new RuntimeException("실패");
    }
    public static void m4() {}              // 테스트가 아니다.
    @Test public void m5() {}               // 잘못 사용한 예 : 정적 메서드가 아니다
    public static void m6() {}              // 테스트가 아니다.
    @Test public static void m7() {         // 실패해야한다.
        throw new RuntimeException("실패");
    }
    public static void m8() {}              // 테스트가 아니다.
}
```

위에 주석에 설명해놓았듯이 m1은 성공 m3, m7은 실패, m5는 정적 메서드를 사용하지 않아 잘못 사용한 예, 그리고 나머지는 테스트가 아니다.

`@Test` 애너테이션은 Sample 클래스에 직접적인 영향을 주지는 않는다. 그저 애너테이션에 관심 있는 프로그램에게 추가 정보를 제공할 뿐이다.

즉, 대상 코드의 의미는 그대로 둔 채 그 애너테이션에 관심있는 도구에서 특별한 처리를 할 기회를 준다.

**Test 애너테이션 처리기**

```java
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]); // arg[0] : org.example.item39.Sample
        for (Method m : testClass.getDeclaredMethods()) { // 클래스에 정의된 메서드 리스트를 가져온다
            if (m.isAnnotationPresent(Test.class)) { // @Test 애너테이션만 붙어 있는 경우에만 아래 로직을 실행한다
                tests ++;
                try {
                    m.invoke(null); // 메서드를 실행한다.
                    passed++;
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause(); // 실패 정보를 추출한다.
                    System.out.println(m + " 실패: "+ exc);
                } catch (Exception exc) {
                    System.out.println("잘못 사용한 @Test:" + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);

    }
}
```

**소스 코드 설명** 

명령줄로부터 완전 정규화 된 클래스 이름을 받아, 그 클래스에서 `@Test` 애너테이션이 달린 메서드를 차례로 호출한다.

`isAnnotationPresent` 가 실행할 메서드를 찾아주는 메서드이다.

테스트 메서드가 예외를 던지면 리플렉션 메커니즘이 `InvocationTargetException` 을 던지고 catch에서 잡아 예외에 담긴 실패 정보를 추출해 출력한다.

**실행 결과**

![Untitled](https://user-images.githubusercontent.com/49682056/229296014-5f283bd4-2a8b-4e91-b0e8-f98e9d0de583.png)

![Untitled 1](https://user-images.githubusercontent.com/49682056/229296010-32aa6995-fbe3-434f-869a-1c6f6c8ecffb.png)
---

1. **매개변수 하나를 받는 애너테이션 타입을 처리하는 프로그램**

**ExceptionTest 애너테이션**

```java
/**
 * 코드 39-4 매개변수 하나를 받는 애너테이션 타입
 * 명시한 예외를 던져야만 성공하는 테스트 메서드용 애너테이션
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}
```

애너테이션의 매개변수 타입은 `Class<? extends Throwable>`이다.

⇒ Throwable을 확장한 클래스의 클래스 객체, 따라서 모든 예외와 오류 타입을 수용한다.

**테스트 코드**

```java
// 코드 39-5 매개변수 하나짜리 애너테이션을 사용하는 코드
public class Sample2 {

    @ExceptionTest(ArithmeticException.class)
    public static void m1() { // 성공해야 한다.
        int i = 0;
        i = i / i;
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m2() { // 실패해야 한다. (다른 예외 발생)
        int[] a = new int[0];
        int i = a[1];
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m3() {} // 실패해야 한다. (예외가 발생하지 않음)

}
```

**ExceptionTest 애너테이션 처리기**

```java
// 코드 39-5 매개변수 하나짜리 애너테이션을 사용하는 프로그램
public class RunTests2 {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(ExceptionTest.class)) { // 변경된 부분
                tests ++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    Class<? extends Throwable> excType = m.getAnnotation(ExceptionTest.class).value();
                    if (excType.isInstance(exc)) { // 올바른 예외를 던지는 지 확인
                        passed++;
                    } else {
                        System.out.printf("테스트 %s 실패: 기대한 예외 %s, 발생한 예외 %s%n", m, excType.getName(), exc);
                    }
                } catch (Exception exc) {
                    System.out.println("잘못 사용한 @Test:" + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);
    }
}
```

catch 문에서 Exception을 잡아 예상한 예외를 뱉었는지 확인한다.

---

1. **배열 매개변수를 받는 애너테이션**

**ExceptionArrTest 애너테이션**

```java
/**
 * 코드 39-6 배열 매개변수를 받는 애너테이션 타입
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionArrTest {
    Class<? extends Throwable>[] value();
}
```

**테스트 코드**

```java
// 코드 39-7 배열 매개변수를 받는 애너테이션을 사용하는 코드
public class Sample3 {
    @ExceptionArrTest({ IndexOutOfBoundsException.class, NullPointerException.class})
    public static void doublyBad() { // 성공해야 한다.
        ArrayList<String> list = new ArrayList<>();
        // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나 NullPointerException을 던질 수 있다.
        list.add(5, null);
    }
}
```

**ExceptionArrTest 애너테이션 처리기**

```java
// 코드 39-7 배열 매개변수를 받는 애너테이션을 사용하는 코드
public class RunTests3 {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(ExceptionArrTest.class)) {
                tests ++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    int oldPassed = passed;
                    Class<? extends Throwable>[] excTypes = m.getAnnotation(ExceptionArrTest.class).value();
                    for (Class<? extends Throwable> excType : excTypes) {
                        if (excType.isInstance(exc)) {
                            passed++;
                            break;
                        }
                    }
                    if (passed == oldPassed) {
                        System.out.printf("테스트 %s 실패 : %s %n", m, exc);
                    }
                } catch (Exception exc) {
                    System.out.println("잘못 사용한 @Test:" + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);
    }
}
```

**실행결과**

![Untitled 2](https://user-images.githubusercontent.com/49682056/229296013-0b21af19-e5da-4518-b5bb-791dbff0cb0c.png)

---

1. `**@Repeatable` 메타 애너테이션으로 여러 개의 값을 받는 방법**

배열 매개변수를 사용하는 대신 애너테이션에 `@Repeatable` 메타애너테이션을 다는 방식으로 이 애너테이션은 하나의 프로그램 요소에 여러번 달 수 있다.

**주의점**

- `@Repeatable` 을 단 애너테이션을 반환하는 ‘컨테이너 애너테이션’을 하나 더 정의하고, `@Repeatable` 에 이 컨테이너 애너테이션의 class 객체를 매개변수로 전달해야한다.
- 컨테이너 애너테이션은 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야한다.

**ExceptionRepeatableTest 애너테이션**

```java
/**
 * 코드 39-8 반복 가능한 애너테이션 타입
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
public @interface ExceptionRepeatableTest {
    Class<? extends Throwable> value();
}
```

**ExceptionTestContainer 애너테이션**

```java
// 컨테이너 애너테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
    ExceptionRepeatableTest[] value();
}
```

**테스트 코드**

```java
// 코드 39-9 반복 가능 애너테이션을 두 번 단 코드
public class Sample4 {

    @ExceptionRepeatableTest(IndexOutOfBoundsException.class)
    @ExceptionRepeatableTest(NullPointerException.class)
    public static void doublyBad() { // 성공해야 한다.
        ArrayList<String> list = new ArrayList<>();
        // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나 NullPointerException을 던질 수 있다.
        list.add(5, null);
    }
}
```

**ExceptionRepeatableTest 애너테이션 처리기**

```java
// 코드 39-10 반복 가능 애너테이션 다루기
public class RunTests4 {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(ExceptionRepeatableTest.class) || m.isAnnotationPresent(ExceptionTestContainer.class)) {
                tests ++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    int oldPassed = passed;
                    ExceptionRepeatableTest[] excTests = m.getAnnotationsByType(ExceptionRepeatableTest.class);
                    for (ExceptionRepeatableTest excType : excTests) {
                        if (excType.value().isInstance(exc)) {
                            passed++;
                            break;
                        }
                    }
                    if (passed == oldPassed) {
                        System.out.printf("테스트 %s 실패 : %s %n", m, exc);
                    }
                } catch (Exception exc) {
                    System.out.println("잘못 사용한 @Test:" + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);
    }
}
```

반복 가능 애너테이션을 여러개 달면 하나만 달았을 때와 구분하기 위해 해당 ‘컨테이너’ 애너테이션 타입이 적용된다.

`getAnnotationsByType` 메서드는 이 둘을 구분하지 않아서 반복 가능 애너테이션과 그 컨테이너 애너테이션을 모두 가져오지만, `isAnnotationPresent` 메서드는 둘을 명확히 구분한다.

따라서 반복 가능 애너테이션을 여러 개 단 다음 `isAnnotationPresent` 로 반복 가능 애너테이션이 달렸는지 검사한다면 “그렇지 않다”라고 알려준다.

그 결과 애너테이션을 여러 번 단 메서드들을 모두 무시하고 지나친다.

같은 이유로 `isAnnotationPresent` 로 컨테이너 애너테이션이 달렸는지 검사한다면 반복 가능 애너테이션을 한번만 단 메서드를 무시하고 지나간다.

그래서 달려 있는 수와 상관 없이 모두 검사하려면 둘을 따로따로 확인해야한다.

**장점**

- 높은 가독성

**단점** 

- 애너테이션 선하고 이를 처리하는 부분에서의 코드 양 증가
- 처리 코드가 복잡해 오류가 날 가능성 존재

### 정리

애너테이션으로 할 수 있는 일을 명명 패턴으로 처리할 이유는 없다.

도구 제작자를 제외하고는, 일반 프로그래머가 애너테이션 타입을 직접 정의할 일은 거의 없다.

하지만 자바 프로그래머라면 예외없이 자바가 제공하는 애너테이션 타입들을 사용해야 한다.