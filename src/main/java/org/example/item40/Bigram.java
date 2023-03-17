package org.example.item40;

import java.util.HashSet;
import java.util.Objects;

// 코드 40-1 영어 알파벳 2개로 구성된 문자열을 표현하는 클래스 - 버그를 찾아보자
public class Bigram {

    private final char first;

    private final char second;

    public Bigram(char first, char second) {
        this.first = first;
        this.second = second;
    }

//    public boolean equals(Bigram b) {
//        return b.first == first && b.second == second;
//    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bigram)) {
            return false;
        }
        Bigram b = (Bigram) o;
        return b.first == first && b.second == second;
    }

    @Override
    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        HashSet<Bigram> bigrams = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                bigrams.add(new Bigram(ch, ch));
            }
        }
        System.out.println(bigrams.size()); // 26을 기대하지만 260이 출력된다!
    }
}
