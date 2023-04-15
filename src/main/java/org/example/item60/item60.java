package org.example.item60;

import java.math.BigDecimal;

public class item60 {
    public static void main(String[] args) {
        System.out.println(1.03 - 0.42); //0.6100000000000001
        System.out.println(1.00 - 9 * 0.10); //0.09999999999999998

        doubleMethod();
        BigDecimalMethod();
        intMethod();
    }

    private static void doubleMethod() {
        // 코드 60-1 오류 발생! 금융 계산에 부동소수 타입을 사용했다.
        double funds = 1.00;
        int itemBought = 0;
        for (double price = 0.10; funds >= price; price+= 0.10) {
            funds -= price;
            itemBought++;
        }
        System.out.println(itemBought + "개 구입");
        System.out.println("잔돈(달러) : " + funds);
    }


    private static void BigDecimalMethod() {
        // 코드 60-2 BigDecimal을 사용한 해범. 속도가 느리고 쓰기 불편하다.
        final BigDecimal TEN_CENTS = new BigDecimal(".10");

        BigDecimal funds = new BigDecimal("1.00");
        int itemBought = 0;
        for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)) {
            funds = funds.subtract(price);
            itemBought++;
        }
        System.out.println(itemBought + "개 구입");
        System.out.println("잔돈(달러) : " + funds);
    }


    private static void intMethod() {
        // 코드 60-3 정수 타입을 사용한 해법
        int funds = 100;
        int itemBought = 0;
        for (int price = 10; funds >= price; price += 10) {
            funds -= price;
            itemBought++;
        }
        System.out.println(itemBought + "개 구입");
        System.out.println("잔돈(센트) : " + funds);
    }
}
