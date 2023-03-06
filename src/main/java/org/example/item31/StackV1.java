package org.example.item31;

import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;

public class StackV1<E> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;


    public StackV1() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        @SuppressWarnings("unchecked") E result = (E) elements[--size];
        elements[size] = null;
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public void pushAll(Iterable<E> src) {
        for (E e : src) {
            push(e);
        }
    }

    public void popAll(Collection<E> dst){
        while (!isEmpty())
            dst.add(pop());
    }
}
