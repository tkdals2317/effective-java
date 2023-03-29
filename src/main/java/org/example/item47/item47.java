package org.example.item47;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class item47 {
    public static void main(String[] args) {
        // 코드 47-1 자바 타입 추론의 한계로 컴파일 되지 않는다.
        //for (ProcessHandle ph : ProcessHandle.allProcesses()::iterator) {
            // 프로세스를 처리한다.
        //}
        // 코드 47-2 스트림 반복하기 위한 '끔직한' 우회 방법
        for (ProcessHandle ph : (Iterable<ProcessHandle>) ProcessHandle.allProcesses()::iterator) {
            // 프로세스를 처리한다.
        }

        for (ProcessHandle ph : iterableOf(ProcessHandle.allProcesses())) {

        }

        Set<String> list = Set.of("a", "b", "c");
        Collection<Set<String>> of = PowerSet.of(list);
        System.out.println(of.size());

        System.out.println(of.contains(Set.of("a")));
        System.out.println(of.contains(Set.of("d")));

    }

    // 코드 47-3 Stream<E>를 Iterable<E>로 중개해주는 어댑터
    private static <E> Iterable<E> iterableOf(Stream<E> stream) {
        return stream::iterator;
    }

    // 코드 47-4 Iterable<E>를 Stream<E>로 중개해주는 어댑터
    private static <E> Stream<E> streamOf(Iterable<E> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
