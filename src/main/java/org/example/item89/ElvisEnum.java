package org.example.item89;

import java.util.Arrays;

// 코드 89-4 열거 타입 싱글턴 - 전통적인 싱글턴보다 우수하다
public enum ElvisEnum {
    INSTANCE;

    private String[] favoriteSongs = { "Hound Dog", "Heartbreak Hotel"};

    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }

}
