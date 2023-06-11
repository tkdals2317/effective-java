# item 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라

<aside>
💡 **싱글턴**이란 **인스턴스를 오직 하나만 생성할 수 있는 클래스**를 말한다.

싱글턴을 만드는 **세 가지 방법**에 대해 알아보자

</aside>

### 아이스 브레이킹

예제에 나온 Elvis 클래스에 대한 유래를 알아봤다. 뭔지 알고 보면 더 이해가 잘되니깐 ㅎㅎ..

![Untitled](https://user-images.githubusercontent.com/49682056/216204491-6d2caad2-1ddc-4c3f-adb0-cd6e0eb540e8.png)

---

### 방법 1. public static 멤버가 final 필드로 만드는 방법

- 생성자는 private으로 감춰준다.
- 접근할 수 있는 수단으로 public static 멤버를 사용한다

```java
public class Elvis {
	public static final Elvis INSTANCE = new Elvis();
	private Elvis() { ... }

	public void leaveTheBuilding()
}
```

여기서 private으로 선언된 생성자는 public static final 필드인 Elvis.INSTANCE가 초기화 될 떄 딱 한번만 수행된다.

public, protected 생성자가 없으므로 Elvis 클래스가 초기화 될때 만들어진 INSTANCE는 전체 시스템에서 하나뿐임을 보장한다.

클라이언트는 일반적인 방법으로는 새로운 인스턴스를 생성하지 못하지만 리플랙션 API를 통해 private 생성자를 호출 할 수 있다 ⇒ 생성자를 수정하여 두번 째 객체가 생성되려 할 때 예외를 던지게 만든다.

```java
private Elvis() {
	if (INSTANCE != null) {
		throws new RuntimeException("이미 생성된 객체가 존재합니다.");
	}
}
```

멀티 스레드 환경에서는 해당 클래스의 상태가 없도록 만들어주자

**장점**

- 해당 클래스가 싱글텀임이 API에 명백히 드러남
- 간결함

---

### 방법 2. 정적 팩터리 방식

- 생성자는 private으로 감춰준다.
- 접근할 수 있는 수단으로 public static 팩터리 메서드(아래 코드에서 getInsatnce 메서드)를 사용한다

```java
public class Elvis {
	private static final Elvis INSTANCE = new Elvis();
	private Elvis() { ... }
	public static Elvis getInstance() { return INSTANCE; }

	public void leaveTheBuilding()
}
```

방법 1과 유사해보이지만 인스턴스 자체는 private으로 감추고 getInstance() 메서드를 사용하여 정적 팩토리 방식으로 인스턴스를 얻는다.

**장점**

- API를 바꾸지 않고도 싱글턴이 아니게 바꿀 수 있다.
    - INSTANCE를 반환하던 걸 새로운 객체로 반환하게만 바꾸면 된다.
- 원한다면 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다.
- 정적 팩터리의 참조를 공급자(Supplier)로 사용할 수 있다. (잘 모르겠다)
    - Elvis::getInstance를 Supplier<Elvis>로 반환하여 사용한다.

방법 2 또한 방법1과 같이 리플렉션에 취약하다는 한계점이 있다.

이런 장점이 필요하지 않다면 방법 1의 public 필드 방식이 좋다

 

그리고 두 방식은 직렬화 이후 역직렬화 할 때 새로운 인스턴스를 반환한다는 문제점이 있다.

역직렬화에서는 기본 생성자가 아닌 값을 복사하여 새로운 인스턴스를 반환하기 때문이다.

이를 방지하기 위해 Serializable을 implements하고 readResolve() 메서드에서 구현하여 싱글턴 인스턴스를 반환하게 한다, 모든 필드에 trasient(직렬화에서 제외) 키워드를 넣는다.

---

### 방법 3. 원소가 하나인 열거 타입을 선언하는 방법

```java
public enum Elvis {
	INSTANCE; 
	
	public String getName() {
		return "Elvis";
	}

	public void leaveTheBuilding() { ... }
}
```

enum 타입은 기본적으로 직렬화 가능하므로 Serializable 인터페이스를 구현할 필요가 없고, 리플렉션 문제도 발생하지 않는다.

부자연스러워 보이지만 싱글턴을 만드는 가장 좋은 방법이다.

하지만 상속을 해야되는 상황에서는 사용할 수 없다는 단점이 있다.