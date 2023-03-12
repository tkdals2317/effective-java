package org.example.item35;

// 코드 35-1 ordinal을 잘못 사용한 예 - 따라하지 말 것
public enum Ensemble {
    SOLO, DUET, TRIO, QUARTET, QUINTET,
    SEXTET, SEPTET, OCTET, NONET, DECTET;

    public int numberOfMusicians() {
        return ordinal() + 1;
    }

    public static void main(String[] args) {
        for (Ensemble ensemble : Ensemble.values()) {
            System.out.println(ensemble.numberOfMusicians());
        }
    }
}
