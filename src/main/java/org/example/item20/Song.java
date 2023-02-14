package org.example.item20;

import org.apache.commons.collections4.collection.SynchronizedCollection;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class Song {
    // 골격 구현 클래스 AbstractList
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);
        return new AbstractList<>() {
            @Override
            public Integer get(int i) {
                return a[i];
            }

            @Override
            public Integer set(int i, Integer val) {
                int oldVal = a[i];
                a[i] = val;
                return oldVal;
            }

            @Override
            public int size() {
                return a.length;
            }
        };
    }
}
