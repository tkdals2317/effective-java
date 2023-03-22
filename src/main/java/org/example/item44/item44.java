package org.example.item44;

import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.function.*;

public class item44 {
    public static void main(String[] args) {
        CacheLinkedHashMap<Integer, Integer> map = new CacheLinkedHashMap<>();
        for (int i = 0; i < 200; i++) {
            map.put(i, i);
        }
        System.out.println(map); // {100=100, 101=101, 102=102, 103=103, ..., 199=199}


        LinkedHashMap<Integer, Integer> map2 = new LinkedHashMap<>();
        for (int i = 0; i < 200; i++) {
            map2.put(i, i);
        }
        System.out.println(map2); // {0=0, 1=1, 2=2, 3=3, 4=4, 5=5, 6=6, 7=7, 8=8, 9=9, 10=10, 11=11, ... 199=199}

        UnaryOperator<String> unaryOperatorToLowerCase = String::toLowerCase;

        String str = "IDENTITY";
        String apply = unaryOperatorToLowerCase.apply(str);
        System.out.println(apply); // identity

        BinaryOperator<BigInteger> binaryOperatorAdd = BigInteger::add;
        BinaryOperator<Integer> binaryOperatorSum = Integer::sum;
        Predicate<Collection<Object>> predicate = Collection::isEmpty;
        Consumer<String> consumer = System.out::print;
        Supplier<Instant> supplier = Instant::now;
        Function<Integer,String> function = (i)-> Integer.toString(i);

        BigInteger bigInteger = BigInteger.TEN; // 10
        BigInteger binaryOperatorAddApply = binaryOperatorAdd.apply(bigInteger, bigInteger);
        System.out.println(binaryOperatorAddApply); // 20


    }
}
