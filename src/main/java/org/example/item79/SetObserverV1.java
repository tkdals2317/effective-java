package org.example.item79;

@FunctionalInterface
public interface SetObserverV1<E> {
    // ObservableSet 원소가 더해지면 호출된다.
    void added(ObservableSetV1<E> set, E element);
}
