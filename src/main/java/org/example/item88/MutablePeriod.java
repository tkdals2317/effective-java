package org.example.item88;

import java.io.*;
import java.util.Date;

// 코드 88-4 가변 공격의 예
public class MutablePeriod {

    // Period 인스턴스
    public final PeriodV3 period;

    // 시작 시각 필드 - 외부에서 접근할 수 없어야 한다.
    public final Date start;

    // 종료 시각 필드 - 외부에서 접근할 수 없어야 한다.
    public final Date end;

    public MutablePeriod() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);

            // 유효한 Period 인스턴스를 직렬화한다.
            out.writeObject(new PeriodV3(new Date(), new Date()));

            /*
             * 악의적인 '이전 객체 참조', 즉 내부 Date 필드로의 참조를 추가한다.
             * 상세 내용은 자바 객체 직렬화 명세의 6.4절을 참조하자
             */
            byte[] ref = { 0x71, 0, 0x7e, 0, 5 }; // 참조 #5
            bos.write(ref);
            ref[4] = 4;
            bos.write(ref);

            // Period 역직렬화 후 Date 참조를 '훔친다'
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

            this.period = (PeriodV3) in.readObject();
            this.start = (Date) in.readObject();
            this.end = (Date) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        MutablePeriod mp = new MutablePeriod();
        PeriodV3 p = mp.period;
        Date pEnd = mp.end;

        // 시간을 되돌리자!!
        pEnd.setYear(78);
        System.out.println(p);

        // 60년대로 회귀!
        pEnd.setYear(69);
        System.out.println(p);

    }
}
