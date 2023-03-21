package org.example.item43;

import java.util.HashMap;
import java.util.Map;

public class item43 {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        // 람다
        map.merge("key", 1 , (count, incr) -> count + incr);
        // 메서드 참조
        map.merge("key", 1 , Integer::sum);
    }
}
