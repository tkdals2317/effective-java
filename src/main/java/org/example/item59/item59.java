package org.example.item59;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class item59 {
    // 코드 59-1 흔하지만 문제가 심각한 코드
    static Random rnd = new Random();
    
    private static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }

    public static void main(String[] args) throws IOException {
        int n = 2 *  (Integer.MAX_VALUE / 3);
        int low = 0;
        int low2 = 0;
        for (int i = 0; i < 1000000; i++) {
            if (random(n) < n/2) {
                low++;
            }
            if (rnd.nextInt(n) < n/2) {
                low2++;
            }
        }
        System.out.println(low);
        System.out.println(low2);

        // 코드 59-2 transferTo 메서드를 이용해 URL의 내용 가져오기 - 자바 9부터 가능하다.
        try (InputStream in = new URL(args[0]).openStream()) {
            in.transferTo(System.out);
        }
    }


}
