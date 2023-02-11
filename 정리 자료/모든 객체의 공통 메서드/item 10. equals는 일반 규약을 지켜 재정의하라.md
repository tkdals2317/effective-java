# item 10. equals는 일반 규약을 지켜 재정의하라

<aside>
💡 equals 메서드를 재정의해야 하는 상황과 하지 않아도 되는 상황에 대해 알아보자
그리고 equals 메서드의 동치관계를 구현할 때 지켜야 할 규약에 대해 알아보자

</aside>

<aside>
❗ **주의! 내용이 어렵고 양이 많을 수 있습니다**

</aside>

### equals 메서드를 재정의하지 않아도 되는 케이스

- ****************************************************************************************각 인스턴스가 본질적으로 고유하다.****************************************************************************************
    - 값을 표현하는 것이 아니라 동작 개체를 표현하는 클래스
    - 대표적으로 Thread

- **인스턴스의 ‘논리적 동치성(logical equality)’를 검사할 일이 없다.**
    - java.util.regex.Pattern의 equals는 정규표현식이 표현은 달라도 같은 표현식 인지 검사해야하는데 이 케이스가 논리적 동치성을 검사하는 케이스
    - 이런 경우가 아니라면 Object의 기본 equals만으로 해결 가능하다

- **상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다**
    - Set 구현체는 AbstractSet이 구현한 equals를 상속받아 사용한다. (List, Map도 마찬가지)

- **클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다**
    - 값 클래스여도 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스의 경우 equals를 만들 필요가 없다. 어차피 하나니까!
    - equals를 호출할 일이 없고 철저하게 위험을 회피하고 싶을 땐 equals 메서드를 호출하면 예외를 던지게 작성하면 된다.

---

### equals 메서드를 재정의해야 할 때는 언제일까?

위에서 설명한 Pattern과 같이 논리적 동치성을 확인해야 하는 경우이다.

주로 **값 클래스**들이 여기 해당된다.

- 여깃 값 클래스란 Integer, String 처럼 값을 표현하는 클래스를 말한다.
- 지금 내 머릿속에서 생각나는 예는 기간을 나타내는 Period 같은 것도 될 수 있을 거 같다. (같은 기간인지 비교해야 할 때)

---

### equals 메서드를 재정의할 때 지켜야 할 일반 규약 5가지

1. **반사성(reflexivity) : null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true다**

객체는 자기 자신과 같아야 한다는 의미이다. ~~(내가 나가 아니라고 말하는 꼴 ㅋㅋㅋ… 안지키는 게 더 힘들다)~~

---

1. **대칭성(symmetry) : null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true면 y.equals(x)도 true다.**

두 객체는 서로 동지 여부에 똑같이 답해야 한다는 뜻이다.

```java
// 코드 10-1) 대칭성을 위배하는 잘못된 코드 - 대소문자를 구분하지 않는 문자열을 구현한 클래스
public final class CaseInsensitiveString {
  private final String s;

  public CaseInsensitiveString(String s) {
    this.s = Objects.requireNonNull(s);
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof CaseInsensitiveString) {
      return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
    }

    if(o instanceof String) { // 한 방향으로만 작동!!
      return s.equalsIgnoreCase((String) o);
    }
    return false;
  }
}
```

이 클래스에서의 equals는 대소문자를 무시하고 일반 문자열과도 비교를 시도한다.

```java
CaseInsensitiveString caseInsensitiveString = new CaseInsensitiveString("Polish");
String s = "Polish";
System.out.println(caseInsensitiveString.equals(s)); // true를 출력
System.out.println(s.equals(caseInsensitiveString)); // false를 출력
```

위 코드를 실행시키면 대칭성이 뭔지 바로 알 수 있다.

CaseInsensitiveString의 equlas(s)를 했을 때는 true를 반환하지만 String인 s의 equals로 caseInsensitiveString와 비교하면 false가 떨어진다.

List에 CaseInsensitiveString를 담았고 contains(s)를 했을 때 false를 반환하지만 OpenJDK에 따라 결과가 달라질 수 있다. 

