package org.example.item45;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 코드 45-2 스트림을 과하게 사용했다. - 따라 하지 말 것!
public class AnagramsV2 {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary)) {
            words.collect(
                    Collectors.groupingBy(word -> word.chars().sorted() // alphabetize 메서드 로직
                            .collect(StringBuilder::new,
                                    (sb, c) -> sb.append((char)c),
                                    StringBuilder::append).toString())
            ).values().stream()
                    .filter(group -> group.size() >= minGroupSize)
                    .map(group -> group.size() + " " + group)
                    .forEach(System.out::println);
        }
    }
}
