package org.example.item26;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RawTypeStamps {

    private final Collection stamps = new ArrayList();

    public void add() {
        stamps.add(new Coin());
    }

    public void cancelAll(){
        for (Iterator i = stamps.iterator(); i.hasNext();) {
            // Coin 인스턴스가 들어가있는 경우 ClassCastException 발생
            Stamp stamp = (Stamp) i.next();
            stamp.cancel();
        }
    }

}
