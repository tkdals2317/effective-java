package org.example.item31;

import java.util.List;

public class item31 {

    public static void main(String[] args) {
        StackV1<Number> numberStackV1 = new StackV1<>();
        List<Number> numVal = List.of(1, 2, 3.4, 4L);
        List<Integer> intVal = List.of(5, 6);

        numberStackV1.pushAll(numVal);
        //java: incompatible types: java.util.List<java.lang.Integer> cannot be converted to java.lang.Iterable<java.lang.Number>
        //numberStackV1.pushAll(intVal);
    }

}
