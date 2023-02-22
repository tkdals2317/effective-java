package org.example.item27;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class item27 {

    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        Set<Lark> exaltation = new HashSet();

    }

    private static class RefactoringArrayList extends ArrayList<Lark> {
        private int size;
        transient Object[] elementData;

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            if (a.length < size) {
                // Make a new array of a's runtime type, but my contents:
                @SuppressWarnings("unchecked") T[] result = (T[]) Arrays.copyOf(elementData, size, a.getClass());
                return result;
            }
            System.arraycopy(elementData, 0, a, 0, size);
            if (a.length > size)
                a[size] = null;
            return a;
        }
    }

}
