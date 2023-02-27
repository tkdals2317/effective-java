package org.example.item28;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chooser {
    private final Object[] choiceArray;


    public Chooser(Collection choiceArray) {
        this.choiceArray = choiceArray.toArray();
    }

    //반환된 Object 타입을 형변환 하여 사용해야 한다.
    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }
}
