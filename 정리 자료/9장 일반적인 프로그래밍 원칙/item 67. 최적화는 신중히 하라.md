# item 67. 최적화는 신중히 하라

<aside>
💡 최적화를 섣불리 하는 것에 대한 위험성과 최적화 시 주의해야 할 점을 알아보자

</aside>

### 최적화에 관련된 격언

> (맹목적인 어리석음 을 포함해) 그 어떤 핑계보다 효율성이라는 이름 아래 행해진 컴퓨팅 죄악이 더 많다.(심지어 효율을 높이지도 못하면서) - 윌리엄 울프
> 

> (전체의 97% 정도인) 자그마한 효율성은 모두 잊자. 섣부른 최적화가 만악의 근원이다 - 도널드 크누스
> 

> 최적화를 할 때는 다음 두 규칙을 따르라.
첫 번째. 하지 마라.
두 번째. (전문가 한정) 아직 하지 마라. 다시 말해 완전히 명백하고 최적화되지 않은 해법을 찾을 때 까지는 하지 마라. - M.A 잭슨
> 

위 격언들은 최적화가 좋은 결과보다는 오히려 해로운 결과로 이어지기 쉽다는 의미를 내포하고 있다.

본인도 성능개선을 목적으로 캐싱을 무분별하게 남발하였다가 엄청난 에러로 돌아오게 되었다.

### **빠른 프로그램보다는 좋은 프로그램으로 작성하라.**

성능 때문에 견고한 구조를 희생하지 말자

좋은 프로그램은 정보 은닉 원칙을 따르므로 개별 구성요소의 내부를 독립적으로 설계할 수 있다.

따라서 시스템의 나머지에 영향을 주지 않고도 각 요소를 다시 설계할 수 있다.

구현상의 문제는 나중에 최적화해 해결 할 수 있지만, 아키텍처의 결함이 성능을 제한하는 상황이라면 시스템 전체를 다시 작성하지 않고는 해결하기 불가능 할 수 있다.

완성된 설계의 기본 틀을 변경하려다 보면 유지보수하거나 개선하기 어려운 꼬인 구조의 시스템이 만들어지기 쉽기 때문이다.

### 성능을 제한하는 설계를 피하라

완성 후 변경하기가 가장 어려운 설계요소는 바로 컴포넌트끼리, 혹은 외부 시스템과의 소통 방식이다.

API, 네트워크 프로토콜, 영구 저장용 데이터 포맷이 대표적이다.

최근에 NCP 메일 성능 문제로 장애가 난 적이 있었는데 같은 케이스인 것 같다.

NCP 메일 API에서 단건으로 밖에 조회가 안되는 데이터가 있어 메일이 몇천 건 이상 넘어가면 API 통신을 몇천번이나 반복해야하는 성능적으로 치명적인 문제점이 생긴 적이 있다.

이런 설계 요소들은 완성 후에는 변경하기 어렵거나 불가능할 수 있으며, 동시에 시스템 성능을 심각하게 제한할 수 있다.

### API를 설계할 때 성능에 주는 영향을 고려하라.

public 타입을 가변으로 만들면, 즉 내부 데이터를 변경할 수 있게 만들면 불필요한 방어적 복사를 수없이 유발할 수 있다(item 50).

특정 구현 타입을 사용하기보단 인터페이스를 사용하자

이는 특정 구현체에 종속되게 하여, 나중에 더 빠른 구현체가 나오더라도 이용하지 못하게 된다.

### 성능을 위해 API를 왜곡하는 건 매우 안좋은 생각이다.

잘 설계된 API는 성능도 좋은게 보통이다.

API를 왜곡하도록 만든 그 성능 문제는 해당 플랫폼이나 아랫단 소프테웨어의 다음 버전에서 사라질 수도 있지만, 왜곡된 API와 이를 지원하는데 따르는 고통은 영원히 계속될 것 이다.

### 각각의 최적화 시도 전후로 성능을 측정하라

성능이 눈에 띄게 높아지지 않는 경우도 있고 오히려 안좋아지는 경우도 있다.

주요 원인은 프로그래메서 시간을 잡아먹는 부분을 추측하기 어렵기 때문이다.

그럴 때는 프로파일링 도구를 이용하여 최적화 노력을 어디에 집중해야할 지 도움 받아 보자.

나도 안써봤지만 성능 개선을 할 때 한번 활용해봐야겠다. 과거에 메모리 이슈로 인해 VisualVM을 사용한 적 이있는데 그때는 메모리 사용량만 확인하였는데 메서드간 소요시간도 체크할 수 있다고 한다.

관련해서는 따로 공부해서 정리해봐야겠다!

책에서는 JMH를 추천하고 있다.

### 정리

빠른 프로그램 작성하기보단 좋은 프로그램을 작성하다 보면 성능은 따라 오게 되어있다.

API, 네트워크 프로토콜, 영구 저장용 데이터 포맷을 설계할 때는 성능을 생각하여 설계하자.

성능을 항상 측정해보고, 프로파일러를 사용해 문제가 되는 지점을 찾아 최적화를 수행하자.