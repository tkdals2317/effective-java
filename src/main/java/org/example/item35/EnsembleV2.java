package org.example.item35;


public enum EnsembleV2 {
    SOLO(1), DUET(2), TRIO(3), QUARTET(4),
    QUINTET(5), SEXTET(6), SEPTET(7), OCTET(8),
    DOUBLE_QUARTET(8), NONET(9), DECTET(10),
    TRIPLE_QUARTET(12);

    private final int numberOfMusicians;

    EnsembleV2(int numberOfMusicians) {
        this.numberOfMusicians = numberOfMusicians;
    }

    public int numberOfMusicians() {
        return numberOfMusicians;
    }


    public static void main(String[] args) {
        for (EnsembleV2 ensemble : EnsembleV2.values()) {
            System.out.println(ensemble.numberOfMusicians());
        }
    }
}
