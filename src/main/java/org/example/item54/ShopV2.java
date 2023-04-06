package org.example.item54;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopV2 {

    private final List<Cheese> cheesesInStock = new ArrayList<>() {};

    public void addCheese(Cheese cheese) {
        cheesesInStock.add(cheese);
    }

    // 코드 54-2 빈 컬렉션을 반환하는 올바른 예
    public List<Cheese> getCheesesV1(){
        return new ArrayList<>(cheesesInStock);
    }

    // 코드 54-3 최적화 - 빈 컬렉션을 매번 새로 할당하지 않도록 했다.
    public List<Cheese> getCheesesV2(){
        return cheesesInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheesesInStock);
    }

    // 코드 54-4 길이가 0일 수도 있는 배열을 반환하는 올바른 방법
    public Cheese[] getCheesesArrV1() {
        return cheesesInStock.toArray(new Cheese[0]);
    }

    // 코드 54-5 최적화 - 빈 배열을 매번 새로 할당하지 않도록 했다.
    private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

    public Cheese[] getCheesesArrV2() {
        return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
    }

    // 코드 54-6 나쁜 예 - 배열을 미리 할당하면 성능이 나빠진다.
    public Cheese[] getCheesesArrV3() {
        return cheesesInStock.toArray(new Cheese[cheesesInStock.size()]);
    }

}
