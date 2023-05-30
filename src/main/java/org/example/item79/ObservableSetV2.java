package org.example.item79;

import org.example.item18.ForwardingSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

// 코드 79-1 잘못된 코드. 동기화 블록 안에서 외계인 메서드를 호출한다.
public class ObservableSetV2<E> extends ForwardingSet<E> {

    public ObservableSetV2(Set<E> set) {
        super(set);
    }

    private final List<SetObserverV2<E>> observers = new ArrayList<>();

    public void addObserver(SetObserverV2<E> observer) {
        synchronized (observers){
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserverV2<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }


    // 코드 79-3 외계인 메서드를 동기화 바깥으로 옮겼다.
    private void notifyElementAdded(E element) {
        List<SetObserverV2<E>> snapshot = null;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (SetObserverV2<E> observer : snapshot) {
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
