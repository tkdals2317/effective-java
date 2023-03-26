package org.example.item45;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

// 코드 45-3 스트림을 적절히 활용하면 깔끔하고 명료해진다.
public class AnagramsV3 {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary)) {              // 사전 파일을 열고 스트림 변수 이름을 words로 지어 각 원소가 단어임을 명확히 한다.
            words.collect(                                                  // 종단 연산
                    groupingBy(AnagramsV3::alphabetize)                     // 모든 단어를 수집해 맵으로 모은다.
            ).values().stream()                                             // 맵의 values()가 반환한 값으로 새로운 Stream<List<String>을 연다.
                    .filter(group -> group.size() >= minGroupSize)          // 중간연산 : 리스트들 중 원소가 minGroupSize보다 적은 것은 필터링되 무시된다.
                    .forEach(g -> System.out.println(g.size() + " " + g));  // 종단 연산 : 필터링된 리스트를 출력한다.
        }

        "Hello world!".chars().forEach(System.out::print);
        "Hello world!".chars().forEach(x -> System.out.print((char) x));
    }
    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
