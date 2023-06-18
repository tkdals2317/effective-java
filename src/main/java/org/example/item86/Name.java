package org.example.item86;

import java.io.Serializable;

// 코드 87-1 기본 직렬화 형태에 적합한 후보
// 객체의 물리적 표현과 논리적 표현이 같다면 기본 직렬화 형태라도 무방하다
public class Name implements Serializable {

    /**
     * 성. null이 아니어야 함
     * @serial
     */
    private final String lastName;

    /**
     * 이름. null이 아니어야 함
     * @serial
     */
    private final String firstName;

    /**
     * 중간 이름. 중간 이름이 없다면 null
     * @serial
     */
    private final String middleName;


    public Name(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }
}
