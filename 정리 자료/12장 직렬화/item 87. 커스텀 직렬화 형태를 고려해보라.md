# item 87. 커스텀 직렬화 형태를 고려해보라

<aside>
💡 기본 직렬화와 커스텀 직렬화를 선택하는 기준과 사용법에 대해 알아보자

</aside>

### 기본 직렬화와 커스텀 직렬화의 차이

기본 직렬화의 형태는 유연성, 성능, 정확성 측면에서 신중히 고민한 후 합당할 때만 사용해야 한다.

**객체의 물리적 표현과 논리적 내용이 같다면 기본 직렬화 형태라도 무방하다.**

```java
// 코드 87-1 기본 직렬화 형태에 적합한 후보
// 객체의 물리적 표현과 논리적 표현이 같다면 기본 직렬화 형태라도 무방하다
public class Name implements Serializable {

    /**
     * 성. null이 아니어야 함
     * @serial
     */
    private final String lastName;

    /**
     * 이름. null이 아니어야 함
     * @serial
     */
    private final String firstName;

    /**
     * 중간 이름. 중간 이름이 없다면 null
     * @serial
     */

    private final String middleName;

    public Name(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }
}
```

성명은 논리적으로 이름, 성, 중간이름이라는 3개의 문자열로 구성되며, 앞 코드의 인스턴스 필드들은 이 논리적 구성요소를 정확히 반영했다.

**기본 직렬화 형태가 적합하다고 결정했더라도 불변식 보장과 보안을 위해 readObject 메서드를 제공해야 할 때가 많다.**

<aside>
💡 Name의 세 필드 모두 private 임에도 문서화 주석이 달려 있다. 이 필드들은 결국 클래스의 직렬화 형태에 포함되는 공개 API에 속하며 공개 API는 모두 문서화해야 하기 때문이다.
private 필드의 설명을 API 문서에 포함하라고 자바독에 알려주는 역할은 @serial 태그가 한다. 이 태그로 기술한 내용은 API 문서에서 직렬화 형태를 설명하는 특별한 페이지에 기록된다.

</aside>

**객체의 물리적 표현과 논리적 표현의 차이가 클 때는 기본 직렬화보다 커스텀 직렬화를 사용하자**

```java
// 코드 87-2 기본 직렬화 형태에 적합하지 않은 클래스
// 객체의 물리적 표현과 논리적 표현의 차이가 크다
public final class StringListV1 implements Serializable {

    private int size = 0;
    private Entry head = null;

    private static class Entry implements Serializable {
        String data;
        Entry next;
        Entry previous;
    }
}
```

물리적 표현과 논리적 표현의 차이가 클 때의 기본 직렬화 사용의 문제점

1. **공개 API가 현재의 내부 표현 방식에 영구히 묶인다.**
    1. StringListV1의 Entry가 다음 릴리즈에 변경되어도 해당 코드를 삭제할 수 없다.
2. **너무 많은 공간을 차지 할 수 있다.**
    1. 엔트리와 연결 정보는 내부 구현에 해당하니 직렬화 형태에 포함할 가치가 없다. 
3. **시간이 너무 많이 걸릴 수 있다.**
    1. 직렬화 로직은 객체 그래프의 위상에 관한 정보가 없으니 그래프를 직접 순회해볼 수 밖에 없어 속도가 느리다
4. **스택 오버플로를 일으킬 수 있다.**
    1. 기본 직렬화 과정은 객체 그래프를 재귀 순회하는데, 이 작업은 중간 정도 크기의 객체 그래프에서도 자칫 스택 오버플로를 일으킬 수 있다.

합리적인 커스텀 직렬화로 구현한 StringList 코드를 살펴 보자

```java
// 코드 87-3 합리적인 커스텀 직렬화 형태를 갖춘 StringList
public final class StringListV2 implements Serializable {

    private transient int size = 0;
    private transient Entry head = null;

    // 이제는 직렬화 되지 않는다.
    private static class Entry {
        String data;
        Entry next;
        Entry previous;
    }

    // 지정한 문자열을 이 리스트에 추가했다.
    public final void add(String s) {
    }

    /**
     * 이 {@code StringListV2} 인스턴스를 직렬화한다.
     *
     * @serialData 이 리스트의 크기(포함된 문자열의 개수)를 기록한 후
     * ({@code int}), 이어서 모든 원소를(각각은 {@code String})
     * 순서대로 기록한다.
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);

        // 모든 원소를 울바른 순서로 기록한다.
        for (Entry e = head; e != null; e = e.next)
            s.writeObject(e.data);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int numElements = s.readInt();
        // 모든 원소를 읽어 이 리스트에 삽입한다.
        for (int i = 0; i < numElements; i++) {
            add((String) s.readObject());

        }
    }
}
```

