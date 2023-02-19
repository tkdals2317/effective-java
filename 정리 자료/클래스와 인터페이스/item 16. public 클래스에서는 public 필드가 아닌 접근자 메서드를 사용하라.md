# item 16. public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라

### public 필드 사용의 단점

- API를 수정하지 않고는 내부 표현을 바꿀 수 없다.
- 불변식을 보장하지 못한다.
- 외부에서 필드를 접근할 때 부수 작업을 수행할 수도 없다(스레드 안전 X)

### 필드를 모두 private으로 바꾸고 public 접근자(getter)를 추가하자

```java
public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }

    public void setX(double x) { this.x = x; }

    public double getY() { return y; }

    public void setY(double y) { this.y = y; }
}
```

패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공함으로써 클래스 내부 표현 방식을 언제든 바꿀 수 있는 유연성을 얻을 수 있고, 클라이언트가 내부 표현 방식을 마음대로 바꿀 수 없게 된다.

### package-private 클래스 혹은 private 중첩 클래스라면 데이터 필드를 노출한다 해도 문제가 없다

그 클래스가 표현하려는 추상 개념만 올바르게 표현해주면 된다.

책에서는 클래스 선언 면이나 이를 사용하는 클라이언트 코드 면에서나 접근자 방식보다 훨씬 깔끔하다고는 하나, 일관성있게 다 만들어주는 게 낫지 않나라는 생각이 들었다.

### 클래스 필드가 불변이라고 public으로 두는 것은 좋은 생각이 아니다.

API를 변경하지 않고는 표현 방식을 바꿀수 없고, 필드를 읽을 때 부수 작업을 수행할 수 없다는 단점은 여전하다.

### 정리

public 클래스는 절대 가변 필드를 직접 노출해서는 안 된다. 

불변 필드라해도 여전히 안심할 수없다.

하지만 package-private  클래스나 private 중첩 클래스에서는 필드를 노출하는 편이 나을 떄도 있다.(사실 잘 모르겠다..그냥 똑같이 접근자를 붙혀주는게 난 편한 거 같다)