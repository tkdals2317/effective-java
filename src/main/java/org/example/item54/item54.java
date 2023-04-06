package org.example.item54;

import java.util.Arrays;
import java.util.List;

public class item54 {
    public static void main(String[] args) {
        ShopV1 shopV1 = new ShopV1();
        List<Cheese> cheeses = shopV1.getCheeses();

        if (cheeses != null && cheeses.contains(Cheese.STILTON)) {
            System.out.println("좋았어, 바로 그거야.");
        }

        ShopV2 shopV2 = new ShopV2();

        System.out.println(shopV2.getCheesesV1());
        System.out.println(shopV2.getCheesesV2());
        System.out.println(Arrays.toString(shopV2.getCheesesArrV1()));
        System.out.println(Arrays.toString(shopV2.getCheesesArrV2()));
        System.out.println(Arrays.toString(shopV2.getCheesesArrV3()));

        shopV2.addCheese(Cheese.STILTON);

        System.out.println(shopV2.getCheesesV1());
        System.out.println(shopV2.getCheesesV2());
        System.out.println(Arrays.toString(shopV2.getCheesesArrV1()));
        System.out.println(Arrays.toString(shopV2.getCheesesArrV2()));
        System.out.println(Arrays.toString(shopV2.getCheesesArrV3()));


    }
}
