package org.example.item50;

import java.util.Date;

public class item50 {
    public static void main(String[] args) {
        // 코드 50-2 PeriodV1 인스턴스의 내부를 공격해보자
        Date start = new Date();
        Date end = new Date();

        PeriodV1 periodV1 = new PeriodV1(start, end);
        end.setYear(78); // periodV1의 내부를 수정했다!

        System.out.println(periodV1);

        Date start1 = new Date();
        Date end1 = new Date();

        PeriodV2 periodV2 = new PeriodV2(start1, end1);
        end1.setYear(78);

        System.out.println(periodV2);

        periodV2.getEnd().setTime(78);
        System.out.println(periodV2);


        // 코드 50-4 Period 인스턴스를 향한 두번 째 공격
        Date start2 = new Date();
        Date end2 = new Date();

        PeriodV2 period = new PeriodV2(start2, end2);
        period.getEnd().setYear(78);

        System.out.println(period);

        Date start3 = new Date();
        Date end3 = new Date();

        PeriodV3 periodV3 = new PeriodV3(start3, end3);
        periodV3.getEnd().setYear(78);

        System.out.println(periodV3);

    }
}
