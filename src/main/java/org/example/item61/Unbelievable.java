package org.example.item61;

// 코드 61-3 기이하게 동작하는 프로그램 - 결과를 맞춰보자
public class Unbelievable {
    static Integer i;

    public static void main(String[] args) {
        if (i == 42) { // NPE 발생!!
            System.out.println("믿을 수 없군!");
        }
    }
}
