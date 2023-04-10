package org.example.item55;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

public class item55 {

    // 코드 55-1 컬렉션에서 최댓값을 구한다(컬렉션이 비었으면 예외를 던진다.)
    public static <E extends Comparable<E>> E  maxV1(Collection<E> c) {
        if (c.isEmpty()) {
            throw new IllegalArgumentException("빈 컬렉션");
        }

        E result = null;

        for (E e :c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return result;
    }

    // 코드 55-2 컬렉션에서 최댓값을 구해 Optional<E>를 반환한다.
    public static <E extends Comparable<E>> Optional<E> maxV2(Collection<E> c) {
        if (c.isEmpty()) {
            return Optional.empty();
        }

        E result = null;

        for (E e :c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return Optional.of(result);
    }


    // 코드 55-3 컬렉션에서 최댓값을 구해 Optional<E>를 반환한다. - 스트림 버전
    public static <E extends Comparable<E>> Optional<E> maxV3(Collection<E> c) {
        return c.stream().max(Comparator.naturalOrder());
    }

    public static void main(String[] args) {
        List<String> words = List.of("aaa", "bbb", "cccc");

        // 코드 55-4 옵셔널 활용 1 - 기본값을 정해둘 수 있다.
        String lastWordInLexicon1 = maxV3(words).orElse("단어 없음...");
        String lastWordInLexicon1_1 = maxV3(words).orElseGet(() -> new String("단어 없음ㅋㅋ..."));

        // 코드 55-5 옵셔널 활용 2 - 원하는 예외를 던질 수 있다.
        String lastWordInLexicon2 = maxV3(words).orElseThrow(IllegalArgumentException::new);

        // 코드 55-6 옵셔널 활용 3 - 항상 값이 채워져 있다고 가정한다.
        String lastWordInLexicon3 = maxV3(words).get();

        //Optional<ProcessHandle> parentProcess = ph.parent();
        //System.out.println("부모 PID " + (parentProcess.isPresent() ? String.valueOf(parentProcess.get().pid()) : "N/A"));
        // orElse로 대체 가능하다.
        //System.out.println("부모 PID " + (parentProcess.map(processHandle -> String.valueOf(processHandle.pid())).orElse("N/A")));


        Stream<String> stringStream = Stream.of(maxV3(words))
                .filter(Optional::isPresent)
                .map(Optional::get);

    }

}
