package org.example.item50;

import java.util.Date;

// 코드 50-1 기간을 표현하는 클래스 - 불변식을 지키지 못했다.
public final class PeriodV1 {
    private final Date start;
    private final Date end;

    /**
     * @param start 시작 시간
     * @param end 종료 시간
     * @throws IllegalArgumentException 시작 시간이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발생한다.
     */
    public PeriodV1(Date start, Date end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
        }
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "PeriodV1{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
