# item 52. 다중정의는 신중히 사용하라

<aside>
💡 다중정의 메서드와 재정의 메서드의 차이점과 다중정의 메서드 사용시 주의점을 알아보자

</aside>

### 다중정의와 재정의 메서드의 메서드 호출 메커니즘

**다중정의 메서드**

```java
// 코드 52-1 컬렉션 분류기 - 오류! 이 프로그램은 뭘 출력할까?
public class CollectionClassifier {

    public static String classify(Set<?> s) { return "집합";}
    public static String classify(List<?> s) { return "리스트";}
    public static String classify(Collection<?> s) { return "그 외";}

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        for (Collection<?> c : collections) {
            System.out.println(classify(c));
        }
    }
}

==== output ====
그 외
그 외
그 외
```

“집합”, “리스트”, “그 외”가 출력되는 것을 기대하지만 “그 외”만 세 번 출력된다.

왜 일까?

**다중정의 된 메서드는 어느 메서드가 호출할지가 컴파일타임에 정해지기 때문이다.**

컴파일타임에는 for문 안의 c는 `Collection<?>` 타입이다. 

런타임에는 타입이 매번 달라지지만, 호출할 메서드를 선택하는 데는 영향을 주지않는다.

**⇒다중정의한 메서드는 정적으로 선택된다.**

위 코드를 정상적으로 동작하게 하고 싶다면 classify 메서드를 하나로 합친 후 instanceof로 명시적으로 검사하면 깔끔히 해결된다.

```java
public static String classify(Collection<?> c) {
    return c instanceof Set ? "집합" : 
           c instanceof List ? "리스트" : "그 외";
}
```

**재정의된 메서드 호출**

```java
// 코드 52-2 재정의된 메서드 호출 메커니즘 - 이 프로그램은 무엇을 출력할까?
class Wine {
    String name() {
        return "포도주";
    }
}

class SparklingWine extends Wine {
    @Override String name() {
        return "발포성 포도주";
    }
}

class Champagne extends SparklingWine {
    @Override
    String name() {
        return "샴페인";
    }
}

public class Overriding {
    public static void main(String[] args) {
        List<Wine> wineList = List.of(new Wine(), new SparklingWine(), new Champagne());

        for (Wine wine : wineList) {
            System.out.println(wine.name());
        }
    }
}

==== output ====
포도주
발포성 포도주
샴페인
```

예상한 것과 같이 “포도주”, “발포성 포도주”, “샴페인”이 출력된다.

for문의 컴파일타임 타입이 모두 Wine인 것과 무관하게 항상 ‘가장 하위에서 재정의’한 재정의 메서드가 실행된다.

**⇒ 재정의 메서드는 동적으로 선택된다.**

### 다중정의가 혼동을 일으키는 상황을 피하자

다중정의한 메서드가 기대한 대로 동작하지 않을 수 있으니 헷갈릴 수 있는 코드는 작성하지 말자.

특히 공개 API라면 더 신경 써야 하낟.

API 사용자가 매개변수를 넘기면서 어떤 메서드가 호출될지 모른다면 프로그램이 오동작하기 쉽다.

### 안전하고 보수적으로 가려면 매개변수 수가 같은 다중정의는 만들지 말자

가변인수를 사용하는 메서드라면 아에 다중정의를 해서는 안된다.

### 다중정의하는 대신 메서드 이름을 다르게 지어주는 방법이 있다.

ObjectOutputStream의 writeBoolean, writeInt, writeLong 같은 식으로 메서드 이름을 다르게 지어주자

### 다중정의 생성자는 정적 팩터리나 매개변수 중 하나 이상이 근본적으로 다르게 하라

정적 팩터리를 사용하는 것이 좋지만 그렇지 못한 경우에는 매개변수 하나 이상이 근본적으로 다르게하면 헷갈릴 일을 줄일 수 있다.

“근본적을 다르다”는 두 타입의 값을 서로 어느 쪽으로든 형변환 할 수 없다 라는 뜻이다.

```java
public class SetList {
    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i ++) {
            set.add(i);
            list.add(i);
        }

        for (int i = 0; i < 3; i ++) {
            set.remove(i);
            list.remove(i);
        }

        System.out.println(set);
        System.out.println(list);
    }
}
==== output =====
[-3, -2, -1]
[-2, 0, 2]
```

![Untitled](https://user-images.githubusercontent.com/49682056/235343168-f7d7fb27-38d3-4ea0-a673-64e1a0fcb0a3.png)

![Untitled 1](https://user-images.githubusercontent.com/49682056/235343165-506f7259-4ed0-4186-be18-61245295a42c.png)

위 코드는 list.remove(i)는 다중 정의된 list.remove(int index)를 선택하면서 ‘지정한 위치’의 원소를 지우는 로직을 수행하게 되면서 [-2, 0 2]라는 출력이 나오게된다.

이런 문제를 해결하기 위해서는 Integer로 형변환하여 remove(Object o) 메서드를 선택하게 하면 된다.

```java
for (int i = 0; i < 3; i ++) {
    list.remove((Integer) i); // 혹은 remove(Integer.valueOf(i))
}
```

이런 혼란스러운 결과가 나오는 이유는 List<E> 인터페이스가 remove(Object)와 remove(int)를 다중정의했기 때문이다.

제네릭과 오토박싱이 등장하면서 두 메서드의 매개변수 타입이 더는 “근본적으로 다르지 않게” 되었다. 

### 람다와 메서드 참조 다중정의 시 혼란을 일으킨다.

```java
// 1번. Thread 생성자 호출
new Thread(System.out::println).start();

// 2번. ExecutorService의 submit 메서드 호출
ExecutorService executorService = Executors.newCachedThreadPool();
executorService.submit(System.out::println);
```

2번은 컴파일 오류가 난다. 두 메서드 다 Runnable을 받는 형제 메서드를 다중정의하고 있는데 왜 한쪽에서만 에러가 날까?

submit 다중 정의 메서드 중에 Callable<T>를 받는 메서드도 있다는 데 있다.

만약 submit 메서드가 하나만 있었다면 정상적으로 동작했을 것이다.

기술적으로 말하면 System.out::println은 부정확한 메서드 참조로 “암시적 타입 람다식이나 부정확한 메서드 참조 같은 인수 표현식은 목표 타입이 선택되기 전에는 그 의마가 정해지지 않기 때문에 적용성 테스트 때 무시된다”는 것이 원인이다. 

(뭔 소리야 이게..? 아무튼 다중정의된 메서드가 있으면 메서드 참조를 지양하라는 것 같다.)

**메서드를 다중정의할 때, 서로 다른 함수형 인터페이스라도 같은 위치의 인수를 받아서는 안 된다.**

### 어떤 다중정의 메서드가 불리는 지 몰라도 기능만 똑같으면 신경쓰지 않아도 된다.

상대적으로 더 특수한 다중정의 메서드에서 덜 특수한 다중정의 메서드로 일을 넘겨버리는 메서드 포워딩을 사용하면 기능상은 동일하게 되면 상관 없다.

```java
public boolean contentEquals(StringBuffer sb) {
    return contentEquals((CharSequence)sb);
}
```

### 정리

- **다중정의한 메서드는 정적으로 컴파일 타임에 선택되고 재정의 메서드는 동적으로 선택된다.**
- 매개변수가 같다면 다중정의를 피하자
- 메서드 포워딩을 하여 동일하게 동작되게 만들자