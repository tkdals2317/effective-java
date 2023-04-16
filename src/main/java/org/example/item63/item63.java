package org.example.item63;

public class item63 {
    private static int LINE_WIDTH = 100;

    public static void main(String[] args) {
        long beforeTime = System.currentTimeMillis();
        System.out.println(statement());
        long afterTime = System.currentTimeMillis();
        System.out.println("소요 시간(m) : " + (afterTime - beforeTime));

        long beforeTime2 = System.currentTimeMillis();
        System.out.println(statement2());
        long afterTime2 = System.currentTimeMillis();
        System.out.println("소요 시간(m) : " + (afterTime2 - beforeTime2));
    }

    private static String lineForItem(int i) {
        return "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    }

    private static int numItems() {
        return 1000;
    }

    // 코드 63-1 문자열 연결을 잘못 사용한 예 - 느리다!
    public static String statement() {
        long beforeTime = System.currentTimeMillis();
        String result = "";
        for (int i = 0; i < numItems(); i++) {
            result += lineForItem(i);
        }

        return result;
    }

    // 코드 63-2 StringBuilder를 사용하면 문자열 연결 성능이 크게 개선된다.
    public static String statement2() {
        StringBuilder sb = new StringBuilder(numItems() * LINE_WIDTH);
        for (int i = 0; i < numItems(); i ++) {
            sb.append(lineForItem(i));
        }
        return sb.toString();
    }
}
