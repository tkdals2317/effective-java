package org.example.item71;

public class item71 {

    public static void main(String[] args) {
        try {
            methodThrowCheckedException();
        } catch (TheCheckedException e) {
            throw new AssertionError(e);
        }

        try {
            methodThrowCheckedException();
        } catch (TheCheckedException e) {
            e.printStackTrace(); // 이런, 우리가 졌다.
            System.exit(1);
        }

        Test obj = new Test();
        Test2 obj2 = new Test2();

        // 코드 71-1 검사예외를 던지는 메서드 - 리팩터링 전
        try {
            obj.action();
        } catch (TheCheckedException e) {
            // 예외 상황을 대처한다.
            throw new RuntimeException(e);
        }


        // 코드 71-2 상태 검사 메서드와 비검사 예외를 던지는 메서드 - 리팩터링 후
        if (obj2.actionPermitted()) {
            obj2.action();
        } else {
            // 예외 상황을 대처한다.
        }
    }

    private static void methodThrowCheckedException() throws TheCheckedException {

    }

    public static class Test {

        public void action() throws TheCheckedException {

        }
    }

    public static class Test2 {

        public void action() throws RuntimeException {

        }

        public boolean actionPermitted() {
            return true;
        }
    }
}
