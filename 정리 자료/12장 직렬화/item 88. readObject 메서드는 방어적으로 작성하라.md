# item 88. readObject 메서드는 방어적으로 작성하라

<aside>
💡 readObject 메서드를 방어적으로 작성하는 방법을 알아보자

</aside>

### readObject도 또 하나의 public 생성자이다

```java
//코드 88-1 방어적 복사를 사용하는 불변 클래스
public final class Period implements Serializable {

    private final Date start;
    private final Date end;

    /**
     * @param start 시작 시간
     * @param end 종료 시간
     * @throws IllegalArgumentException 시작 시간이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발생한다.
     */
    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "PeriodV2{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
```

해당 클래스는 객체의 물리적 표현이 논리적 표현과 부합하므로 기본 직렬화 형태를 사용해도 나쁘지 않다고 생각할 수 있다.

단순히 `implements Serializable`를 붙히면 될 것 같지만 그것은 큰 오산이다.

readObject 메서드가 실질적으로 또 다른 public 생성자이기 때문에 다른 생성자와 동일하게 방어적으로 작성해야 한다.

위 방어적 복사처럼 인수가 유효한지 검사하고 필요하다면 매개변수를 방어적으로 복사해야 한다.

**쉽게 말해 readObject는 매개변수로 바이트 스트림을 받는 생성자라고 할 수 있다.**

불변식을 깨뜨릴 목적의 임의로 생성한 바이트 스트림로 불변식을 쉽게 깰 수 있다.

```java
// 코드 88-2 허용되지 않는 Period 인스턴스를 만들 수 있다.
public class BogusPeriod {
    //진짜 Period 인스턴스에서는 만들어질 수 없는 바이트 스트림
    private static final byte[] serializedForm = new byte[] { (byte) 0xac,
            (byte) 0xed, 0x00, 0x05, 0x73, 0x72, 0x00, 0x06, 0x50, 0x65, 0x72,
            0x69, 0x6f, 0x64, 0x40, 0x7e, (byte) 0xf8, 0x2b, 0x4f, 0x46,
            (byte) 0xc0, (byte) 0xf4, 0x02, 0x00, 0x02, 0x4c, 0x00, 0x03, 0x65,
            0x6e, 0x64, 0x74, 0x00, 0x10, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f,
            0x75, 0x74, 0x69, 0x6c, 0x2f, 0x44, 0x61, 0x74, 0x65, 0x3b, 0x4c,
            0x00, 0x05, 0x73, 0x74, 0x61, 0x72, 0x74, 0x71, 0x00, 0x7e, 0x00,
            0x01, 0x78, 0x70, 0x73, 0x72, 0x00, 0x0e, 0x6a, 0x61, 0x76, 0x61,
            0x2e, 0x75, 0x74, 0x69, 0x6c, 0x2e, 0x44, 0x61, 0x74, 0x65, 0x68,
            0x6a, (byte) 0x81, 0x01, 0x4b, 0x59, 0x74, 0x19, 0x03, 0x00, 0x00,
            0x78, 0x70, 0x77, 0x08, 0x00, 0x00, 0x00, 0x66, (byte) 0xdf, 0x6e,
            0x1e, 0x00, 0x78, 0x73, 0x71, 0x00, 0x7e, 0x00, 0x03, 0x77, 0x08,
            0x00, 0x00, 0x00, (byte) 0xd5, 0x17, 0x69, 0x22, 0x00, 0x78 };

    public static void main(String[] args) {
        Period p = (Period) deserialize(serializedForm);
        System.out.println(p);
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
```

위 코드를 실행하면 종료 시각이 시작 시각보다 앞서는 Period 인스턴스를 만들고 있다. (본인 컴퓨터에서 실행되지 않는다..)

보다시피 Period를 직렬화 할 수 있도록 선언한 것 만으로 클래스의 불변식을 깨뜨리는 객체를 만들 수 있게 된 것이다.

### readObject 메서드에 유효성 검사를 추가해보자

```java
// 코드 88-3 유효성 검사를 수행하는 readObject 메서드 - 아직 부족하다
private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();

    // 불변식을 만족하는지 검사한다.
    if (start.compareTo(end) > 0) {
        throw new InvalidObjectException(start + "가 " + end + "보다 늦다.");
    }
}
```

위 작업을 통해 공격자가 허용되지 않는 Period 인스턴스를 생성하는 일을 막을 수 있지만, 정상 Period 인스턴스에서 시작된 바이트 스트림 끝에 private Date 필드로의 참조를 추가하면 가변 Period 인스턴스를 만들어 낼 수 있다.

스트림 끝에 추가된 이 ‘악의적인 객체 참조’를 읽어 Period 객체의 내부 정보를 얻을 수 있다.

이 참조로 얻은 Date 인스턴스들을 수정 할 수 있으니, Period 인스턴스는 불변이 아니게 되는 것이다.

다음은 이 공격이 어떻게 이뤄지는지 보여주는 예다.

