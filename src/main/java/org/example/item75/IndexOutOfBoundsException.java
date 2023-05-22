package org.example.item75;


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