⇒ **그 객체를 사용하는 다른 객체들이 어떻게 반응할지 알 수 없다.**

String을 비교하고 싶다면 타입 캐스팅하여 String이 아닌 CaseInsensitiveString로만 비교 하게 만들자

```java
@Override
public boolean equals(Object o) {
  return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
} 
```

---

1. **추이성(transitivity) : null이 아닌 모든 참조 값 x, y, z에 대해, x.equals(y)가 true이고 y.equals(z)도 true이면 z.equals(x)도 true다.**

첫번 째 객체와 두번 째 객체가 같고, 두 번째 객체와 세번째 객체가 같다면, 첫번 째 객체와 세번 째 객체도 같아야 한다는 뜻이다.

상위 클래스에는 없는 새로운 필드를 하위 클래스에 추가하는 상황을 예를 들어보자

여기 2차원에서의 저믈 표현하는 클래스가 있다.

```java
class Point {
  private final int x;
  private final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof Point)) return false;
    Point p = (Point) o;
    return this.x == p.x && this.y == p.y;
  }
}
```

이 클래스를 확장하여 점에 색상을 더한 ColorPoint 클래스를 만들어보자

```java
class ColorPoint extends Point {
  
  private final Color color;

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof ColorPoint)) return false;
    
    return super.equals(o) && this.color == ((ColorPoint) o).color;
  }
}
```

equals 메소드를 재정의하지 않으면 상위 클래스인 Point의 equals를 수행하여 색상 정보는 무시한 채 비교가 이뤄지게 된다.

그럼 위치와 색상을 비교하는 equals를 만들어보자

```java
// 코드 10-2) 잘못된 코드 - 대칭성 위배!
class ColorPoint extends Point {
  
  private final Color color;

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof ColorPoint)) return false;
    
    return super.equals(o) && this.color == ((ColorPoint) o).color;
  }
}
```

보기엔 모든 문제가 해결된 것 같지만, Point 객체와 ColorPoint 객체를 바꿔가며 equals를 했을 때 대칭성을 위배하게 된다.

```java
ColorPoint a = new ColorPoint(1, 2, Color.RED);
Point b = new Point(1, 2);

System.out.println(a.equals(b)); // false를 출력
System.out.println(b.equals(a)); // true를 출력
```

그렇다면 Point와 비교할 경우에는 색상을 무시하게 짠 코드라면 해결 될까?

```java
// 10-3) 잘못된 코드 - 추이성 위배!
class ColorPoint extends Point {
  
  private final Color color;

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof Point)) return false;

    //o가 일반 Point이면 색상을 무시하고 비교한다
    if(!(o instanceof ColorPoint)) return o.equals(this);
    
    //o가 ColorPoint이면 색상까지 비교한다.
    return super.equals(o) && this.color == ((ColorPoint) o).color;
  }
}
```

하지만 이 방식은 대칭성은 지켜주지만, 추이성을 깨버린다.

```java
ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
Point p2 = new Point(1, 2);
ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);

System.out.println(p1.equals(p2)); // true
System.out.println(p2.equals(p3)); // true
System.out.println(p1.equals(p3)); // false
```

p1과 p2 비교와 p2와 p3 비교에서는 색상을 무시하고 비교를 했지만, p1과 p3를 비교했을 땐 색상까지 고려하여 비교했기 때문이다.

**또 이런 코드는 무한 재귀에 빠질 수 있다.**

Point를 상속받아 만든 smellPoint를 만들고, equals를 구현했다고 했을 때 SmellPoint와 ColorPoint를 equals 했을 때 StackOverflowError를 일으킨다.

```java
class SmellPoint extends Point {
  
  private final Smell smell;

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof Point)) return false;

    if(!(o instanceof SmellPoint)) return o.equals(this); // 재귀가 발생하는 부분!!!
    
    return super.equals(o) && this.smell == ((SmellPoint) o).smell;
  }
}
```

```java
Point colorPoint = new ColorPoint(1, 2, Color.RED);
Point smellPoint = new SmellPoint(1, 2, Smell.SWEET);

System.out.println(colorPoint.equals(smellPoint ));  // StackOverflowError
```

