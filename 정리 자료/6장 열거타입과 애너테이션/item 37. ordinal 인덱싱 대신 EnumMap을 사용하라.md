# item 37. ordinal 인덱싱 대신 EnumMap을 사용하라

<aside>
💡 EnumMap에 대해 알아보고 EnumMap의 활용법을 알아보자!

</aside>

### ordinal() 메서드로 배열 인덱스로 사용하는 예제

```java
public class Plant {

    enum LifeCycle {ANNUAL, PERENNIAL, BIENNIAL}

    final String name;
    final LifeCycle lifeCycle;

    public Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }
    
		// 코드 37-1 ordinal()을 배열 인덱스로 사용 - 따라하지 말것!
    public static void main(String[] args) {
        // 정원에 심은 식물을 배열 하나로 관리
        Plant[] garden = { new Plant("장미", LifeCycle.PERENNIAL), new Plant("소나무", LifeCycle.ANNUAL), new Plant("나팔꽃", LifeCycle.BIENNIAL)};

        // 생애주기별로 총 3개의 집합을 만든다.
        Set<Plant>[] plantsByLifeCycle = new Set[LifeCycle.values().length];
        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            plantsByLifeCycle[i] = new HashSet<>();
        }
        // 정원을 한바퀴 돌며 식물을 해당 해당 집합에 넣는다.
        for (Plant plant : garden) {
            plantsByLifeCycle[plant.lifeCycle.ordinal()].add(plant);
        }
        
        // 결과 출력
        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            System.out.printf("%s : %s%n", Plant.LifeCycle.values()[i], plantsByLifeCycle[i]);
        }

    }
}

==== output ====
ANNUAL : [소나무]
PERENNIAL : [장미]
BIENNIAL : [나팔꽃]
```

정원에 심은 식물을 배열 하나로 관리하고, 이들을 생애주기별로 묶는다.

생애주기별로 총 3개의 집합을 만들고 정원을 한바퀴 돌며 식물을 해당 해당 집합에 넣는다.

집합들을 배열 하나에 넣고 생애주기의 oridnal 값을 배열의 인덱스로 사용한 코드이다.

**위 코드의 문제점**

- 배열은 제네릭과 호환되지 않으므로 비검사 형변환을 수행해야하고 깔끔히 컴파일 되지 않는다.
- 배열은 각 인덱스의 의미를 모르니 출력 결과에 직접 레이블을 달아야 한다.
- 정확한 정숫값을 사용한다는 것을 직접 보증해야한다. ⇒ 정수는 열거타입과 달리 타입 안전하지 않기 때문이다.

### EnumMap을 사용해보자

배열은 실질적으로 열거 타입의 상수를 값으로 매핑하는 일을 함으로 이걸 Map으로 바꿀 수 있다. 

열거타입을 키 값으로 사용하도록 설계한 Map 구현체가 바로 EnumMap이다.

```java
// 결과 출력
for (int i = 0; i < plantsByLifeCycle.length; i++) {
    System.out.printf("%s : %s%n", Plant.LifeCycle.values()[i], plantsByLifeCycle[i]);
}

EnumMap<LifeCycle, Set<Plant>> plantsByLifeCycleV2 = new EnumMap<>(LifeCycle.class);
for (Plant.LifeCycle lc : Plant.LifeCycle.values()) {
    plantsByLifeCycleV2.put(lc, new HashSet<>());
}

for (Plant plant : garden) {
    plantsByLifeCycleV2.get(plant.lifeCycle).add(plant);
}

System.out.println(plantsByLifeCycleV2);

==== output ====
{ANNUAL=[소나무], PERENNIAL=[장미], BIENNIAL=[나팔꽃]}
```

**위 코드의 장점**

- 더 짧고 명료하고 안전하고 성능도 비슷하다.
- 안전하지 않은 형변환은 쓰지 않아도 된다.
- 맵의 키인 열거타입이 그 자체로 출력용 문자열을 제공하니 출력결과에 레이블을 달지 않아도 된다.
- 배열의 인덱스를 계산하는 과정에서 오류가 날 가능성도 없다

