package org.example.item52;

import java.math.BigInteger;
import java.util.*;

// 코드 52-1 컬렉션 분류기 - 오류! 이 프로그램은 뭘 출력할까?
public class CollectionClassifier {

    //public static String classify(Set<?> c) { return "집합";}
    //public static String classify(List<?> c) { return "리스트";}
    //public static String classify(Collection<?> c) { return "그 외";}
    public static String classify(Collection<?> c) {
        return c instanceof Set ? "집합" :
               c instanceof List ? "리스트" : "그 외";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        for (Collection<?> c : collections) {
            System.out.println(classify(c));
        }
    }
}
