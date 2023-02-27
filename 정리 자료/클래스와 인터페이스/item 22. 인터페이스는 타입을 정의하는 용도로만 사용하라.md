# item 22. 인터페이스는 타입을 정의하는 용도로만 사용하라

<aside>
💡 **인터페이스는 자신을 구현한 클래스의 인스턴스를 참조할 수 있는 타입 역할을 한다.**
달리 말해, 클래스가 어떤 인터페이스를 구현한다는 것은 자신의 인스턴스로 무엇을 할 수 있는지를 클라이언트에게 얘기해주는 것이다.
인터페이스는 오직 이 용도로만 사용해야 한다.

</aside>

### 인터페이스 안티 패턴 - 상수용 인터페이스

아래의 상수 인터페이스는 메서드 없이 상수를 뜻하는 static final 필드로만 가득찬 인터페이스로 인터페이스를 잘못 사용한 예이다.

```java
// 상수 인터페이스 안티패턴 - 사용 금지!
public interface PhysicalConstants {
    
    // 아보가드로 수 (1/몰)
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    
    // 볼츠만 상수 (J/K)
    static final double BOLZMANN_CONSTANT = 1.380_648_52e-23;
    
    // 전자 질량 (Kg)
    static final double ELECTRON_MASS = 9.109_383_56e-31;

}
```

클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부 구현에 해당한다.

따라서 상수 인터페이스를 구현하는 것은 이 내부 구현을 클래스의 API로 노출하는 행위이다.

또한 final이 아닌 클래스가 상수 인터페이스를 구현한다면 모든 하위 클래스의 이름 공간이 그 인터페이스가 정의한 상수들로 오염되버린다.

### 특정 클래스나 인터페이스와 강하게 연관된 상수라면 그 클래스나 인터페이스 자체에 추가해야한다.

모든 숫자 기본 타입의 박싱 클래스가 대표적으로, Integer와 Double에 선언된 MIN_VALUE, MAX_VALUE 상수가 이런 예다.

열거 타입으로 나타내기 적합한 상수라면 열거타입으로 만들어 공개하면 된다.

### 차라리 인스턴스화 할 수 없는 유틸리티 클래스로 만들자

```java
public class PhysicalConstantsUtilityClass {
    // 인스턴스화 방지를 위한 private 생성자
    private PhysicalConstantsUtilityClass() {}; 
    
    // 아보가드로 수 (1/몰)
    public final double AVOGADROS_NUMBER = 6.022_140_857e23; // 자바 7부터는 숫자 리터럴에 _을 넣어 가독성을 높여준다. _를 사용하여 세자리씩 묶어주자 9,000,000 과 같은 개념

    // 볼츠만 상수 (J/K)
    public final double BOLZMANN_CONSTANT = 1.380_648_52e-23;

    // 전자 질량 (Kg)
    public final double ELECTRON_MASS = 9.109_383_56e-31; 
}
```

클라이언트에서 사용시 클래스 이름까지 명시하고 자주 쓰인다면 정적 임포트하여 클래스 이름을 생략할 수 있다.

```java
import static org.example.item22.PhysicalConstantsUtilityClass.AVOGADROS_NUMBER;

// 정적 임포트를 사용하여 상수 이름만으로 사용하기
public class Test {
    double atoms(double mols) {
        return AVOGADROS_NUMBER * mols; // 빈번히 사용한다면 정적 임포트가 값어치를 한다.
    }
}
```

### 정리

인터페이스는 타입을 정의하는 용도로만 사용해야 한다. 상수 공개용 수단으로 사용하지 말자