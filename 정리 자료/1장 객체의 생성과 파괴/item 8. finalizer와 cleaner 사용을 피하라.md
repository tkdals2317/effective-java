# item 8. finalizer와 cleaner 사용을 피하라

### 개요

자바는 두 가지 객체 소멸자 `finalizer`와 `cleaner` 제공한다.

이번 장에서는 이 두 객체 소멸자를 사용하지 말아야 할 이유에 대해 알아보자

---

### C++과의 차이점

C++의 파괴자(destructor)와 JAVA의 `finalizer`와 `cleaner`는 다른 개념이다.

C++에서의 파괴자는 생성자의 꼭 필요한 대척점으로 특정 객체와 관련되 자원을 회수하는 방법이다.

하지만 자바에서는 가비지 컬렉터에게 접근 할 수 없게 된 객체를 회수하는 역할을 담당시키고, 프로그래머에게 아무런 작업도 요구하지 않는다.

하지만 자바에서는 try-with-resource와 try-finally를 사용하여 해결한다

---

### `finalizer`와 `cleaner`는 즉시 수행된다는 보장이 없다

객체가 접근 할 수 없게 된 후 finalizer와 cleaner가 실행되기까지 얼마나 걸릴지 알 수 없다.

**즉,** `finalizer`와 `cleaner`**로는 제때 실행되어야 하는 작업은 절대 할 수 없다.**

예를 들어 파일 닫기를 finalizer나 cleaner에 맡기면 시스템이 동시에 열 수 있는 파일 개수가 한계가 있기 때문에 시스템에 중대한 오류를 일으킬 수 있다.

시스템이 `finalizer` 나 `cleaner` 실행을 게을리하여 파일을 계속 열어 둔다면 새로운 파일을 열지 못해 프로그램이 실패할 수 있다.

`finalizer`와 `cleaner`가 실행되는 시점은 전적으로 가비지 컬렉터의 알고리즘에 달렸으며, 이는 가비지 컬렉터 구현마다 천차만별이다.

클래스에 finalizer를 달아두면 그 인스턴스의 자원 회수가 제멋대로 지연될 수도 있다.

---

### `finalizer`와 `cleaner`는 수행 여부를 보장하지 않는다

접근할 수 없는 객체에 딸린 종료 작업을 전혀 수행하지 못한 채 프로그램이 중단될 수도 있다. (그럼 왜 있는거야..?)

**따라서 프로그램 생애주기와 상관없는, 상태를 영구적으로 수정하는 작업에서는 절대 finalizer와 cleaner에 의존해서는 안된다.**

예를 들어 데이터베이스 같은 공유 자원의 영구 락(lock) 해제를 `finalizer`와 `cleaner`에게 맡기면 분산 시스템 전체가 멈출 수 있다.

**System.gc나 System.runFinalization 메서드에 현혹되지 말자**

`finalizer`와 `cleaner`의 실행될 가능성을 높여줄 수 있으나, 보장해주진 않는다.

사실 이를 보장해주겠다는 메서드가 2개 있었다.

바로 `System.runFinalizersOnExit`와 그 쌍둥이인 `Runtime.runFinalizersOnExit`다.

하지만 이 두메서드는 ThreadStop이라는 심각한 결함 때문에 수십년간 지탄 받아 왔다.

---

### `finalizer`동작 중 발생한 예외는 무시되며, 처리할 작업이 남았더라도 그 순간 종료된다.

finalizer에서 난 예외 때문에 해당 객체는 마무리가 덜 된 상태로 남게되고 이런 훼손된 객체를 다른 스레드가 사용하려 한다면 어떤 동작을 할지 예측할 수 없다.

보통의 경우 잡지 못한 예외가 스레드를 중단 시키고 스택 추적 내용을 출력하겠지만, 같은 일이 finalizer에서 일어나면 경고조차 출력하지 않는다.

그나마 cleaner를 사용하는 라이브러리는 자신의 스레드를 통제하기 때문에 이런 문제가 발생하지 않는다.

---

### `finalizer`와 `cleaner`는 심각한 성능 문제를 동반한다.

`AutoCloseble` 객체를 사용하는 것에 비해 `finalizer`와 `cleaner`를 사용하면 매우 느리다

---

### `finalizer`와 `cleaner` 는 보안 문제를 일으킬 수 있다.

