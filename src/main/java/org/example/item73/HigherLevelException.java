package org.example.item73;

public class HigherLevelException extends RuntimeException{

    public HigherLevelException() {

    }
    // 코드 73-3 예외 연쇄용 생성자
    public HigherLevelException(LowerLevelException cause) {
        super(cause);
    }
}
