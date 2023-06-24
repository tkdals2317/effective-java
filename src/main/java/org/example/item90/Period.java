package org.example.item90;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;

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


    private void readObject(ObjectInputStream s) throws InvalidObjectException {
        throw new InvalidObjectException("프록시가 필요합니다.");
    }

    // 직렬화 프록시 패턴용 writeReplace 메서드
    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    // 코드 90-1 Period 클래스용 직렬화 프록시
    private static class SerializationProxy implements Serializable { // (5) 그리고 바깥 클래스와 직렬화 프록시 모두 Serializable을 구현한다고 선언해야 한다.

        // (1) 바깥 클래스의 논리적 상태를 정밀하게 표현하는 중첩 클래스를 설계해 private static으로 선언한다.
        private final Date start;
        private final Date end;

        // (2) 이 중첩 클래스의 생성자는 단 하나여야 하며, 바깥 클래스를 매개변수로 받아야한다.
        SerializationProxy(Period p){
            // (3) 이 생성자는 단순히 인수로 넘어온 인스턴스의 데이터를 복사한다.
            // (4) 일관성 검사나 방어적 복사도 필요 없다.
            this.start = p.start;
            this.end = p.end;
        }

        private static final long serialVersionUID = 234098243823485285L; // 아무 값이나 상관 없다. (아이템 87)

        // Period.SerializationProxy용 readResolve 메서드
        private Object readResolve() {
            return new Period(start, end);
        }
    }
}
