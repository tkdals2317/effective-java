package org.example.item38;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class item38 {
    public static void main(String[] args) {
        double x = 4.0000;
        double y = 2.0000;
        testV1(ExtendedOperation.class, x, y);
        System.out.println("===============================");

        testV2(Arrays.asList(ExtendedOperation.values()), x, y);
        System.out.println("===============================");

        List<Operation> extendedOperations = Arrays.asList(ExtendedOperation.values());
        List<Operation> basicOperations = Arrays.asList(BasicOperation.values());
        List<Operation> allOperation = new ArrayList<>();
        allOperation.addAll(extendedOperations);
        allOperation.addAll(basicOperations);
        testV2(allOperation, x, y);

    }
    private static <T extends Enum<T> & Operation> void testV1(Class<T> opEnumType, double x, double y) {
        for (Operation op : opEnumType.getEnumConstants()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

    private static void testV2(Collection<? extends Operation> opSet, double x, double y) {
        for (Operation op : opSet) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }

}