**EnumMap의 성능이 ordinal을 쓴 배열에 비견되는 이유**

- 그 내부에서 배열을 사용함 ⇒ 내부 구현 방식을 안으로 숨겨서 Map의 타입 안전성과 배열의 성능을 모두 얻어냄

EnuMap의 생성자가 받는 키 타입의 Class 객체는 한정적 타입 토큰으로 런타임 제네릭 타입 정보를 제공한다.

### 스트림을 활용해보자

스트림을 사용하면 위 코드를 더 간결하게 줄일 수 있다.

아래의 일반 Map을 사용하는 코드와 EnumMap을 사용했을 때의 코드를 보자

```java
// 코드 37-3 스트림을 사용한 코드 1 - EnumMap을 사용하지 않는다.
System.out.println(Arrays.stream(garden).
        collect(groupingBy(p -> p.lifeCycle)));

// 코드 37-4 스트림을 사용한 코드 2 - EnumMap을 이용해 데이터와 열거 타입을 매핑했다.
System.out.println(Arrays.stream(garden)
        .collect(groupingBy(p -> p.lifeCycle, () -> new EnumMap<>(LifeCycle.class), toSet())));
```

37-3 코드의 경우 일반 Map을 사용하여 EnumMap을 써서 얻을 수 있는 공간과 성능 이점이 사라지게 된다.

하지만 원하는 맵 구현체를 받는 매개변수 3개짜리 Collectors.groupingBy 메서드로 EnumMap으로 만들 수 있다.

스트림을 사용할 경우 EnumMap만 사용했을 때와 다르게 동작한다.

EnumMap 버전의 경우 언제나 식물의 생애주기다 하나씩의 중첩 맵을 만들지만, 스트림 버전에서는 해당 생애 주기에 속하는 식물이 있을 때만 만든다.

```java
Plant[] garden = { new Plant("장미", LifeCycle.PERENNIAL), new Plant("나팔꽃", LifeCycle.BIENNIAL)};

Set<Plant>[] plantsByLifeCycle = new Set[LifeCycle.values().length];
//EnumMap 버전의 경우 언제나 식물의 생애주기다 하나씩의 중첩 맵을 만든다.
for (int i = 0; i < plantsByLifeCycle.length; i++) {
    plantsByLifeCycle[i] = new HashSet<>();
}
...

==== output ==== 
ANNUAL : []
PERENNIAL : [장미]
BIENNIAL : [나팔꽃]

// 스트림 버전에서는 해당 생애 주기에 속하는 식물이 있을 때만 만든다.
Arrays.stream(garden)
        .collect(groupingBy(p -> p.lifeCycle, () -> new EnumMap<>(LifeCycle.class), toSet()));

==== output ====
{PERENNIAL=[장미], BIENNIAL=[나팔꽃]}
```

### 다차원 관계에서의 EnumMap 활용

