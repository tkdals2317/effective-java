package org.example.item46;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;


public class item46 {
    public static void main(String[] args) throws IOException {

        File file = new File(System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\item46\\input");

        Map<String, Long> frequencyV1 = getFrequencyV1(file);
        Map<String, Long> frequencyV2 = getFrequencyV2(file);

        System.out.println(frequencyV1);
        System.out.println(frequencyV2);
        System.out.println(getFrequencyTop10(frequencyV2));


        List<Album> albums = new ArrayList<>();
        albums.add(new Album("1번째 앨범", "이상민", 10));
        albums.add(new Album("2번째 앨범", "이상민", 20));
        albums.add(new Album("3번째 앨범(명반)", "이상민", 500));
        albums.add(new Album("4번째 앨범(망작)", "이상민", 2));
        albums.add(new Album("에넥도트", "이센스", 500));
        albums.add(new Album("에넥도트 2", "이센스", 2));
        // 코드 46-5 각 키와 해당 키의 특정 원소를 연관 짓는 맵을 생성하는 수집기
        Map<String, Album> topHits = albums.stream().collect(
                // Collectors.maxBy 는 Comparator<T> 를 받아 BinaryOperator<T> 를 돌려준다
                toMap(Album::getArtist, album -> album, maxBy(comparing(Album::getSales))));

        // 코드 46-7 마지막에 쓴 값을 취하는 수집기
        Map<String, Album> lastAlbum = albums.stream().collect(
                toMap(Album::getArtist, album -> album, (oldVal, newVal) -> newVal));

        System.out.println(topHits);

    }

    private static Map<String, Long> getFrequencyV1(File file) throws IOException {
        // 코드 46-1 스트림 패러다임을 이해하지 못한 채 API만 사용했다 따라 하지 말 것!
        Map<String, Long> freq = new HashMap<>();
        try (Stream<String> words = new Scanner(file).tokens()) {
            words.forEach(word -> {
                freq.merge(word.toLowerCase(), 1L, Long::sum);
            });
        }
        return freq;
    }

    private static Map<String, Long> getFrequencyV2(File file) throws IOException {
        // 코드 46-2 스트림을 제대로 활용해 빈도표를 초기화한다.
        Map<String, Long> freq;
        try (Stream<String> words = new Scanner(file).tokens()) {
            freq = words.collect(groupingBy(String::toLowerCase, counting()));
        }
        return freq;
    }

    private static List<String> getFrequencyTop10(Map<String, Long> freq) throws IOException {
        // 코드 46-3 빈도표에서 가장 흔한 단어 10개를 뽑아내는 파이프라인
        List<String> topTen = freq.keySet().stream()
                .sorted(comparing(freq::get).reversed())
                .limit(10)
                .collect(toList());

        return topTen;
    }

}
