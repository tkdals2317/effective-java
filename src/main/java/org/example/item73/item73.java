package org.example.item73;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

public class item73 {

    public static void main(String[] args) {
        // 코드 73-1 예외 번역
        try {
            //...
        } catch (LowerLevelException e) {
            throw new HigherLevelException();
        }


        AbstractSequentialList<String> objects = new AbstractSequentialList<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }
        };

        // 코드 73-2 예외 연쇄
        try {

        } catch (LowerLevelException cause) {
            throw new HigherLevelException(cause);
        }
    }

}
