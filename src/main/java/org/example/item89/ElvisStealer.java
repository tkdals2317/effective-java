package org.example.item89;

import java.io.Serializable;

// 코드 89-2 도둑 클래스
public class ElvisStealer implements Serializable {

    static Elvis impersonator;
    private Elvis payload;

    private Object readResolve() {
        // resolve 되기 전의 Elvis 인스턴스의 참조를 저장한다.
        impersonator = payload;

        // favoriteSongs 필드에 맞는 객체를 반환한다.
        return new String[] { "A Fool Such as I"};
    }

    private static final long serialVersionUID = 0L;

}