```java
// 코드 88-4 가변 공격의 예
public class MutablePeriod {

    // Period 인스턴스
    public final Period period;

    // 시작 시각 필드 - 외부에서 접근할 수 없어야 한다.
    public final Date start;

    // 종료 시각 필드 - 외부에서 접근할 수 없어야 한다.
    public final Date end;

    public MutablePeriod() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);

            // 유효한 Period 인스턴스를 직렬화한다.
            out.writeObject(new Period(new Date(), new Date()));

            /*
             * 악의적인 '이전 객체 참조', 즉 내부 Date 필드로의 참조를 추가한다.
             * 상세 내용은 자바 객체 직렬화 명세의 6.4절을 참조하자
             */
            byte[] ref = { 0x71, 0, 0x7e, 0, 5 }; // 참조 #5
            bos.write(ref);
            ref[4] = 4;
            bos.write(ref);

            // Period 역직렬화 후 Date 참조를 '훔친다'
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

            this.period = (Period) in.readObject();
            this.start = (Date) in.readObject();
            this.end = (Date) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        MutablePeriod mp = new MutablePeriod();
        Period p = mp.period;
        Date pEnd = mp.end;

        // 시간을 되돌리자!!
        pEnd.setYear(78);
        System.out.println(p);

        // 60년대로 회귀!
        pEnd.setYear(69);
        System.out.println(p);

    }
}
```

위 main 코드를 실행하면 다음과 같은 결과가 출력된다.

![Untitled](https://github.com/tkdals2317/effective-java/assets/49682056/c5cbed1b-c6c8-4109-adf0-e458061281ff)

Period 인스턴스는 불변식을 유지한 채 생성됐지만, 의도적으로 내부의 값을 수정할 수 있었다.

이 문제의 근원은 Period의 readObject 메서드가 방어적 복사를 충분히 하지 않은데 있다.

**객체를 역직렬화할 때는 클라이언트가 소유해서는 안 되는 객체 참조를 갖는 필드를 모두 반드시 방어적으로 복사해야 한다.**

```java
// 코드 88-5 방어적 복사와 유효성 검사를 수행하는 readObject 메서드
private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    // 가변 요소를 방어적으로 복사한다.
    start = new Date(start.getTime());
    end = new Date(start.getTime());
    // 불변식을 만족하는지 검사한다.
    if (start.compareTo(end) > 0) {
        throw new InvalidObjectException(start + "가 " + end + "보다 늦다.");
    }
}
```

방어적 복사를 유효성 검사보다 앞서 수행하며, Date의 `clone` 메서드는 사용하지 않았음에 주목하자.

두 조치(가변 요소 방어적 복사 & 불변식 검사) 모두 Period를 공격으로부터 보호하는 데 필요하다.

또한 final 필드는 방어적 복사가 불가능하니 주의하자. 

그래서 이 readObject 메서드를 사용하려면 start와 end 필드에서 final 한정자를 제거해야 한다.

위 코드로 아까 공격을 실행하면 다음과 같이 방어적 복사가 성공한 것을 볼 수 있다.

![Untitled 1](https://github.com/tkdals2317/effective-java/assets/49682056/0a6574f8-f529-400e-8d57-b39e92ec8bd2)
### 커스텀 방어적 readObject를 작성해야 하는 경우

transient 필드를 제외한 모든 필드의 값을 매개변수로 받아 유효성 검사 없이 필드에 대입하는 public 생성자를 추가해도 괜찮은 경우

⇒ 커스텀 readObject 메서드를 만들어 모든 유효성 검사와 방어적 복사를 수행해야 한다.

### final이 아닌 직렬화 가능 클래스에서의 readObject와 생성자의 공통점

마치 생성자처럼 readObject 메서드도 재정의 가능 메서드를 (직접적으로든 간접적으로든) 호출해서는 안 된다.

이 규칙을 어겼는데 해당 메서드가 재정의되면, 하위 클래스의 상태가 완전히 역직렬화되기 전에는 하위 클래스에서 재정의된 메서드가 실행되어 오작동으로 이어진다.

### 정리

readObject 메서드를 작성할 때는 언제나 public 생성자를 작성하는 자세로 임해야 한다.

readObject는 어떤 바이트 스트림이 넘어오더라도 유효한 인스턴스를 만들어내야 한다.

바이트 스트림이 진짜 직렬화된 인스턴스라고 가정해서는 안 된다. 

이번 아이템에서는 기본 직렬화 형태를 사용한 클래스로 예를 들었지만 커스텀 직렬화를 사용하더라도 모든 문제가 그대로 발생할 수 있다.

이어서 안전한 readObject 메서드를 작성하는 지침을 요약하면 다음과 같다.

- private이어야 하는 객체 참조 필드는 각 필드가 가리키는 객체를 방어적으로 복사하라. 불변 클래스 내의 가변 요소가 여기 속한다.
- 모든 불변식을 검사하여 어긋나는 게 발견되면 InvalidObjectException을 던진다. 방어적 복사 다음에는 반드시 불변식 검사가 뒤따라야 한다.
- 역직렬화 후 객체 그래프 전체의 유효성 검사를 해야한다면 ObjectInputValidation 인터페이스를 사용하라
- 직접적이든 간접적이든, 재정의 할 수 있는 메서드는 호출하지 말자