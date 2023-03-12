package org.example.item36;

import java.util.EnumSet;
import java.util.Set;

// 코드 36-2 EnumSet - 비트 필드를 대체하는 현대적 기법
public class TextV2 {
    public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }

    // 어떤 Set을 넘겨도 되나, EnumSet이 가장 좋다
    public void applyStyles(Set<Style> styles) {}

    public static void main(String[] args) {
        TextV2 textV2 = new TextV2();

        // 모든 요소를 포함한 EnumSet
        EnumSet<Style> allOfStyle = EnumSet.allOf(Style.class); // BOLD, ITALIC, UNDERLINE, STRIKETHROUGH

        // Style의 빈 컬렉션 생성
        EnumSet<Style> noneOfStyle = EnumSet.noneOf(Style.class); // 빈 컬렉션

        // 매개변수로 전달된 요소를 제외한 Set 생성
        EnumSet<Style> complementOfSet = EnumSet.complementOf(EnumSet.of(Style.BOLD, Style.ITALIC)); // UNDERLINE, STRIKETHROUGH


        textV2.applyStyles(EnumSet.of(Style.BOLD, Style.UNDERLINE));
    }
}
