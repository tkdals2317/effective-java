package org.example.item88;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;


public final class PeriodV2 implements Serializable {

    private final Date start;
    private final Date end;

    /**
     * @param start 시작 시간
     * @param end 종료 시간
     * @throws IllegalArgumentException 시작 시간이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발생한다.
     */
    public PeriodV2(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
        }
    }

    // 코드 88-3 유효성 검사를 수행하는 readObject 메서드 - 아직 부족하다
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

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
