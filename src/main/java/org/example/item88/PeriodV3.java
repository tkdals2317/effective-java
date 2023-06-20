package org.example.item88;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;


public final class PeriodV3 implements Serializable {

    private Date start;
    private Date end;

    /**
     * @param start 시작 시간
     * @param end 종료 시간
     * @throws IllegalArgumentException 시작 시간이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발생한다.
     */
    public PeriodV3(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
        }
    }


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
