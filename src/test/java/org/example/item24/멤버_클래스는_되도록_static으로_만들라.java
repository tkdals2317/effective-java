package org.example.item24;

import org.example.item24.Calculator;
import org.example.item24.OuterClass;
import org.junit.jupiter.api.Test;

import java.util.*;

public class 멤버_클래스는_되도록_static으로_만들라 {

    @Test
    public void staticMemberTest() throws Exception {
        Calculator.Operation plus = Calculator.Operation.PLUS;
        Calculator.Operation minus = Calculator.Operation.MINUS;

    }

    @Test
    void nonStaticMemberTest() {
        OuterClass.StaticMemberInnerClass staticMemberInnerClass = new OuterClass.StaticMemberInnerClass();
        OuterClass.NonStaticMemberInnerClass nonStaticMemberInnerClass = new OuterClass().new NonStaticMemberInnerClass();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        ArrayList<Object> objects = new ArrayList<>();
    }



}
