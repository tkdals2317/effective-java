package org.example.item26;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class GenericStamps {

    private final Collection<Stamp> stamps = new ArrayList<>() {};

    public void add() {
        // stamps.add(new Coin()); // 컴파일 오류가 뜬다
    }

    public void cancelAll(){
        for (Stamp stamp : stamps) {
            stamp.cancel();
        }
    }
}
