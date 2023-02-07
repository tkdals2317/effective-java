# item 6. 불필요한 객체 생성을 피하라

### 개요

똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 있다.
잘못된 예와 그 개선된 예을 보며 그 이유에 대해 알아보자

### 예제 1. String 생성자

**안좋은 예**

```java
String s = new String("bikini"); // 절대 금지
```

위 코드는 실행 될 때마다 String 인스턴스를 새로 만든다. 

생성자에 넘겨지는 “bikini” 자체가 이 생성자로 만들어내려는 String과 기능적으로 완전히 똑같다

이 코드를 반복문이나 빈번히 호출되는 메서드 안에 있다면 쓸데없는 String 인스턴스가 수백만개가 만들어질수 있다.

**개선된 예**

```java
String s = "bikini";
```

이 코드는 새로운 인스턴스를 매번 만드는 대신 하난의 String 인스턴스를 사용한다.

그리고 가상머신 안에서 이와 똑같은 문자열 리터럴을 사용하는 모든 코드가 같은 객체를 사용함이 보장 된다.

---

### 예제 2. Boolean 생성자와 Boolean.valueOf(String)

**안좋은 예**

자바 9에서는 Boolean 생성자가 deprecated로 지정되었고 valueOf 메소드를 사용하는 것이 좋다.

```java
Boolean boolean = new Boolean(true);
```

**개선된 예**

생성자 대신 정적 패터리 메서드를 제공하는 불변 클래스에서는 정적 팩터리 메서드를 사용하여 불필요한 객체 생성을 피할 수 있다.

생성자는 호출 할 때마다 새로운 객체를 만들지만, **팩터리 메서드를 사용한다면 새로운 객체를 만들지 않고 이미 한번 만들어둔 객체를 재사용하여 인스턴스를 사용할 수 있다.**

```java
Boolean boolean = Boolean.valueOf(true);
```

<img width="404" alt="Untitled" src="https://user-images.githubusercontent.com/49682056/217239416-00c3e9e6-96a5-4cbc-b0b4-9e366cca047a.png">

```java
public static Boolean valueOf(boolean b) {
		return b ? Boolean.TRUE : Boolean.FALSE; // 만들어지 객체를 재사용한다
}
```

+불변 객체만이 아니라 가변 객체라 해도 사용 중에 변경되지 않을 것임을 안다면 재사용할 수 있다.

---

### 예제 3. String.matches

생성비용이 아주 비싼 객체도 있는데 이런 ‘비싼 객체’가 반복해서 필요하다면 캐싱하여 재사용하길 권한다.

예를 들어 주어진 문자열이 유효한 로마 숫자인지 확인하는 메서드를 작성한다고 해보자

**안좋은 예**

```java
static boolean isRomanNumeralSlow(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}
```

Pattern은 입력받은 정규표현식에 해당하는 유한 상태 머신을 만들기 때문에 인스턴스 생성비용이 비싸다.

메소드가 호출 될 때마다 Pattern객체가 생성되므로 성능이 중요한 상황에서 반복해 사용하기엔 적합하지 않다.

**개선된 예**

```java
public class RomanNumerals {
		private static final Pattern ROMAN = Pattern.compile(
		        "^(?=.)M*(C[MD]|D?C{0,3})"
		                + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
		
		static boolean isRomanNumeralFast(String s) {
		    return ROMAN.matcher(s).matches();
		}
}
```

정규표현식을 표현하는 불변인 Pattern 인스턴스를 클래스 초기화 과정에서 직접 생성해 캐싱해두고 나중에 메서드가 호출될 때 이 인스턴스를 재사용한다.

위 코드로 **성능 개선** 뿐만 아니라 개선전에는 존재 조차도 몰랐던 Pattern 인스턴스를 static final 필드로 이름을 지어주어 **코드의 의미가 더 훨씬 잘 드러난다**.

---

### 예제 4. 오토박싱

불필요한 객체를 만들어내는 또 다른 예로 오토박싱을 들 수 있다.

**안좋은 예**

```java
private static long sum() {
    Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++)
        sum += i;
    return sum;
}
```

위 예제는 정수의 모든 총합을 구하는 메서드로 int는 부족하니 long을 사용하여 계산하고 있다.

이 메서드는 정확한 답을 내긴 하지만 `sum` 변수를 long이 아니라 Long 타입으로 선언하여 불필요한 Long 인스턴스가 2^31개나 만들어지게 된다.

Long에서 long으로 바꾸는 것만으로 6.3초에서 0.59초로 빨라진다.

**박싱된 기본 타입보다는 기본타입을 사용하고 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.**

---

### 정리

단순히 이번 장의 내용이 “객체 생성이 비싸니 피해야 한다”로 오해하면 안된다.

프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것은 일반적으로 좋은 일일 수 있다.

하지만 불필요한 객체생성을 피해서 성능적인 이점을 얻을 수 있다는 것을 알아두자!
