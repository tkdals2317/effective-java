package org.example.item14;

import lombok.*;

import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhoneNumber implements Comparable<PhoneNumber> {
//    private static final Comparator<PhoneNumber> COMPARATOR = comparingInt((PhoneNumber phoneNumber) -> phoneNumber.areaCode)
//            .thenComparingInt(phoneNumber -> phoneNumber.prefix)
//            .thenComparingInt(phoneNumber -> phoneNumber.lineNum);
    private static final Comparator<PhoneNumber> PERSON_COMPARATOR =
            Comparator.comparing((PhoneNumber phoneNumber) -> phoneNumber.person.getName())
            .thenComparing((PhoneNumber phoneNumber) -> phoneNumber.person.getAge());

    private int areaCode;
    private int prefix;
    private int lineNum;

    private Person person;

    @Override
    public int compareTo(PhoneNumber phoneNumber) {
        //return COMPARATOR.compare(this, phoneNumber);
        return PERSON_COMPARATOR.compare(this, phoneNumber);
    }
}