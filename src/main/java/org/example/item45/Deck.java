package org.example.item45;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Deck {
    public static void main(String[] args) {
        System.out.println(newDeckV1());
        System.out.println(newDeckV2());
    }
    
    // 코드 45-4 데카르트 곱 계산을 반복 방식으로 구현
    private static List<Card> newDeckV1() {
        List<Card> result = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                result.add(new Card(suit, rank));
            }
        }
        return result;
    }
    // 코드 45-5 데카르트 곱 계산을 스트림 방식으로 구현
    private static List<Card> newDeckV2() {
        return Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values())
                        .map(rank -> new Card(suit, rank)))
                .collect(Collectors.toList());
    }

    

    private static class Card {
        Suit suit;
        Rank rank;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }

        @Override
        public String toString() {
            return suit.toString() + " " +rank.toString();
        }
    }

    private enum Suit {
        SPADE, HEART, DIAMOND, CLUB
    }

    private enum Rank {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE
    }
}
