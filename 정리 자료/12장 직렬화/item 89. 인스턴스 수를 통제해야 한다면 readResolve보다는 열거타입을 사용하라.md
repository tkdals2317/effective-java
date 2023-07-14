# item 89. 인스턴스 수를 통제해야 한다면 readResolve보다는 열거타입을 사용하라

<aside>
💡 싱글톤과 같이 인스턴스 수를 통제해야 하는 경우에는 readResolve 대신 Enum을 사용하자

</aside>

### readResolve

싱글턴 클래스에 implement Serializable을 추가하는 순간 그 클래스는 싱글턴 클래스가 아니게 된다.

기본 직렬화를 쓰지 않더라도, 그리고 명시적인 readObject를 제공하더라도 이 클래스가 초기화될 때 만들어진 인스턴스와는 별개인 인스턴스를 반환하게 된다.

그런 경우 readResolve 메서드 기능을 이용하면 readObject가 만들어낸 인스턴스를 다른 것으로 대체할 수 있다.

역직렬화한 객체의 클래스가 readResolve 메서드를 적절히 정의해뒀다면, 역직렬화 후 새로 생성된 객체를 인수로 이 메서드가 호출되고, 이 메서드가 반환한 객체 참조가 새로 생성된 객체를 대신해 반환된다.

대부분 이때 새로 생성된 체의 참조는 유지하지 않으므로 바로 가비지 컬렉션 대상이 된다.

```java

public class Elvis implements Serializable {

    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }

		//인스턴스 통제를 위한 readResove - 개선의 여지가 있다.
		private Object readResolve() {
			// 진짜 Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에게 맡긴다.
			return INSTANCE
		}

}
```

여기서 주의 할 점은 모든 인스턴스 필드는 transient로 선언해야 한다.

**사실, readResolve를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야 한다.**

### readResolve의 문제점

transient로 선언되지 않은 인스턴스필드가 존재할 시 객체 참조를 통한 공격이 가능하다.

싱글턴이 trnsient가 아닌 참조 필드를 가지고 있다면, 그 필드 내용은 readResolve 메서드가 실행되기 전에 역직렬화된다. 

잘 조작된 스트림을 써서 해당 참조 필드의 내용이 역직렬화되는 시점에 그 역직렬화된 인스턴스의 참조를 훔쳐 올 수 있다.

1. reaResolve 메서드와 인스턴스 필드 하나를 포함한 ‘도둑’ 클래스를 작성한다.
2. 이 인스턴스 필드는 도둑이 ‘숨길’ 직렬화된 싱글턴을 참조하는 역할을 한다.
3. 직렬화된 스트림에서 싱글턴의 비휘발성 필드를 이 도둑의 인스턴스로 교체한다.
4. 싱글턴은 도둑을 참조하고 도둑은 싱글턴을 참조하는 순환고리가 만들어진다.
5. 싱글턴이 도둑을 포함하므로 싱글턴이 역직렬화 될 때 도둑의 readResolve 메서드가 먼저 호출된다.
6. 그 결과, 도둑의 readResolve 메서드가 수행될 때 도둑의 인스턴스 필드에는 역직렬화 도중인  (그리고 reaResolve가 수행되기 전인) 싱글턴의 참조가 담겨있게 된다.
7. 도둑의 reaResolve 메서드는 이 인스턴스 필드가 참조한 값을 정적 필드로 복사하여 readResolve가 끝난 후에도 참조할 수 있도록 한다.
8. 그런 다음 이 메서드는 도둑이 숨긴 transient가 아닌 필드의 원래 타입에 맞는 값을 반환한다.

```java
// 코드 89-1 잘못된 싱글턴 - transient가 아닌 참조 필드를 가지고 있다!
public class Elvis implements Serializable {

    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    private String[] favoriteSongs = { "Hound Dog", "Heartbreak Hotel"};

    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }

    private Object readResolve() {
        return INSTANCE;
    }

}
```

```java
// 코드 89-2 도둑 클래스
public class ElvisStealer implements Serializable {

    static Elvis impersonator;
    private Elvis payload;

    private Object readResolve() {
        // resolve 되기 전의 Elvis 인스턴스의 참조를 저장한다.
        impersonator = payload;

        // favoriteSongs 필드에 맞는 객체를 반환한다.
        return new String[] { "A Fool Such as I"};
    }

    private static final long serialVersionUID = 0L;

}
```

