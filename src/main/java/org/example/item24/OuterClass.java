package org.example.item24;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OuterClass {
    private int x = 10;

    public static class StaticMemberInnerClass {
        void x() {
            OuterClass outerClass = new OuterClass();
            outerClass.x = 100;
        }
    }

    public class NonStaticMemberInnerClass {
        void x() {
            OuterClass.this.x = 100;
        }
    }
}
