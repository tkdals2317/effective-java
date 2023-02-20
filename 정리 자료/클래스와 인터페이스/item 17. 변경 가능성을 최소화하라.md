# item 17. 변경 가능성을 최소화하라

<aside>
💡 **변경 가능성을 최소화하는 불변 객체를 만드는 방법과 불변 객체의 장점에 대해 알아보자**

</aside>

### 불변 클래스란?

인스턴스의 내부 값을 수정할 수 없는 클래스를 뜻하며, 불변 인스턴스에 간직된 정보는 고정되어 객체가 파괴되는 순간까지 절대 달라지지 않는다

자바 플랫폼 라이브러리의 다양한 불변 클래스 : String, 기본 타입의 박싱된 클래스, BigInteger, BigDecimal

### 불변 클래스를 만드는 다섯가지 규칙

- 객체의 상태를 변견하는 메서드(변경자) 즉, setter를 제공하지 않는다.
- 클래스를 확장 할 수 없도록 한다
    - 하위 클래스에서 객체의 상태를 변하게 만드는 상태를 막아준다
    - 상속을 막는 방법에는 final로 선언하는 방법과, 생성자를 제공하지 않고 정적 팩터리 메서드만 제공하는 방법이 있다.
- 모든 필드는 final로 선언한다.
    - 시스템이 강제하는 수단을 이용해 설계자의 의도를 명확히 드러내는 방법
    - 새로 생성된 인스턴스를 동기화 없이 다른 스레드로 건네도 문제없이 동작하게끔 보장하는데도 필요하다
- 모든 필드를 private으로 선언한다.
    - 필드가 참조하는 가변 객체를 클라이언트에서 직접 수정하는 일을 막아준다.
    - 기술적으로 필드를 public final로 선언해도 불변 객체가 되지만 내부 표현을 바꾸지 못하므로 권장 X
- 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.
    - 클래스에 가변 객체를 참조하는 필드가 하나라도 있다면 클라이언트에서 그 객체의 참조를 얻을 수 없도록 해야한다.
    - 접근자 메서드가 그 필드를 그대로 반환하게 해서는 안된다.
    - 생성자, 접근자, readObject 메서드 모두에서 방어적 복사를 수행해라

### 불변 클래스 예제

```java
public final class Complex {
    private final double re;
    private final double im;

    private Complex(double re, double im){
        this.re = re;
        this.im = im;
    }

    public static Complex valueOf(double re, double im){
        return new Complex(re, im);
    }

    public double realPart() {
        return re;
    }

    public double imaginaryPart(){
        return im;
    }

    public Complex plus(Complex c){
        return new Complex(re+c.re, im+c.im);
    }

    public Complex minus(Complex c){
        return new Complex(re-c.re, im-c.im);
    }

    public Complex times(Complex c){
        return new Complex(re*c.re - im*c.im, re*c.im + im*c.re);
    }

    public Complex divdedBy(Complex c){
        double tmp = c.re*c.re+c.im*c.im;
        return new Complex((re*c.re + im*c.im)/tmp,(im*c.re-re*c.im)/tmp);
    }

    @Override public boolean equals(Object o){
        if(o == this) return true;
        if(!(o instanceof Complex)) return false;
        Complex c = (Complex) o;

        return Double.compare(c.re, re) == 0 && Double.compare(c.im, im) == 0;
    }

    @Override
    public int hashCode(){
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override
    public String toString(){
        return "("+re+" + "+im+"i)";
    }
}
```

위 코드에서의 특징

- 접근자 메서드는 있으나 변경자 메서드(setter)는 없다.
- 사칙연산 메서드들은 자기 자신은 수정하지 않고 새로운 Complex 인스턴스를 만들어 반환한다. (피연산자 자체는 그대로인 프로그래밍 패턴을 함수형 프로그래밍이라고 한다) (처음 알았다..)
- 메서드 이름이 add 같은 동사 대신 plus 같은 전치사를 사용한다.

### 불변 객체는 단순하다

불변 객체는 생성된 시점의 상태를 파괴될 때까지 그대로 간진한다.

모든 생성자가 클래스 불변식을 보장한다면 영원히 불변으로 남는다.

### 불변 객체는 근본적으로 스레드 안전하여 따로 동기화할 필요가 없다.

클래스를 스레드 안전하게 만드는 가장 쉬운 방법이 불변 객체를 만드는 것이다.

불변 객체에 대해서는 그 어떤 스레드도 다른 스레드에 영향을 줄 수 없으니 (변경이 불가능하니) 안심하고 공유할 수 있다.

### 불변 클래스라면 한번 만든 인스턴스를 최대한 재활용하자

가장 쉬운 재활용 방법은 자주 쓰이는 값들을 상수(public static final)로 제공하는 것이다.

위 Complex 클래스로 예를 들어보자

```java
public static final Complex ZERO = new Complex(0, 0);
public static final Complex ONE = new Complex(1, 0);
public static final Complex I = new Complex(0, 1);
```

불변 클래스는 자주 사용되는 인스턴스를 캐싱하여 같은 인스턴스를 중복 생성하지 않게 해주는 정적 팩터리를 제공할 수 있다.

⇒ 여러 클라이언트가 인스턴스를 공유하여 메모리 사용량과 가비지 컬렉션 비용이 줄어든다.

