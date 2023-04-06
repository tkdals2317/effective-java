package org.example.item54;

import java.util.ArrayList;
import java.util.List;

public class ShopV1 {

    private final List<Cheese> cheesesInStock = new ArrayList<>() {};

    // 코드 54-1 컬렉션이 비었으면 null을 반환한다.
    /**
     * @return 매장 안의 있는 모든 치즈 목록을 반환한다.
     *         단, 제고가 하나도 없다면 null을 반환한다.
     */
    public List<Cheese> getCheeses(){
        return cheesesInStock.isEmpty() ? null : new ArrayList<>(cheesesInStock);
    }

}
