# item 60. 정확한 답이 필요하다면 float과 double은 피하라

<aside>
💡 정확한 계산을 위한 값의 자료형으로는 float과 double은 부적합하다. 그러니 다른 방법을 생각해보자

</aside>

### float과 double

float과 double 타입은 과학과 공학 계산용으로 설계되었다.

이진 부동소수점 연산에 쓰이며, 넓은 범위의 수를 빠르게 정밀한 ‘근사치’로 계산하도록 세심하게 설계되었다.

따라서 정확한 결과가 필요한 금융관련 계산에는 float와 double 타입은 적합하지 않다.

아래의 예제 코드를 보면 알 수 있다.

```java
System.out.println(1.03 - 0.42); // 기댓값 : 0.61, 출력값 : 0.6100000000000001
System.out.println(1.00 - 9 * 0.10); //기댓값 : 0.1, 출력값 : 0.09999999999999998
```

```java
// 코드 60-1 오류 발생! 금융 계산에 부동소수 타입을 사용했다.
double funds = 1.00;
int itemBought = 0;
for (double price = 0.10; funds >= price; price+= 0.10) {
    funds -= price;
    itemBought++;
}
System.out.println(itemBought + "개 구입");
System.out.println("잔돈(달러) : " + funds);

==== output =====
3개 구입
잔돈(달러) : 0.3999999999999999
```

### 금융계산과 같은 정확한 값을 요구할 때는 BigDecimal, int 혹은 long을 사용하자

**BigDecimal을 사용한 코드**

```java
// 코드 60-2 BigDecimal을 사용한 해범. 속도가 느리고 쓰기 불편하다.
final BigDecimal TEN_CENTS = new BigDecimal(".10");

BigDecimal funds = new BigDecimal("1.00");
int itemBought = 0;
for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)) {
    funds = funds.subtract(price);
    itemBought++;
}
System.out.println(itemBought + "개 구입");
System.out.println("잔돈(달러) : " + funds);
```

정확한 값이 나오긴 하나 기본 타입보다 사용하기 불편하고 느리다는 단점이 있다.

**int 혹은 long 타입을 사용한 코드**

```java
// 코드 60-3 정수 타입을 사용한 해법
int funds = 100;
int itemBought = 0;
for (int price = 10; funds >= price; price += 10) {
    funds -= price;
    itemBought++;
}
System.out.println(itemBought + "개 구입");
System.out.println("잔돈(센트) : " + funds);
```

다룰 수 있는 값의 크기가 제한되고, 소수점을 직접 관리해야 한다.

위 코드에서는 달러 대신 센트를 기준으로 계산하여 int를 사용하였다. 

### 정리

**정확한 답이 필요한 계산에서는 float이나 double은 피하라**

**소수점 추적은 시스템에 맡기고, 코딩 시의 불편함이나 성능 저하를 신경쓰지 않는다면 BigDecimal을 사용하라**

BigDecimal이 제공하는 여덟가지 반올림 모드를 이용하여 반올림을 완벽히 제어할 수 있다.

**반면, 성능이 중요하고 소수점을 직접 추적할 수 있고 숫자가 너무 크지 않다면 int나 long을 사용하라**

숫자를 아홉 자리 십진수로 표현할 수 있다면 int, 열여덟 자리 십진수로 표현할 수 있다면 long을 사용하라

열여덟자리가 넘어간다면 BigDecimal을 사용하라