**해법**

이런 문제는 모든 객체 지향 언어의 동치관계에서 나타나는 근본적인 문제이다.

구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.

얼핏, equals안의 instanceof 검사를 getClass 검사로 바꾸면 규약도 지키고 값도 추가하면서 구체 클래스를 상속할 수 있을 것으로 보이지만 

이는 **리스코프 치환 원칙**을 위배한다.

<aside>
💡 **리스코프 치환의 원칙 : 상위 타입의 객체를 하위 타입의 객체로 치환해도 상위 타입을 사용하는 프로그램은 정상적으로 동작해야 한다.**
                                      * 개방 폐쇄 원칙을 받쳐주는 원칙으로 다형성에 관한 원칙을 제공한다.

</aside>

```java
// 10-4) 잘못된 코드 - 리스코프 치환 원칙 위배!
@Override
public boolean equals(Object o) {
  if(o == null || o.getClass() != this.getClass()) {
    return false;
  }

  Point p = (Point) o;
  return this.x == p.x && this.y = p.y;
}
```

**Point의 하위 클래스는 정의상 여전히 Point이므로 어디서든 Point로써 활용될 수 있어야 한다.** 하지만 위 코드에서는 그렇지 못하다.

**그래서 해법이 뭐냐고..?**

상속을 포기하고 컴포지션을 사용해라, 즉 상속이 아닌 필드로 두라는 말이다.

Point를 상속하는 대신 Point를 ColorPoint의 private 필드로 두고, ColorPoint와 같은 위치의 일반 Point를 반환하는 뷰(view) 메서드를 public으로 추가하는 방법이 있다.

```java
public ColorPoint {
  private Point point;
  private Color color;

  public ColorPoint(int x, int y, Color color) {
    this.point = new Point(x, y);
    this.color = Objects.requireNonNull(color);
  }

  public Point asPoint() {
    return this.point;
  }

  @Override
  public boolean equals(Object o) {
    if(!(o instanceof ColorPoint)) {
      return false;
    }
    ColorPoint cp = (ColorPoint) o;
    return this.point.equals(cp) && this.color.equals(cp.color);
  }
}
```

웃기게도 자바 라이브러리도 이런 구체 클래스를 확장해 값을 추가한 클래스가 있다.

java.util.Timestamp는 java.util.Date를 확장하여 nanoseconds 필드를 추가하였다. (설계 실수라고 한다..)

그 결과 두 객체를 한 컬렉션에 넣거나 섞어 사용하면 이상하게 동작 할 수 있으니 주의하자!

<aside>
📍 **추상 클래스의 하위 클래스에서라면 equals 규약을 지키면서 값을 추가할 수 있다.**
아무런 값을 갖지 않는 추상 클래스 Shape를 상위 클래스로 두고, radius 필드를 추가한  Circle 클래스와, length와 width 필드를 추가한 Rectangle 클래스를 만들 수 있다. 
**상위 클래스를 직접 인스턴스로 만드는 게 불가능하다면 지금까지 이야기한 문제는 일어나지 않는다.**

</aside>

---

1. **일관성(consistency) : null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다.**
    
    두 객체가 같다면 (어느 하나 혹은 두 객체가 모두 수정되지 않는 한) 앞으로도 영원히 같아야 한다는 뜻이다.
    
    가변 객체는 비교시점에 따라 달라질 수 있지만, 불변 객체의 비교의 결과는 절대로 변해서는 안된다.
    
    **클래스가 불변이든 가변이든, equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안된다.**
    
    equals는 항시 메모리에 존재하는 객체만을 사용한 결정적(deterministic) 계산만 수행해야 한다.
    

---

1. **null-아님 : null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false다.**
    
    모든 객체가 null과 같지 않아야한다는 뜻이다.
    
    ```java
    @Override 
    public boolean equals(Object o) {
      if(o == null) return false; //불필요
      return this.x == o.x;
    }
    ```
    
    그렇다고 위 코드처럼 짤 필요는 없다.
    
    동치성을 검사하려면 equals는 건네받은 객체를 적절히 형변환 후 필수 필드 값을 알아내야한다.
    
    어차피 동치성 검사를 하는 과정에서 묵시적 null검사를 하게 된다
    
    ```java
    @Override
    public boolean equals(Object o) {
      if(!(o instanceof MyType)) 
    			return false; //묵시적 null검사
      MyType mt = (MyType) o;
    	...
    }
    ```
    

