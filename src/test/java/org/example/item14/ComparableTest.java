package org.example.item14;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ComparableTest {
    @Test
    public void comparingTest() {
        List<PhoneNumber> phoneNumberList = new ArrayList<>();


        phoneNumberList.add(new PhoneNumber( 82, 3170,  2317, new Person("다상민", 31)));
        phoneNumberList.add(new PhoneNumber( 42, 3170,  2317, new Person("라상민", 31)));
        phoneNumberList.add(new PhoneNumber( 32, 3170,  2317, new Person("김상민", 25)));
        phoneNumberList.add(new PhoneNumber( 12, 3170,  2317, new Person("이상민", 28)));
        phoneNumberList.add(new PhoneNumber( 52, 3170,  2317, new Person("니상민", 27)));
        phoneNumberList.add(new PhoneNumber( 62, 3170,  2317, new Person("이상민", 28)));

        System.out.println("============정렬 전============");
        for (PhoneNumber phoneNumber : phoneNumberList) {
            System.out.println(phoneNumber);
        }
        System.out.println("============compareTo 정렬============");
        phoneNumberList.sort(PhoneNumber::compareTo);
        for (PhoneNumber phoneNumber : phoneNumberList) {
            System.out.println(phoneNumber);
        }


        System.out.println("============객체 참조 Person 이름 정렬 후============");
        phoneNumberList.sort(Comparator.comparing(phoneNumber -> phoneNumber.getPerson().getAge()));
        for (PhoneNumber phoneNumber : phoneNumberList) {
            System.out.println(phoneNumber);
        }

        System.out.println("============객체 참조 Person 나이 정렬 후 이름 정렬============");
        Function<PhoneNumber, Integer> firstSort = phoneNumber -> phoneNumber.getPerson().getAge();
        Function<PhoneNumber, String> secondSort = phoneNumber -> phoneNumber.getPerson().getName();

        phoneNumberList.sort(Comparator.comparing(firstSort).thenComparing(secondSort));
        for (PhoneNumber phoneNumber : phoneNumberList) {
            System.out.println(phoneNumber);
        }
    }
}