package org.example.item19;

import org.junit.jupiter.api.Test;

public class 상속을_고려해_설계하고_문서화하라_그러지_않았다면_상속을_금지하라 {

    @Test
    public void 생성자가_재정의_가능_메서드_호출하는_예제() {
        Sub sub = new Sub();
        sub.overrideMe();
    }
}
