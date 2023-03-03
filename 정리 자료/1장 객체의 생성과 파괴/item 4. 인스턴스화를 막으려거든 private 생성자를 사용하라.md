# item 4. 인스턴스화를 막으려거든 private 생성자를 사용하라

<aside>
💡 정적 메서드와 정적 필드만을 담은 클래스를 만드는 방법으로  private 생성자를 사용해야 하는 이유를 알아보자

</aside>

### 정적 메서드와 정적 필드만 담은 클래스를 사용하는 예

정적 메서드와 정적 필드만 담은 클래스는 객체지향적이지 못한 것처럼 보이나 남용하지 않으면 나름 쓰임새가 있는 클래스가 될 수 있다.

대표적인 예로 java.lang.Math나 java.lang.Arrays가 클래스가 있다.

![Untitled](https://user-images.githubusercontent.com/49682056/216827728-3f2a9cde-4c04-4e15-bd7a-b308009cf121.png)

![Untitled 1](https://user-images.githubusercontent.com/49682056/216827719-792a6dfb-ba6c-40a9-bc4f-df4c2dcce562.png)

또한 java.util.Collections처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드(혹은 팩터리)를 모아놓은 클래스도 있다. 


![Untitled 2](https://user-images.githubusercontent.com/49682056/216827721-d480aef4-7093-4a79-b0a7-4047310266da.png)

위 클래스는 생성자를 호출하려고 하면 아래와 같이 에러가 떨어지는 것을 확인 할 수 있다.

위 클래스의 경우 기본 타입의 값이나 배열 관련 메서드를 모아놓은 유틸리티성 클래스이기에 애초에 인스턴스로 만들어 사용하려고 설계된 게 아니기 때문이다.

![Untitled 3](https://user-images.githubusercontent.com/49682056/216827722-8a64a385-f54d-402b-84e6-c5b6124092af.png)

또한 위와 같이 final 클래스와 관련한 메서드나 필드만을 모아 정의해놓은 클래스 또한 마찬가지이다.

### private 생성자

기본적으로 생성자를 명시하지 않으면 컴파일러가 자동으로 매개변수를 받지않는 기본 생성자를 만들어준다.

이는 개발자가 위와 같은 인스턴스화가 필요없는 클래스를 의도치않게 인스턴스 생성을 하게 할 수 있다.

그렇다고 추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.

하위 클래스를 만들어 인스턴스화를 할수 있기 때문이다.

이런 인스턴스화를 막기 위해서 private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다.

```java
public class UtiltityClass {
	// 기본 생성자가 만들어지는 것을 막는다(인스턴스화 방지용)
	private UtiltityClass() {
		throws new AssertionError();
	}
}
```

명시적 생성자가 private이니 클래스 바깥에서는 접근 할 수 없다.

위 코드에서 Error를 던지는 이유는 혹여나 실수로 클래스 내부에서 생성자를 호출하는 경우를 막기 위해 사용한다.

덤으로 직관적으로 알 수 없으므로 private로 생성자 위에 주석을 달아주도록 하자

그리고 이 방식은 더 이상 상속을 받지 못하게하는 효과도 있다. 하위 클래스를 생성할 때 명시적이던 묵시적이던 상위 클래스의 생성자를 호출하게 되는데 이때 상위 클래스의 생성자에 접근 할 수 없기 때문이다.

위에 설명한 클래스 또한 private 생성자로 인스턴스화를 막아놓은 것을 볼 수 있다.

![Untitled 4](https://user-images.githubusercontent.com/49682056/216827723-cd33f263-a61e-4b6f-a06d-fa577462b383.png)

![Untitled 5](https://user-images.githubusercontent.com/49682056/216827724-c36adc7c-72c1-475d-9306-ffb09fd49bb4.png)

![Untitled 6](https://user-images.githubusercontent.com/49682056/216827725-47f78b37-b550-420b-b0c7-cc4415504679.png)
### 실무에서는?

![Untitled 3](https://user-images.githubusercontent.com/49682056/216827722-8a64a385-f54d-402b-84e6-c5b6124092af.png)

위에서 봤던 RegexPattern의 경우 private 생성자가 없음에도 인스턴스화를 할 수 없다.

이는 @UtilityClass 어노테이션 자체에 기본 생성자가 private으로 생성되게 만들어 놓았으며 리플랙션이나 내부에서 생성자를 호출한 경우 UnsupportedOperationException을 발생시키도록 되있기 때문이다.

![Untitled 7](https://user-images.githubusercontent.com/49682056/216827726-ce937ecb-aa85-4017-b8c4-318b76b15244.png)

그리고 다른 방법으로는 Lombok에서 제공해주는 생성자 어노테이션에서 access를 통해 생성자의 접근 범위를 private으로 간단하게 private 생성자를 만들 수 있다.