![https://namu.wiki/w/상전이](https://user-images.githubusercontent.com/49682056/227700300-84861a0b-50dc-474b-9c41-a954df6ad925.png)

```java
// 코드 37-5 배열들의 배열의 인덱스에 ordinal()을 사용 - 따라하지 말 것!
public enum Phase {
    SOLID, LIQUID, GAS;
    public enum Transition {
        MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;

        // 행은 from의 ordinal을, 열은 to의 ordinal을 인덱스로 쓴다.
        private static final Transition[][] Transitions = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };

        // 한 상태에서 다른 상태로의 전이를 반환한다.
        public static Transition from(Phase src, Phase dst) {
            return Transitions[src.ordinal()][dst.ordinal()];
        }
    }

    public static void main(String[] args) {
        System.out.println(Transition.from(Phase.SOLID, Phase.LIQUID)); // MELT
        System.out.println(Transition.from(Phase.SOLID, Phase.GAS)); // SUBLIME
        System.out.println(Transition.from(Phase.GAS, Phase.LIQUID)); // CONDENSE
    }
}
```

배열들의 배열의 인덱스에 ordinal()을 사용한 코드를 EnumMap으로 바꿔 보자

```java
// 코드 37-6 중첩 EnumMap으로 데이터와 열거 타입 쌍을 연결했다
public enum PhaseV2 {
    SOLID, LIQUID, GAS;

    public enum TransitionV2 {
        MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);

        private final PhaseV2 from;
        private final PhaseV2 to;

        TransitionV2(PhaseV2 from, PhaseV2 to) {
            this.from = from;
            this.to = to;
        }

        // 상전이 맵을 초기화한다.
        private static final Map<PhaseV2, Map<PhaseV2, TransitionV2>> m = Stream.of(values())
                .collect(groupingBy(transition -> transition.from,
                        () -> new EnumMap<>(PhaseV2.class),
                        toMap(t -> t.to, t -> t, (x, y) -> y, () -> new EnumMap<>(PhaseV2.class))));

        public static TransitionV2 from(PhaseV2 from, PhaseV2 to) {
            return m.get(from).get(to);
        }
    }
}
```

상전이 맵을 초기화하는 코드가 복잡하다. for문 버전으로 보면 이해가 조금 쉽다.

```java
// 상 전이 맵 초기화 stream 버전
private static final Map<PhaseV2, Map<PhaseV2, TransitionV2>> m = Stream.of(values())
                .collect(groupingBy(transition -> transition.from, // 전이 이전 상태를 기준으로 묶는다.
                        () -> new EnumMap<>(PhaseV2.class), 
                        toMap(t -> t.to, t -> t, (x, y) -> y, () -> new EnumMap<>(PhaseV2.class)))); // 이후 상태를 전이에 대응시키는 EnumMap을 생성한다. 여기서 (x, y) -> y는 선언만 하고 실제로 쓰이지는 않는데, 이는 단지 EnumMap을 얻으려면 맵 팩터리가 필요하고 수집기들은 점층적 팩터리를 제공하기 때문이다.

// 상 전이 맵 초기화 for문 버전
private static final Map<PhaseV2, Map<PhaseV2, TransitionV2>> m2 =
        new EnumMap<PhaseV2, Map<PhaseV2, TransitionV2>>(PhaseV2.class);

static{
    for(PhaseV2 p : PhaseV2.values())
        m2.put(p, new EnumMap<PhaseV2, TransitionV2>(PhaseV2.class));

    for(TransitionV2 trans : TransitionV2.values())
        m2.get(trans.from).put(trans.to, trans);
}
```

여기서 새로운 상태인 플라즈마(PLASMA)를 추가해보자. 이 상태와 연결된 전이는 2개이다. 

기체에서 플라즈마로 변하는 이온화(IONIZE)이고, 플라즈마에서 기체로 변하는 탈이온화(DEIONIZE)다.

배열로 만든 37-5 코드의 경우 새로운 상수를 Phase에 1개, Transition에 2개를 추가하고, 원소 9개짜리 배열들의 배열을 원소 16개짜리로 교체 해야 한다.

```java
private static final Transition[][] Transitions = {
        {null, MELT, SUBLIME, null},
        {FREEZE, null, BOIL, null},
        {DEPOSIT, CONDENSE, null, IONIZE},
        {null, null, DEIONIZE, null}
};
```

하지만 EnumMap 버전에서는 상태 목록에 PLASMA만 추가하고, 전이 목록에 IONIZE(GAS, PLASMA)와 DEIONIZE(PLASMA, GAS)만 추가하면 된다.

```java
public enum PhaseV2 {
    SOLID, LIQUID, GAS, PLASMA;

    public enum TransitionV2 {
        MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID),
        IONIZE(GAS, PLASMA), DEIONIZE(PLASMA, GAS);
				... // 나머지 코드는 그대로다.
	}
```

### 정리

**배열의 인덱스를 얻기 위해 ordinal을 사용하는 것은 좋지 않으니 대신 EnumMap을 사용하라**

다차원 관계에서는 EnumMap<…, EnumMap<…>>으로 표현하자!