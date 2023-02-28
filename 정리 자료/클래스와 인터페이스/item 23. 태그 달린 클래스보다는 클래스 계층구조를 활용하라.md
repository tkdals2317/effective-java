# item 23. 태그 달린 클래스보다는 클래스 계층구조를 활용하라

<aside>
💡 **태그 달린 클래스의 단점을 알아보고 클래스 계층 구조로 바꾸는 방법을 알아보자**

</aside>

### 태그 달린 클래스

위에서 말한 태그 클래스란, 한 개의 클래스가 두가지 이상을 표현 할 수 있으며, 그 중 표현하는 의미를 태그 값으로 알려주는 클래스를 말한다.

아래의 코드를 보고 이해해보자. 

아래의 Figure 클래스는 Shape 태그에 따라 원이 될 수도, 사각형이 될 수도 있다. 이런 클래스를 태그 달린 클래스라고 한다.

```java
public class Figure {
    enum Shape { RECTANGLE, CIRCLE };
    // 태그 필드 - 현재 모양을 나타낸다.
    final Shape shape;

    // 다음 필드들은 모양이 사각형(RECTANGLE)일 때만 쓰인다.
    double length;
    double width;

    // 다음 필드는 모양이 원(CIRCLE)일 때만 쓰인다.
    double radius;

    // 원용 생성자
    Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    // 사각형용 생성자
    Figure(double length, double width) {
        shape = Shape.RECTANGLE;
        this.length = length;
        this.width = width;
    }
    
    double area() {
        switch (shape) {
            case RECTANGLE:
                return length * width;
            case CIRCLE :
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
}
```

**위 코드의 단점**

1. 열거타입 선언, 태그 필드, switch 문 등 쓸데없는 코드가 많다.
2. 여러 구현이 한 클래스에 혼합되어 가독성이 나쁘다.
3. 다른 의미를 위한 코드도 항상 함께 하니 메모리도 많이 사용한다.
4. 필드들을 final로 선언하려면 해당 의미에 쓰이지 않는 필드까지 생성자에서 초기화해야한다.
5. 인스턴스 타입만으로는 현재 나타나내는 의미를 알 길이 없다.

### 클래스 계층 구조를 활용하는 서브 타이핑

자바는 타입 하나로 다양한 의미의 객체를 표현하는 훨씬 나은 수단인 클래스 계층 구조를 활용하는 서브타이핑이라는 것을 제공한다.

**클래스 계층 구조로 바꾸는 방법**

1. 가장 먼저 계층구조의 루트가 될 추상 클래스를 정의하고, 태그 값에 따라 달라지는 메서드들을 루트 클래스의 추상 메서드로 선언한다.(area 메서드)
2. 태그 값에 상관없이 동작이 일정한 메서드들을 루트 클래스에 일반 메서드로 추가한다.
3. 모든 하위 클래스에서 공통으로 사용하는 데이터 필드들도 전부 루트 클래스로 올린다.
4. 루트 클래스로 확장한 구체 클래스를 의미별로 하나씩 정의한다.
5. 구체 클래스에 필요한 데이터 필드를 넣는다(원에서는 radius, 사각형에서는 length, width)
6. 루트 클래스에서 정의한 추상 메서드를 각자 의미에 맞게 구현한다.

위 방법으로 Figure 클래스를 클래스 계층 구조 방식으로 구현하면 아래와 같다.

```java
abstract class Figure {
    abstract double area();
}

class Circle extends Figure {
    final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * (radius * radius);
    }
}

class Rectangle extends Figure {
    final double length;
    final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return length * width;
    }
}
```

**장점**

1. 간결하고 명확하다.
2. 살아남은 필드들은 모두 final로 불변이다.
3. 각 클래스의 생성자가 모든 필드를 초기화하고 추상 메서드를 구현했는지 컴파일러가 검사해준다.
4. 타입 사이의 자연스러운 계층 관계를 반영할 수 있어서 유연성은 물론 컴파일 타임 타입 검사 능력을 높여준다.

### 정리

태그 달린 클래스는 절대 왠만해서 쓰지말자.

새로운 클래스를 작성하는 데 태그 필드가 나타난다면 클래스 계층 구조로 대체하는 방법을 생각하자.

기존 코드가 그런게 있다면 계층구조로 리팩토링하자