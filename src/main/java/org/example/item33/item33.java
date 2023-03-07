package org.example.item33;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class item33 {
    // 코드 33-2 타입 안전 이종 컨테이너 패턴 - 클라이언트
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("이상민", 28);
        map.put("윤동우", 29);
        map.put("유병재", 29);

        Map<String, Integer> checkedMap = Collections.checkedMap(map, String.class, Integer.class);
        System.out.println(checkedMap);


        Favorites favorites = new Favorites();
        favorites.putFavorite(String.class, "Java");
        favorites.putFavorite(Integer.class, 9);
        favorites.putFavorite(Class.class, Favorites.class);
        //favorites.putFavorite(Long.class, "문자열을 넣으면 컴파일 에러가 난다!");
        String favoriteString = favorites.getFavorite(String.class);
        Integer favoriteInteger = favorites.getFavorite(Integer.class);
        Class<?> favoriteClass = favorites.getFavorite(Class.class);

        List<String> pets = Arrays.asList("개", "강아지", "앵무");

        System.out.printf("%s %x %s%n", favoriteString, favoriteInteger, favoriteClass);

        Map<Integer, String> map1 = new HashMap<>();
        map1 = Collections.checkedMap(map1, Integer.class, String.class);
        map1.put(1, "one");
        System.out.println(map1);

        Map map2 = map1;
        map2.put("two", 2);
        System.out.println(map2);
    }

    // 코드 33-5 asSubClass를 사용해 한정적 타입 토큰을 안전하게 형변환한다.
    static Annotation getAnnotation(AnnotatedElement element, String annotationTypeName) {

        Class<?> annotationType = null; // 비한정적 토큰
        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return element.getAnnotation(annotationType.asSubclass(Annotation.class));
    }
}