---

자 이제 위 규약을 지키면서 equals 메서드 구현 방법을 단계별로 정리해보자

1. **== 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.**
    
    자기 자신이면 true를 반환한다.(단순한 성능 최적화용으로, 비교 작업이 복잡한 상황에 값어치를 함)
    
2. **instanceof 연산자로 입력이 올바른 타입인지 확인한다.**
    
    그렇지 않다면 false를 반환한다. 
    
    가끔은 그 클래스가 구현한 특정 인터페이스가 될 수 도 있다.(대표적인 예로 List, Map, Map.Entry  등의 컬렉션 인터페이스)
    
3. **입력을 올바른 타입은 형변환한다.**
    
    앞서 2번에서 instanceof 검사를 했기에 이 단계는 100% 성공한다.
    
4. **입력 객체와 자기 자신의 대응되는 ‘핵심’ 필드 모두 일치하는지 하나씩 검사한다.**
    
    모든 필드가 일치하면 true를, 하나라도 다르면 false를 반환한다. 
    
    2단계에서 인터페이스를 사용했다면 입력의 필드 값을 가져올 때도 그 인터페이스의 메서드를 사용해야 한다.
    
    타입이 클래스라면 접근권한에 따라 해당 필드에 직접 접근할 수도 있다.
    

- 타입 별 사용하는 비교 방법
    - float, double을 제외한 기본 타입 필드 : == 연산자로 비교
    - 참조 타입 필드 : 각각의 equals 메서드로 비교
    - float, double : Float.compare(float, float), Double.compare(double, double)로 비교
- null도 정상 값으로 취급하는 참조 타입의 필드의 경우 정적 메서드인 Objects.equals로 비교하여 NPE를 발생시킨다.
- 비교하기가 아주 복잡한 필드를 가질 경우에는 표준형을 저장해둔 후 표준형끼리 비교하면 경제적이다. (불변 클래스에 제격이다)
- 최상의 성능을 바란다면 다를 가능성이 더 크거나 비용이 싼 필드를 먼저 비교하자.
    - 객체의 논리적 상태와 관련 없는 필드로는 비교하지 말자(동기화용 락)
    - 파생 필드를 비교할 필요는 없지만, 파생 필드를 비교하는 것이 더 빠를 때도 있다.

위 모든 규약을 지키고 잘 짜여진 equals 메소드

```java
@Override
public boolean equals(Object o) {
		// **자기 자신의 참조인지 확인**
		if (o == this)
				return true;
		// **instanceof 연산자로 입력이 올바른 타입인지 확인한다. null-아님 체크**
		if (!(o instanceof PhoneNumber)) {
				return false;
		}
		// **입력을 올바른 타입은 형변환한다.**
		PhoneNumber pn = (PhoneNumber)o;
		// **입력 객체와 자기 자신의 대응되는 ‘핵심’ 필드 모두 일치하는지 하나씩 검사한다.**
		return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode
}

```

---

### 마지막 주의 사항

- equals를 재정의 할 땐 hashCode도 반드시 재정의하자
- 너무 복잡하게 해결하려 들지 말자
- Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지말자
    - 이 경우는 오버로딩에 해당한다.

---

### 정리

꼭 필요한 경우가 아니라면 equals를 재정의하지 말자.

많은 경우에 Object의 equals가 대부분의 원하는 비교를 정확히 수행해준다.

재정의해야 할 때(논리적 동치성 검증(동등성)이 필요할 때)는 그 클래스의 핵심 필드 모두를 빠짐없이, 다섯 가지 규약을 확실히 지켜가며 비교해야 한다.

💭 InSight를 하면서 equals를 재정의 한 적이 있는데 위 규약을 지키면서 재정의했는 지 확인해보고 리팩토링 해보자!