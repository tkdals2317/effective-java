package org.example.item79;

import org.example.item18.ForwardingSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

// 코드 79-1 잘못된 코드. 동기화 블록 안에서 외계인 메서드를 호출한다.
public class ObservableSetV3<E> extends ForwardingSet<E> {

    public ObservableSetV3(Set<E> set) {
        super(set);
    }

    private final List<SetObserverV3<E>> observers = new CopyOnWriteArrayList<>();

    public void addObserver(SetObserverV3<E> observer) {
            observers.add(observer);
    }

    public boolean removeObserver(SetObserverV3<E> observer) {
            return observers.remove(observer);
    }


    // 코드 79-3 외계인 메서드를 동기화 바깥으로 옮겼다.
    private void notifyElementAdded(E element) {
        for (SetObserverV3<E> observer : observers) {
            observer.added(this, element);
        }
    }
    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added) {
            notifyElementAdded(element);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for(E element : c) {
            result |= add(element); // notifyElementAdded를 호출한다.
        }
        return result;
    }
}