⇒ 클라이언트를 수정하지 않고도 필요에 따라 캐시 기능을 나중에 덧붙일 수 있다.

### 불변 객체를 자유롭게 공유할 수 있다는 점은 방어적 복사 필요없다

복사해봐야 원본과 똑같으니 복사 자체가 의미가 없다.

그러므로 불변 클래스는 clone 메서드나 복사 생성자를 제공하지 않는 것이 좋다.

### 불변 객체는 자유롭게 공유할 수 있음은 물론, 불변 객체끼리는 내부 데이터를 공유할 수 있다.

예를 들어 BigInteger의 negate는 크기가 같고 부호만 반대인 새로운 BigInteger를 생성하는데, 이 때 배열은 가변이지만 복사하지 않고 원본 인스턴스와 공유 해도 된다.

그 결과 새로 만든 BigInteger 인스턴스도 원본 인스턴스가 가르키는 내부 배열을 그대로 가르킨다.

![Untitled](https://user-images.githubusercontent.com/49682056/219992014-884da696-4634-4a01-891b-e19285adb65b.png)

![Untitled 1](https://user-images.githubusercontent.com/49682056/219992013-ea2367c1-9257-4f44-9081-dffe142209c4.png)

### 객체를 만들 때 다른 불변 객체들을 구성요소로 사용하면 이점이 많다.

값이 바뀌지 않는 구성요소들로 이뤄진 객체라면 그 구조가 아무리 복잡하더라도 불변식을 유지하기 훨씬 수월하기 때문이다.

좋은 예로, 불변 객체는 맵의 키와 집합(Set)의 원소로 쓰기에 안성맞춤이다.

맵이나 집합은 안에 담긴 값이 바뀌면 불변식이 허물어지는데, 불변 객체를 사용하면 그런 걱정은 하지 않아도 된다.

### 불변 객체는 그 자체로 실패 원자성을 제공한다.

상태가 절대 변하지 않으니 잠깐이라도 불일치 상태에 빠질 가능성이 없다.

### 불변 클래스의 단점 : 값이 다르면 반드시 독립된 객체로 만들어야 한다는 것이다.

값의 가짓수가 많다면 이들을 모두 만드는 데 큰 비용을 치뤄야 한다.

이 문제를 대체하는 방법은 두 가지다.

**첫번 째는 다단계 연산들을 예측하여 기본 기능으로 제공하는 방법이다.**

이러한 다단계 연산을 기본으로 제공한다면 더 이상 각 단계마다 객체를 생성하지 않아도 된다.

**두번 째는 가변 동반 클래스를 사용하는 방법이다.**

가변 동반 클래스의 대표적인 예가 StringBuilder 또는 StringBuffer가 있다.

### 상속을 받지 못하게 하는 방법 : 정적 팩터리 메서드를 사용하기

자신을 상속받지 못하게 하는 방법은 final 클래스로 선언하는 것이다.

하지만 기본 생성자를 private 혹은 package-private로 만들고 정적 팩터리 메서드를 제공하게 되면 자연스래 하위 클래스에서는 생성자가 없어 확장할 수 없게 된다.

```java
public class Complex {
    private final double re;
    private final double im;
		// 생성자를 private으로 만든다
    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }
		// 정적 팩터리 메서드
    public static Complex valueOf(double re, double im) {
        return new Complex(re,im);
    }
    ...
}
```

### 직렬화 시 주의 사항

Serializable을 구현하는 불변 클래스 내부에 가변 객체를 참조하는 필드가 있다면 `readObject`나 `readReolve` 메서드를 반드시 제공하거나, `ObjectOutputStream.writeUnshared`나 `ObjectInputStream.readUnshared` 메서드를 사용해야 한다.

그렇지 않으면 공격자가 이 클래스로부터 가변 인스턴스를 만들어낼 수 있다.

### 모든 클래스를 불변으로 만들 수는 없다.

불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이자.

객체를 가질 수 있는 상태의 수를 줄이면 그 객체를 예측하기 쉬워지고 오류가 생길 가능성이 줄어든다.

예전에 선배가 말씀하신 내용중에 무분별한 세터의 사용은 피하라고 코드리뷰를 들은 적이 있는데 이 내용인 것 같다.

꼭 변경해야 할 필드를 뺀 나머지 모두를 final로 선언하자. 다른 합당한 이유가 없다면 모든 필드는 private final이어야 한다.

**생성자는 불변식 설정이 모두 완료된, 초기화가 완벽히 끝난 상태의 객체를 생성해야 한다.**

확실한 이유가 없다면 생성자와 정적 팩터리 외에는 그 어떤 초기화 메서드도 public으로 제공해서는 안된다.

### 정리

실제 지금 프로젝트의 레거시 코드에도 중간에 상태가 변경되는 가변 클래스의 데이터 때문에 디버깅에 골머리를 앓은 적이 있었다.

실무에서 세터를 최소화하고 자바 reocrd를 사용하였는데 이유가 이런 변경가능성을 최소화하기 위함이였다.

이번 아이템을 통해 변경 가능성을 최소화하는 것의 중요성을 느낄 수 있었고 실제 개발할 때도 이번 아이템에 대하여 항상 생각하며 개발해야겠다는 생각이 들었다.