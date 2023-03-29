package org.example.item47;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SubListsV2 {
    /*
    for (int start = 0; start < src.size(); start++) {
        for (int end = start+1; end <= src.size; end++) {
            System.out.println(src.subList(start, end));
        }
    }
    */

    // 코드 47-7 입력 리스트의 모든 부분리스트를 스트림으로 반환한다.
    public static <E> Stream<List<E>> of(List<E> list) {
        return IntStream.range(0, list.size())
                .mapToObj(start -> IntStream.rangeClosed(start + 1, list.size())
                        .mapToObj(end -> list.subList(start, end)))
                .flatMap(x -> x);
    }
}
