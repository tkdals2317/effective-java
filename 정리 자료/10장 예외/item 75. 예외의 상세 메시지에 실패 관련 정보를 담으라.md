# item 75. 예외의 상세 메시지에 실패 관련 정보를 담으라

<aside>
💡 예외의 상세 메시지를 작성하는 방법에 대해 알아보자

</aside>

### 스택 추적(stack trace)

예외를 잡지 못해 프로그램이 실패하면 자바 시슽메은 그 예외의 스택 추적(stack trace) 정보를 자동으로 출력한다.

스택 추적은 예외 객체의 toString 메서드를 호출해 얻는 문자열로, 보통은 예외 클래스 이름 뒤에 상세 메시지가 붙는 형태이다.

**이 정보가 실패 원인을 분석해야 하는 프로그래머가 얻을 수 있는 유일한 방법인 정보인 경우가 많다.**

따라서 예외의 toString 메서드에 실패원인에 관한 정보를 가능한 한 많이 담아 반환하는 일이 아주 중요하다.

⇒ 사후 분석을 위해 실패 순간의 상황을 정확히 포착해 예외의 상세 메시지에 담아야 한다.

### 실패 순간을 포착하려면 발생한 예외에 관여된 모든 매개변수와 필드의 값을 실패 메시지에 담아야한다.

관련 데이터를 모두 담아야 하지만 장황할 필요는 없다.

문제를 분석하는 사람은 스택 추적 뿐만 아니라 관련 문서와 소스코드도 함께 살펴 본다.

스택 추적 에는 예외가 발생한 파일 이름과 줄번호는 물론 스택에서 호출 한 다른 메서드들의 파일 이름과 줄번호까지 정확히 기록되어 있는 게 보통이다.

그러니 문서와 소스코드에서 얻을 수 있는 정보는 굳이 넣지 않아도 된다.

### 보안과 관련된 정보는 주의해서 다루자

문제를 진단하고 해결하는 과정에서 스택 추적 정보를 많은 사람들이 볼 수 있으므로 상세 메시지나 암호 키 같은 정보까지 담아서는 안된다.

### 예외의 상세 메시지와 최종 사용자에게 보여줄 오류 메시지를 혼동해서는 안된다.

최종 사용자에게는 친절한 안내 메시지를 보여주고,

예외 메시지는 가독성보다는 담긴 내용이 훨씬 중요하다.

우리 프로젝트에서 Exception에 따른 @ControllerAdvice 가 붙은 ExceptionHandler가 그 역할을 한다. 사용자에게 보여줄 메시지는 따로 담아서 보내고, 예외 상황 추적을 위한 정보는 log로 남긴다.

### 예외 생성자에서 모두 받아서 상세 메시지까지 미리 생성하는 방법

실패를 적절히 포착하려면 필요한 정보를 예외 생성자에서 받아서 상세 메시지까지 미리 생성해놓는 방법도 괜찮다.

직접 구현한 IndexOutOfBoundsException 예제를 보면서 이해를 해보자

```jsx
public class IndexOutOfBoundsException extends RuntimeException {
    private final int lowerBound;

    private final int upperBound;

    private final int index;

    /**
     * IndexOutOfBoundsException를 생성한다.
     * @param lowerBound 인덱스의 최솟값
     * @param upperBound 인덱스의 최댓값
     * @param index 인덱스의 실젯값
     */
    public IndexOutOfBoundsException(int lowerBound, int upperBound, int index) {
        // 실패를 포착하는 상세 메시지를 생성한다.
        super(String.format("최솟값 : %d, 최댓값 : %d, 인덱스 : %d", lowerBound, upperBound, index));
        // 프로그램에서 이용할 수 있도록 실패 정보를 저장해둔다.
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.index = index;
    }
		
		// 예외는 실패와 관련한 정보를 얻을 수 있는 접근자 메서드를 적절히 제공하는 것이 좋다.
		public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public int getIndex() {
        return index;
    }
}
```

예외가 발생하는 순간 생성자를 통해 메시지를 만들기 위한 정보들을 저장하고 있다.

예외는 실패와 관련한 정보를 얻을 수 있는 접근자 메서드를 적절히 제공하는 것이 좋다.

포착한 실패 정보는 예외 상황을 복구하는 데 유용할 수 있으므로 접근자 메서드는 비검사 예외보다는 검사 예외에서 더 빛을 발한다.

### 정리

최근 Exception 관련 핸들러를 개발하는 중이였는데 마침 이번 아이템을 통해 많은 인사이트를 얻어 가는 것 같다.

예외 메시지를 어떻게 처리할 지 고민이였는데 이번 아이템에서 나온 생성자에 필요한 정보를 담아 CustomException을 만들면 좋을 것 같다.