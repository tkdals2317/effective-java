package org.example.item64;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class item64 {

    // 좋은 예. 인터페이스를 타입으로 사용했다.
    Set<String> stringSet = new LinkedHashSet<>();

    // 나쁜 예. 클래스를 타입으로 사용했다.
    LinkedHashSet<String> stringSet2 = new LinkedHashSet<>();

    // 나중에 구현 클래스를 교체하고자 한다면 그저 새 클래스의 생성자 또는 다른 정적 팩터리를 호출 해주기만 하면 된다.
    Set<String> stringSet3 = new HashSet<>();
}