finalizer는 생성자나 직렬화 과정에서 예외가 발생한다면 이 생성되다만 객체에서 악의적인 하위 클래스의 finalizer가 수행될 수 있게 된다. 

또한 이 finalizer는 정적 필드에 자신의 참조를 할당해 가비지 컬렉터가 수집하지 못하게 막을 수 있다. 

이를 막기 위해서 아무 일도 하지 않는 finalizer를 메서드를 만들고 final로 선언해 해결할 수 있다.

그렇다면 파일이나 스레드를 종료해야할 자원을 담고 있는 객체에 클래스에서는 어떻게 `finalizer`와 `cleaner` 를 대신할 수 있을까?

AutoCloseable을 구현해주고, 클라이언트에서 인스턴스를 다 쓰고 나면 close() 메서드를 호출해주면 된다. (일반적으로 예외가 발생하더라도 종료가 되도록 try-with-resources를 사용해야 한다)

---

### 단점이 이렇게 많은 `finalizer`와 `cleaner` 는 도대체 어디에 쓰는거야?

1. **자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망**
    - 호출해주리라는 보장은 없지만, 클라이언트가 실수로라도 close하지 않았을 시 늦게라도 해주는 것이 아에 안해주는 것보단 낫기 때문이다.
    - 대표적인 예로, FileInputStream, FileOutputStream, ThreadPoolExcutor가 있다
2. **네이티브 피어와 연결된 객체**
    - 네이티브 피어란 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말함
    - 네이티브 피어는 자바 객체가 아니므로 GC는 그 존재를 알지 못하므로 네이티브 객체까지 회수하지 못함
    - 이런 네이티브 피어 객체를 회수할 때 사용
    - 하지만 성능저하를 감당할 수 없거나, 네이티브 피어가 사용하는 자원을 즉시 회수해야 한다면 close 메서드를 사용하자

**Cleaner의 이용하여 안전망으로 사용하는 예제**

```java
public class Room implements AutoCloseable{
    private static final Cleaner cleaner = Cleaner.create();

    //청소가 필요한 자원, cleaner가 청소할 때 수거할 자원을 가진다. Room을 참조하면 순환으로 참조하기에 가비지 컬렉터의 대상이 되지 않으므로 Room을 참조해서는 안된다.
    private static class State implements Runnable{
        int numJunkPiles; // 방(Room) 안의 쓰레기 수

        public State(final int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }
        //close나 cleaner메소드가 호출한다.
        @Override
        public void run() {
            System.out.println("방 청소");
            numJunkPiles = 0;
        }
    }
		// 방의 상태, cleanable과 공유한다
    private final State state;
		
		// Cleanable 객체, 수거 대상이 된다면 방을 청소한다.
    private final Cleaner.Cleanable cleanable; 

    public Room(final int numJunkFiles) {
        this.state = new State(numJunkFiles);
        cleanable = cleaner.register(this, state); 
    }

    @Override
    public void close() throws Exception {
        cleanable.clean();
    }
}
```

**잘 짜여진 클라이언트의 코드**

```java
public class Senior {
	public static void main(String[] args) {
			try (Room myRoom = new Room(7)) {
					System.out.println("안녕");
			}
	}
}
```

기대한 대로 Senior 프로그램은 “안녕”을 출력한 후, 이어서 “방 청소”를 출력한다.

**잘못 짜여진 클라이언트의 코드**

```java
public class Junior {
	public static void main(String[] args) {
		  Room myRoom = new Room(99);
			System.out.println("아무렴");
	}
}
```

Junior 프로그램은 “방 청소”를 출력하기 기대하지만 한번도 출력되지 않는다.

앞에서 말한 단점인 “예측할 수 없다”고 한 상황이다.

위에서 “방 청소”를 출력하려면 System.gc()를 추가하면서 종료하기전에 “방 청소”를 출력 할 수는 있지만, 이건 100프로 출력한다고 보장하지 못한다.

### 정리

cleaner는 안전망의 역할이나 중요하지 않은 네이티브 자원 회수용으로만 사용하자, 물론 이런 경우라도 불확실성과 성능 저하에 주의하자!

그냥 `finalizer`와 `cleaner` 대신 `AutoCloseable` 을 구현하고 try-with-resource와 close()를 잘해주자!

사실 실무에서 딱히 쓸 일도 없을 것 같다ㅎㅎ