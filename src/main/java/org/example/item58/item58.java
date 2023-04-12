package org.example.item58;

import org.example.item39.Test;
import org.example.item57.Element;

import java.util.*;

public class item58 {
    private static class Card {
        Suit suit;
        Rank rank;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }

        @Override
        public String toString() {
            return suit.toString() + " " + rank.toString();
        }
    }

    enum Suit {CLUB, DIAMOND, HEART, SPADE}

    enum Rank {ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING}

    enum Face {ONE, TWO, THREE, FOUR, FIVE}

    static Collection<Suit> suits = Arrays.asList(Suit.values());
    static Collection<Rank> ranks = Arrays.asList(Rank.values());

    static Collection<Face> faces = EnumSet.allOf(Face.class);

    public static void main(String[] args) {
        Collection<Element> c = List.of(new Element(), new Element());

        // 코드 58-1 컬렉션 순회하기 - 더 나은 방법이 있다.
        for (Iterator<Element> i = c.iterator(); i.hasNext(); ) {
            Element e = i.next();
            // e와 i로 무언가를 한다.
        }

        String[] a = {"aa", "bb"};

        // 코드 58-2 배열 순회하기 - 더 나은 방법이 있다.
        for (int i = 0; i < a.length; i++) {
            // a[i]로 무언가를 한다.
        }

        Collection<Element> elements = List.of(new Element(), new Element());

        // 코드 58-3 컬렉션과 배열을 순회하는 올바른 관용구
        for (Element e : elements) {
            // e 로 무언가를 한다.
        }

        // 코드 58-4 버그를 찾아보자
//        List<Card> deck = new ArrayList<>();
//        for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
//            for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); ) {
//                deck.add(new Card(i.next(), j.next()));
//            }
//        }
        
        // 코드 58-5 같은 버그, 다른 증상
        for (Iterator<Face> i = faces.iterator(); i.hasNext();) {
            for (Iterator<Face> j = faces.iterator(); j.hasNext();) {
                System.out.println(i.next() + " " + j.next());
            }
        }


        // 코드 58-6 문제는 고쳤지만 보기 좋지 않다. 더 나은 방법이 있다.
        List<Card> deck2 = new ArrayList<>();
        for (Iterator<Suit> i = suits.iterator(); i.hasNext(); ) {
            Suit suit = i.next();
            for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); ) {
                deck2.add(new Card(suit, j.next()));
            }
        }
        
        // 코드 58-7 컬렉션이나 배열의 중첩 반복을 위한 권장 관용구
        List<Card> deck3 = new ArrayList<>();
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                deck3.add(new Card(suit, rank));
            }
        }

    }
}