```java
// 코드 89-3 직렬화의 허점을 이용해 싱글턴 객체를 2개 생성한다.
public class ElvisImpersonator {
    // 진짜 Elvis 인스턴스로는 만들어질 수 없는 바이트 스트림
    private static final byte[] serializedForm = new byte[] { (byte) 0xac,
            (byte) 0xed, 0x00, 0x05, 0x73, 0x72, 0x00, 0x05, 0x45, 0x6c, 0x76,
            0x69, 0x73, (byte) 0x84, (byte) 0xe6, (byte) 0x93, 0x33,
            (byte) 0xc3, (byte) 0xf4, (byte) 0x8b, 0x32, 0x02, 0x00, 0x01,
            0x4c, 0x00, 0x0d, 0x66, 0x61, 0x76, 0x6f, 0x72, 0x69, 0x74, 0x65,
            0x53, 0x6f, 0x6e, 0x67, 0x73, 0x74, 0x00, 0x12, 0x4c, 0x6a, 0x61,
            0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x4f, 0x62, 0x6a,
            0x65, 0x63, 0x74, 0x3b, 0x78, 0x70, 0x73, 0x72, 0x00, 0x0c, 0x45,
            0x6c, 0x76, 0x69, 0x73, 0x53, 0x74, 0x65, 0x61, 0x6c, 0x65, 0x72,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x01,
            0x4c, 0x00, 0x07, 0x70, 0x61, 0x79, 0x6c, 0x6f, 0x61, 0x64, 0x74,
            0x00, 0x07, 0x4c, 0x45, 0x6c, 0x76, 0x69, 0x73, 0x3b, 0x78, 0x70,
            0x71, 0x00, 0x7e, 0x00, 0x02 };

    public static void main(String[] args) {
        // ElvisStealer.impersonator를 초기화한 다음,
        // 진짜 Elvis(즉 Elvis.INSTANCE)를 반환한다.
        Elvis elvis = (Elvis) deserialize(serializedForm);
        Elvis impersonator = ElvisStealer.impersonator;

        elvis.printFavorites();
        impersonator.printFavorites();
    }

    // 주어진 직렬화 형태(바이트 스트림)로부터 객체를 만들어 반환한다.
    private static Object deserialize(byte[] sf) {
        try {
            return new ObjectInputStream(new ByteArrayInputStream(sf)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

==== output ====
[HoundDog, Heartbreak Hotel]
[A Fool Such as I]
```

서로 다른 2개의 Elvis 인스턴스를 생성할 수 있다는 걸 알 수 있다.

### 해결법

인스턴스 필드를 transient로 선언하여 이 문제를 고칠 수 있지만 Elvis를 원소 하나짜리 열거타입으로 바꾸는 편이 더 나은 선택이다.

[item 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라](https://www.notion.so/item-3-private-2e9523a1c72b41b1af32747be3ec90be?pvs=21) 에서 알려준 방법이다.

직렬화 가능한 인스턴스 통제 클래스를 열거 타입을 이용해 구현하면 선언한 상수 외의 다른 객체는 존재하지 않음을 보장해준다.

```java
// 코드 89-4 열거 타입 싱글턴 - 전통적인 싱글턴보다 우수하다
public enum Elvis {
    INSTANCE;

    private String[] favoriteSongs = { "Hound Dog", "Heartbreak Hotel"};

    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }

}
```

### readResolve는 과연 무쓸모일까?

직렬화 가능 인스턴스 통제 클래스를 작성해야 하는데, 컴파일타임에는 어떤 인스턴스들이 있는 지 알 수 없는 상황이라면 열거타입으로 표현하는 게 불가능하다.

이런 경우에는 reaResolve 메서드를 사용하자

### readResolve 메서드의 접근성은 매우 중요하다.

final 클래스에서라면 readResolve 메서드는 private이어야 한다. 

final이 아닌 클래스에서는 다음의 몇가지를 주의해서 고려해야 한다.

- private으로 선언하면 하위 클래스에서 사용할 수가 없다.
- package-private으로 선언하면 같은 패키지에 속한 하위 클래스에서만 사용할 수 있다.
- protected나 public이면서 하위 클래스에서 재정의하지 않았다면, 하위 클래스의 인스턴스를 역직렬화하면 상위 클래스의 인스턴스를 생성하여 ClassCastException을 일으킬 수 있다.

### 정리

불변식을 지키기 위해 인스턴스를 통제해야 한다면 가능한 한 열거타입을 사용하자

열거타입 대신 readResolve로 처리해야 될 경우 클래스의 모든 참조 타입 인스턴스 필드를 transient로 선언해야 한다