StringList의 필드 모두가 `transient`더라도 `writeObject`와 `readObject`는 각각 먼저 `defaultWriteObject`와 `defaultReadObject`를 호출한다.

클래스의 인스턴스 필드가 모둔 `transient`면 `defaultWriteObject`와 `defaultReadObject`를 호출하지 않아도 들었을지 몰라도, 직렬화 명세는 이 작업을 무조건하라고 요구한다.

이렇게 해야 향후 릴리스에서 `trasient`가 아닌 인스턴스 필드가 추가되더라도 상호 호환되기 때문이다.

신버전 인스턴스를 직렬화한 후 구버전으로 역직렬화하면 새로 추가된 필드들은 무시될 것이다.

구버전 `readObject`메서드에서 `defaultReadObject`를 호출하지 않는다면 역직렬화 할 때 `StreamCorruptedException`이 발생할 것이다.

그리고 기본 직렬화보다 성능적으로 훨씬 뛰어나다.

<aside>
💡 writeObject는 private 메서드임에도 문서화 주석이 달려있다. 위에서 말한 이유와 비슷한 원리로, 이 private 메서드는 직렬화 형태에 포함되는 공개 API에 속하기 때문이다.
필드용의 @serial 태그처럼 메서드에 달린 @serialData 태그는 자바독 유틸리티에 이 내용을 직렬화 형태 페이지에 추가하도록 요청하는 역할을 한다.

</aside>

### `trasient` 에 관하여

해당 객체의 논리적 상태와 무관한 필드라고 확신할 때만 `transient` 한정자를 생략해야 한다.

그래서 커스텀 직렬화 형태를 사용한다면 대부분의 (혹은 모든) 인스턴스 필드를 `transient` 로 선언해야된다.

기본 직렬화를 사용할 경우 `transient` 필드들은 역직렬화될 때 기본 값으로 초기화 됨을 잊지 말자.

### 기본 직렬화 사용 여부와 상관없이 객체의 전체 상태를 읽는 메서드에 적용해야 하는 동기화 메커니즘을 직렬화에도 적용해야 한다.

따라서 모든 메서드를 `synchronized` 로 선언하여 스레드 안전하게 만든 객체에서 기본 직렬화를 사용하려면 `writeObject` 메서드도 다음 코드처럼 `synchronized` 를 선언해야 한다.

```java
// 코드 87-4 기본 직렬화를 사용하는 동기화된 클래스를 위한 writeObject 메서드
private synchronized void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
}
```

`wirteObject` 메서드 안에서 동기화하고 싶다면 클래스의 다른 부분에서 사용하는 락 순서를 똑같이 따라야 한다. 

⇒ 그렇지 않으면 자원 순서 교착상태에 빠질 수 있다.

### 어떤 직렬화 형태를 택하든 직렬화 가능 클래스 모두에 직렬 버전 UID를 명시적으로 부여하자

이렇게 하면 직렬 버전 UID가 일으키는 잠재적인 호환성 문제가 사라진다.

런타임 시에 UID를 만들기 위해 드는 비용도 줄일 수 있다.

```java
private static final long serialVersionUID = <무작위로 고른 long 값>;
```

새로 작성하는 클래스에서는 어떤 long 값을 선택하던 상관 없다. 클래스 일련번호를 생성해주는 serialver 유틸리티를 사용해도 되며, 그냥 생각나는 아무 값이나 선택해도 된다.

직렬 버전 UID가 꼭 고유할 필요는 없다.

한편 직렬 버전 UID가 없는 기존 클래스를 구버전으로 직렬화된 인스턴스와 호환성을 유지한 채 수정하고 싶다면, 구 버전에서 사용한 자동 생성된 값을 그대로 사용해야 한다.

이 값은 직렬화된 인스턴스가 존재하는 구 버전 클래스를 serialver 유틸리티에 입력을 주어 실행하면 얻을 수 있다.

기본 버전 클래스와의 호환성을 끊고 싶다면 단순히 직렬 버전 UID의 값을 바꿔주면 된다. (역직렬화 시 InvalidClassException을 던진다)

**구버전으로 직렬화된 인스턴스들과의 호환성을 끊으려는 경우를 제외하고는 직렬 버전 UID를 절대 수정하지 말자**

### 정리

클래스를 직렬화하기로 결심했다면 어떤 직렬화 형태를 사용할 지 심사숙고하자

**객체의 물리적 표현과 논리적 표현의 차이가 없을 때 ⇒ 기본 직렬화**

**객체의 물리적 표현과 논리적 표현의 차이가 클 때 ⇒ 커스텀 직렬화**

직렬화 형태에 포함된 필드도 릴리즈된 후에는 수정하기 힘드므로 잘못된 직렬화 형태를 선택하면 해당 클래스의 복잡성과 성능에 영구히 부정적인 영향을 남기므로 신중히 설계하자