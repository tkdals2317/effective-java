package org.example.item53;

public class item53 {

    // 코드 53-1 간단한 가변인수 활용 예
    static int sum(int... args) {
        int sum = 0;
        for (int arg : args) {
            sum += arg;
        }
        return sum;
    }

    // 코드 53-2 인수가 1개 이상이어야 하는 가변인수 메서드 - 잘못 구현한 예
    static int min(int... args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("인수가 1개 이상 필요합니다.");
        }
        int min = args[0];
        for (int i = 1; i < args.length; i++) {
            if (args[i] < min) {
                min = args[i];
            }
        }
        return min;
    }

    // 코드 53-3 인수가 1개 이상이어야 할 때 가변인수를 제대로 사용하는 방법
    static int min(int firstArgs, int... remainArgs) {
        int min = firstArgs;
        for (int arg : remainArgs) {
            if (arg < min) {
                min = arg;
            }
        }
        return min;
    }

    // 성능을 생각한다면 인수 4개 이상 부터만 가변인수가 맡게 하자
    public void foo()  {}
    public void foo(int a1)  {}
    public void foo(int a1, int a2)  {}
    public void foo(int a1, int a2, int a3)  {}
    public void foo(int a1, int a2, int a3, int... rest)  {}


}
