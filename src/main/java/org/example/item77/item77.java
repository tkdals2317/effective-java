package org.example.item77;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class item77 {
    public static void main(String[] args) {
        // catch 블록을 비워두면 예외가 무시된다. 아주 의심스러운 코드이다.
        try {

        } catch (RuntimeException e) {

        }

        /*
        Future<Integer> f = exec.submit(planarMap::chromaticNumber);
        int numColors = 4; // 기본값, 어떤 지도라도 이 값이면 충분하다.

        try {
            numColors = f.get(1L, TimeUnit.SECONDS);
        } catch (TimeoutException | ExcutionException ignored) {
            // 기본 값을 사용한다.(색상 수를 최소화하면 좋지만, 필수는 아니다)
        }
        */
    }
}
