package org.example.item79;

@FunctionalInterface
public interface SetObserverV3<E> {
    // ObservableSet 원소가 더해지면 호출된다.
    void added(ObservableSetV3<E> set, E element);
}
