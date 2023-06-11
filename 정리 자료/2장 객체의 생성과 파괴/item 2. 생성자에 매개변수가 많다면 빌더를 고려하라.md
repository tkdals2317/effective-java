# item 2. 생성자에 매개변수가 많다면 빌더를 고려하라

<aside>
💡 정적 팩터리 메서드와 생성자는 선택적 매개변수가 많을 경우 적절히 대응하기 힘들다는 단점이 존재한다.

이런 단점을 극복할 수 있는 **빌더**에 대해 알아보자

</aside>

### 점층적 생성자 패턴

선택적으로 받는 파라미터가 많아지면 많아질 수록 해당 파라미터들을 받는 생성자를 다 만들어주는 방법을 말한다.

위와 같은 방법을 점층적 생성자 패턴이라고 하며 이 경우는 사용자에게 원치않은 매개변수를 지정해줘야 한다거나 매개변수의 조합에 따라 생성자 수가 너무 많아지며 사용자의 실수가 발생하기 쉽다.

```java
public class NutritionFacts {	
	private final int servingSize;  // 필수
	private final int servings;     // 필수
	private final int calories;     // 선택
	private final int fat;          // 선택
	private final int sodium;       // 선택
	private final int carbohydrate; // 선택
	
	public NutritionFacts(int servingSize, int servings) {
		this(servingSize, servings, 0);
	}
	
	public NutritionFacts(int servingSize, int servings, int calories) {
		this(servingSize, servings, calories, 0);
	}
	
	public NutritionFacts(int servingSize, int servings, int calories, int fat) {
		this(servingSize, servings, calories, fat, 0);
	}
	
	public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
		this(servingSize, servings, calories, fat, sodium, 0);
	}
	
	public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
		this.servingSize = servingSize;
		this.servings = servings;
		this.calories = calories;
		this.fat = fat;
		this.sodium = sodium;
		this.carbohydrate = carbohydrate;
	}
}
```

### 자바 빈즈 패턴

매개변수가 없는 생성자로 객체를 만든 후 세터 메서드를 호출해 원하는 매개변수의 값을 설정하는 방식으로 점층적 생성자 패턴보다는 가독성이 좋다는 장점이 있다.

우리 프로젝트에서 자주 보이는 패턴이다.

MyBatis Generator에서 만들어주는 객체가 빌더 패턴을 제공하지 않아 이 방법을 택한 것 같다. (추가하려면 추가할 수 있을지도?)

```java
User user = new User();
user.setName("이상민");
user.setAge(28);
user.setId("lsm0506");
...
```

자바빈즈 패턴에서는 객체 하나를 만들기 위해 메서드를 여러개 호출해야하고, 객체가 완전히 완성되기 전까지는 일관성(consistency)가 무너진 상태에 놓이게 된다는 치명적인 단점이 있다.

이를 보완하기 위해 객체가 완성되기 전까지는 사용되지 못하게 얼리는 freeze 메서드를 만들어 사용하긴 하나, 실무에서는 사용하지 않는다고 한다.

### 대망의 빌더 패턴

점층적 생성자 패턴의 안전성과 자바 빈즈 패턴의 가독성을 갖춘 패턴이다.

1. 클라이언트는 필요한 객체를 직접 만드는 대신, 필수 매개변수만으로 생성자(혹은 정적 팩터리 메서드)를 호출해 빌더 객체를 얻는다.
2. 빌더 객체가 제공해주는 일종의 세터 메서드를 통해 원하는 선택 매개변수를 설정한다.
3. 마지막으로 매개변수가 없는 build() 메서드를 호출하여 우리가 필요한 객체(주로 불변객체)를 얻는다.

```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // 필수 매개변수
        private final int servingSize;
        private final int servings;

        // 선택 매개변수 - 기본값으로 초기화한다. 
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) { calories = val; return this }
        public Builder fat(int val) { fat = val; return this }
        public Builder sodium(int val) { sodium = val; return this }
        public Builder carbohydrate(int val) { carbohydrate = val; return this }

        private NutritionFacts(Builder builder) {
            servingSize = builder.servingSize;
            servings = builder.servings;
            calories = builder.calories;
            fat = builder.fat;
            sodium = builder.sodium;
            carbohydrate = builder.carbohydrate;
        }
}
```

- 빌더는 생성할 클래스 안에 정적 멤버 클래스로 만들어둔다.
- 빌더의 세터는 자기 자신을 반환하기 때문에 연쇄적으로 호출이 가능한데 이를 메서드 체이닝이라 한다.

**장점**

1. 쓰기 쉽고 읽기 쉽다.
2. 계층적으로 설계된 클래스와 함께 쓰기 좋다.
    - 상속받는 하위 클래스에 상위 클래스의 빌더패턴을 상속받아 빌더를 만들어 사용할 수 있다.
    - 하위 클래스에만 존재하는 필드만 상속받은 빌더에 추가하여 사용할 수 있다.
    - 추상 클래스에 추상 빌더를, 구체 클래스에는 구체 클래스 빌더를 갖게 하여 사용
3. 빌더 하나로 여러 객체를 순회하면서 만들 수 있고, 빌더에 넘기는 매개변수에 따라 다른 객체를 만들 수 있어 유연하다.

**단점**

1. 객체를 만들려면 빌더부터 만들어야 되므로 생성 비용이 추가되기에 성능에 민감한 상황에는 문제가 될 수 있다.
2. 빌더 패턴을 적용하기 위해 쓰는 작성해야 하는 코드가 증가한다 ⇒ 실무에서는 Lombok의 @Builder 어노테이션을 사용하면 실무에서는 이런 단점을 극복할 수 있다, 하지만 필수 값을 Builder로 못만든다는 단점 ㅠ
3. 매개변수가 4개 이상은 되어야 고려해볼만 하다. but 필드는 시간이 지날수록 증가하기 때문에 빌더 패턴을 바로 사용하는 게 용이할 때가 있다.

### 정리

생성자나 정적 팩토리가 처리해야할 매개변수가 많다면 빌더 패턴을 선택하자.

특히 매개변수 중 다수가 필수가 아니거나 같은 타입이면 더 그렇다.

빌더패턴은 읽고 쓰기가 편하며 자바빈즈 패턴보다 훨씬 안전하다