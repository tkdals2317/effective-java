# item 11. equals를 재정의하려거든 hashCode도 재정의 하라

<aside>
💡 **equals를 재정의한 클래스 모두에서 hashCode도 재정의해야 한다.**
그렇지 않으면 HashMap이나 HashSet 같은 컬렉션의 원소로 사용할 때 문제를 일으킬 것이다.
**이번 아이템에서 hashCode를 재정의하는 법을 배워보자.**

</aside>

### Object 명세에서 발췌한 규약

> - equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다. 
  단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.

- equals(Object)가 두 객체가 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.

- equals(Object)가 두 객체가 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다.
  단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.
> 

**hashCode를 재정의를 잘못했을 때 크게 문제가 되는 조항은 두번째이다. 즉, 논리적으로 같은 객체는 같은 해시코드를 반환해야 한다.**

```java
// hashCode를 재정의하지 않은 PhoneNumber Map 사용 코드
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(010, 3170,2317), "이상민");

String name = m.get(new PhoneNumber(010, 3170,2317)); // "이상민"이 나온 것이 아니라 null을 반환한다
```

위 코드에서의 문제는 hashCode를 재정의해주면 해결될 수 있다.

### hashCode를 작성하는 요령

1. int 변수인 `result`를 선언한 후 값을 `c`로 초기화한다. 
    - c는 해당 객체의 **첫번째 핵심 필드**를 단계 2.1 방식으로 계산한 해시코드이다.
    - 핵심 필드는 `equals` 비교에 사용되는 필드를 말한다.
2. 해당 객체의 나머지 핵심 필드인 `f` 각각에 대해 다음 작업을 수행한다.
    1. 해당 필드의 해시코드 `c` 를 계산한다.
        1. 기본 타입 필드라면, `Type.hashCode(f)`를 수행한다. 여기서 *Type*은 해당 기본타입의 박싱 클래스다. ex) `Short.hashCode(f)`
        2. 참조 타입 필드면서, 이 클래스의 `equals` 메소드가 이 필드의 `equals`를 재귀적으로 호출하여 비교한다면, 이 필드의 `hashCode`를 재귀적으로 호출한다.
            - 계산이 복잡해질 것 같으면, 이 필드의 표준형을 만들어 그 표준형의 hashCode를 호출한다.
            - 필드의 값이 null이면 전통적으로 0을 사용한다.
        3. 필드가 배열이라면, 핵심 원소 각각을 별도 필드처럼 다룬다. 모든 원소가 핵심 원소라면 `Arrays.hashCode`를 사용한다.
    2. 단계 2.1에서 계산한 해시코드 `c`로 `result`를 갱신한다.
        - `result` = 31 * `result` + `c`;
3. `result`를 반환한다.

- 검증할 단위 테스트를 작성하자.
- 파생 필드는 계산에서 제외해도 된다.
- equals 비교에 사용되지 않은 필드는 반드시 제외하자.
- 31을 곱하는 이유는 31은 홀수이여서 소수이기 때문이다. 왜 소수를 쓰는 지는 모르지만 전통적으로 그리 해왔다.

### hashCode를 작성해보자

**전형적인 hashCode 메서드**

```java
@Override
public int hashCode() {
		int result = 31 * result * Short(areaCode);
		result = 31 * result * Short(prefix);
		result = 31 * result * Short(lineNum);
		return result;
}
```

**한줄 짜리 hashCode** 

```java
@Override
public int hashCode() {
		return Objects.hash(lineNum, prefix, areaCode);
}
```

- 첫번 째 코드보다 간결하지만 입력인수를 받기 위한 배열 생성, 박싱과 언박싱의 이유로 성능이 아쉬우므로 성능이 민감하지 않을 경우에만 사용하자

**클래스가 불변이고 해시코드를 계산하는 비용이 크다면 캐싱하는 방식을 고려하자**

```java
private int hashCode; // 자동으로 0으로 초기화 된다.

@Override
public int hashCode() {
		int result = hashCode;
		if (result == 0) {
				int result = 31 * result * Short(areaCode);
				result = 31 * result * Short(prefix);
				result = 31 * result * Short(lineNum);
				hashCode = result;
		}
		return result;
}
```

- 지연 초기화 방식을 사용하려거든 스레드 안전성까지 고려해야 한다는 점!
- 초깃값은 흔히 생성되는 객체의 해시코드와는 달라야 한다.

### 성능을 높인답시고 해시코드를 계산할 때 핵심 필드를 생략해서는 안 된다.

속도야 빨라지겠지만, 해시 품질이 나빠져 해시테이블 성능을 심각하게 떨어 뜨릴 수 있다.

### hashCode가 반환하는 값의 생성 규칙을 API 사용자에게 자세히 공표하지 말자

그래야 클라이언트가 이 값에 의지하지 않게 되고, 추후에 계산 방식을 바꿀 수도 있다.

 

### 정리

equals를 재정의 할 때는 hashCode도 반드시 재정의해주자!

재정의한 hashCode는 Object의 API 문서에 기술된 일반 규약을 따라야하며, 서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현하자!