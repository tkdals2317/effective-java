package org.example.item50;

import java.util.Date;

// 코드 50-1 기간을 표현하는 클래스 - 불변식을 지키지 못했다.
public final class PeriodV2 {
    private final Date start;
    private final Date end;

    /**
     * @param start 시작 시간
     * @param end 종료 시간
     * @throws IllegalArgumentException 시작 시간이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발생한다.
     */
    // 코드 50-3 수정한 생성자 - 매개변수 방어적 복사본을 만든다.
    public PeriodV2(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
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
