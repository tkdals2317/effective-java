package org.example.item87;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class item87 {
    private static final long serialVersionUID = 15615361231651561L;

    // 코드 87-4 기본 직렬화를 사용하는 동기화된 클래스를 위한 writeObject 메서드
    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }
}
