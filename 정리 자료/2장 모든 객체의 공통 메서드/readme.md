## 2장 모든 객체의 공통 메서드


Object는 객체를 만들 수 있는 구체 클래스지만 기본적으로 상속해서 사용하도록 설계되었다.

Object의 메서드인 equals, hashCode, toString, clone, finalize 모두 재정의를 염두에 두고 설계된 것이라 재정의시 지켜야하는 **일반 규약이 명확히 정의**되어 있다.

이런 규약을 지키지 않고 재정의하면, 이 규약을 준수한다고 가정한 클래스(HashMap, HashSet)를 오동작하게 만들 수 있다.

**이번 장에서는 final이 아닌 Object 메서드를 언제 어떻게 재정의해야 하는지를 다룬다.**
