package org.example.item28;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chooser<T> {
    private final T[] choiceArray;


    public Chooser(Collection<T> choiceArray) {
        this.choiceArray = (T[]) choiceArray.toArray();
    }

    //반환된 Object 타입을 형변환 하여 사용해야 한다.
    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
