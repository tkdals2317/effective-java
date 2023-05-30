package org.example.item79;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class item79 {


    public static void main(String[] args) {
        //observableSetV1Method1();
        //observableSetV1Method2();
        //observableSetV1Method3();

        //observableSetV2Method1();
        observableSetV3Method1();
    }

    private static void observableSetV2Method1() {
        ObservableSetV2<Integer> setV2 = new ObservableSetV2<>(new HashSet<>());
        setV2.addObserver(new SetObserverV2<Integer>() {
            @Override
            public void added(ObservableSetV2<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(()-> set.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            setV2.add(i);
        }
        System.out.println(setV2);
    }
    private static void observableSetV3Method1() {
        ObservableSetV3<Integer> setV3 = new ObservableSetV3<>(new HashSet<>());
        setV3.addObserver(new SetObserverV3<Integer>() {
            @Override
            public void added(ObservableSetV3<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(()-> set.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            setV3.add(i);
        }
        System.out.println(setV3);
    }

    private static void observableSetV1Method3() {
        ObservableSetV1<Integer> setV1 = new ObservableSetV1<>(new HashSet<>());
        // 코드 79-2 쓸데없이 백그라운드 스레드를 사용하는 관찰자
        setV1.addObserver(new SetObserverV1<Integer>() {
            @Override
            public void added(ObservableSetV1<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(()-> set.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });


        for (int i = 0; i < 100; i++) {
            setV1.add(i);
        }
        // 23번째에서 교착상태에 빠진다
    }

    private static void observableSetV1Method2() {
        ObservableSetV1<Integer> setV1 = new ObservableSetV1<>(new HashSet<>());
        setV1.addObserver(new SetObserverV1<>() {
            @Override
            public void added(ObservableSetV1<Integer> set, Integer element) {
                System.out.println(element);
                if (element == 23) {
                    set.removeObserver(this);
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            setV1.add(i);
        }
        // 23번째에서 java.util.ConcurrentModificationException 발생
    }

    private static void observableSetV1Method1() {
        ObservableSetV1<Integer> setV1 = new ObservableSetV1<>(new HashSet<>());

        setV1.addObserver((s, e) -> System.out.println(e));

        for (int i = 0; i < 100; i++) {
            setV1.add(i);
        }
    }
}
