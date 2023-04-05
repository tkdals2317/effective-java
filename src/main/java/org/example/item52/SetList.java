package org.example.item52;

import java.util.ArrayList;
import java.util.TreeSet;

public class SetList {
    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i ++) {
            set.add(i);
            list.add(i);
        }

        for (int i = 0; i < 3; i ++) {
            set.remove(i);
            list.remove((Integer) i);
        }

        System.out.println(set);
        System.out.println(list);
    }
}
