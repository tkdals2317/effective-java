#  Effective Java

![image](https://user-images.githubusercontent.com/49682056/216249756-01a72430-5a43-4956-8b1e-9692a2782c59.png)

EFFECTIVE JAVA 3/E(조슈아 블로크) 책을 스터디하며 정리한 내용과 공부하면서 테스트 해본 소스 코드를 올린 레포지토리입니다.

#### 스터디 방식
- 매주 5개의 아이템 학습 및 정리
- 정리자료 폴더에 스터디 학습 내용 업로드
- 책에서 나온 소스 코드 및 테스트 코드 업로드

#### Effective Java Items
- [2장 객체 생성과 파괴](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4)  
  - [item 1. 생성자 대신 정적 팩터리 메서드를 고려하라](https://github.com/tkdals2317/effective-java/blob/ee217e4cbc7dc9981b95f4f90b6e04955902a786/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%201.%20%EC%83%9D%EC%84%B1%EC%9E%90%20%EB%8C%80%EC%8B%A0%20%EC%A0%95%EC%A0%81%20%ED%8C%A9%ED%84%B0%EB%A6%AC%20%EB%A9%94%EC%84%9C%EB%93%9C%EB%A5%BC%20%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC.md)
  - [item 2. 생성자에 매개변수가 많다면 빌더를 고려하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%202.%20%EC%83%9D%EC%84%B1%EC%9E%90%EC%97%90%20%EB%A7%A4%EA%B0%9C%EB%B3%80%EC%88%98%EA%B0%80%20%EB%A7%8E%EB%8B%A4%EB%A9%B4%20%EB%B9%8C%EB%8D%94%EB%A5%BC%20%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC.md)  
  - [item 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%203.%20private%20%EC%83%9D%EC%84%B1%EC%9E%90%EB%82%98%20%EC%97%B4%EA%B1%B0%20%ED%83%80%EC%9E%85%EC%9C%BC%EB%A1%9C%20%EC%8B%B1%EA%B8%80%ED%84%B4%EC%9E%84%EC%9D%84%20%EB%B3%B4%EC%A6%9D%ED%95%98%EB%9D%BC.md)
  - [item 4. 인스턴스화를 막으려거든 private 생성자를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%204.%20%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4%ED%99%94%EB%A5%BC%20%EB%A7%89%EC%9C%BC%EB%A0%A4%EA%B1%B0%EB%93%A0%20private%20%EC%83%9D%EC%84%B1%EC%9E%90%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%205.%20%EC%9E%90%EC%9B%90%EC%9D%84%20%EC%A7%81%EC%A0%91%20%EB%AA%85%EC%8B%9C%ED%95%98%EC%A7%80%20%EB%A7%90%EA%B3%A0%20%EC%9D%98%EC%A1%B4%20%EA%B0%9D%EC%B2%B4%20%EC%A3%BC%EC%9E%85%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 6. 불필요한 객체 생성을 피하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%206.%20%EB%B6%88%ED%95%84%EC%9A%94%ED%95%9C%20%EA%B0%9D%EC%B2%B4%20%EC%83%9D%EC%84%B1%EC%9D%84%20%ED%94%BC%ED%95%98%EB%9D%BC.md)
  - [item 7. 다 쓴 객체 참조를 해제하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%207.%20%EB%8B%A4%20%EC%93%B4%20%EA%B0%9D%EC%B2%B4%20%EC%B0%B8%EC%A1%B0%EB%A5%BC%20%ED%95%B4%EC%A0%9C%ED%95%98%EB%9D%BC.md)
  - [item 8. finalizer와 cleaner 사용을 피하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%208.%20finalizer%EC%99%80%20cleaner%20%EC%82%AC%EC%9A%A9%EC%9D%84%20%ED%94%BC%ED%95%98%EB%9D%BC.md)
  - [item 9. try-finally보다는 try-with-resources를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/2%EC%9E%A5%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EC%83%9D%EC%84%B1%EA%B3%BC%20%ED%8C%8C%EA%B4%B4/item%209.%20try-finally%EB%B3%B4%EB%8B%A4%EB%8A%94%20try-with-resources%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  
- [3장 모든 객체의 공통 메서드](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/3%EC%9E%A5%20%EB%AA%A8%EB%93%A0%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EA%B3%B5%ED%86%B5%20%EB%A9%94%EC%84%9C%EB%93%9C)
  - [item 10. equals는 일반 규약을 지켜 재정의하라](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/3%EC%9E%A5%20%EB%AA%A8%EB%93%A0%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EA%B3%B5%ED%86%B5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2010.%20equals%EB%8A%94%20%EC%9D%BC%EB%B0%98%20%EA%B7%9C%EC%95%BD%EC%9D%84%20%EC%A7%80%EC%BC%9C%20%EC%9E%AC%EC%A0%95%EC%9D%98%ED%95%98%EB%9D%BC.md)
  - [item 11. equals를 재정의하려거든 hashCode도 재정의 하라](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/3%EC%9E%A5%20%EB%AA%A8%EB%93%A0%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EA%B3%B5%ED%86%B5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2011.%20equals%EB%A5%BC%20%EC%9E%AC%EC%A0%95%EC%9D%98%ED%95%98%EB%A0%A4%EA%B1%B0%EB%93%A0%20hashCode%EB%8F%84%20%EC%9E%AC%EC%A0%95%EC%9D%98%20%ED%95%98%EB%9D%BC.md)
  - [item 12. toString을 항상 재정의하라](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/3%EC%9E%A5%20%EB%AA%A8%EB%93%A0%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EA%B3%B5%ED%86%B5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2012.%20toString%EC%9D%84%20%ED%95%AD%EC%83%81%20%EC%9E%AC%EC%A0%95%EC%9D%98%ED%95%98%EB%9D%BC.md)
  - [item 13. clone 재정의는 주의해서 진행하라](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/3%EC%9E%A5%20%EB%AA%A8%EB%93%A0%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EA%B3%B5%ED%86%B5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2013.%20clone%20%EC%9E%AC%EC%A0%95%EC%9D%98%EB%8A%94%20%EC%A3%BC%EC%9D%98%ED%95%B4%EC%84%9C%20%EC%A7%84%ED%96%89%ED%95%98%EB%9D%BC.md)
  - [item 14. Comparable을 구현할지 고려하라](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/3%EC%9E%A5%20%EB%AA%A8%EB%93%A0%20%EA%B0%9D%EC%B2%B4%EC%9D%98%20%EA%B3%B5%ED%86%B5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2014.%20Comparable%EC%9D%84%20%EA%B5%AC%ED%98%84%ED%95%A0%EC%A7%80%20%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC.md)

- [4장 클래스와 인터페이스](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4)  
  - [item 15. 클래스와 멤버 접근 권한을 최소화하라](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2015.%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EB%A9%A4%EB%B2%84%20%EC%A0%91%EA%B7%BC%20%EA%B6%8C%ED%95%9C%EC%9D%84%20%EC%B5%9C%EC%86%8C%ED%99%94%ED%95%98%EB%9D%BC.md)
  - [item 16. public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2016.%20public%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%97%90%EC%84%9C%EB%8A%94%20public%20%ED%95%84%EB%93%9C%EA%B0%80%20%EC%95%84%EB%8B%8C%20%EC%A0%91%EA%B7%BC%EC%9E%90%20%EB%A9%94%EC%84%9C%EB%93%9C%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 17. 변경 가능성을 최소화하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2017.%20%EB%B3%80%EA%B2%BD%20%EA%B0%80%EB%8A%A5%EC%84%B1%EC%9D%84%20%EC%B5%9C%EC%86%8C%ED%99%94%ED%95%98%EB%9D%BC.md)
  - [item 18. 상속보다는 컴포지션을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2018.%20%EC%83%81%EC%86%8D%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EC%BB%B4%ED%8F%AC%EC%A7%80%EC%85%98%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 19. 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2019.%20%EC%83%81%EC%86%8D%EC%9D%84%20%EA%B3%A0%EB%A0%A4%ED%95%B4%20%EC%84%A4%EA%B3%84%ED%95%98%EA%B3%A0%20%EB%AC%B8%EC%84%9C%ED%99%94%ED%95%98%EB%9D%BC.md)
  - [item 20. 추상 클래스보다는 인터페이스를 우선하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2020.%20%EC%B6%94%EC%83%81%20%ED%81%B4%EB%9E%98%EC%8A%A4%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%A5%BC%20%EC%9A%B0%EC%84%A0%ED%95%98%EB%9D%BC.md)
  - [item 21. 인터페이스는 구현하는 쪽을 생각해 설계하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2021.%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%8A%94%20%EA%B5%AC%ED%98%84%ED%95%98%EB%8A%94%20%EC%AA%BD%EC%9D%84%20%EC%83%9D%EA%B0%81%ED%95%B4%20%EC%84%A4%EA%B3%84%ED%95%98%EB%9D%BC.md)
  - [item 22. 인터페이스는 타입을 정의하는 용도로만 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2022.%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%8A%94%20%ED%83%80%EC%9E%85%EC%9D%84%20%EC%A0%95%EC%9D%98%ED%95%98%EB%8A%94%20%EC%9A%A9%EB%8F%84%EB%A1%9C%EB%A7%8C%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 23. 태그 달린 클래스보다는 클래스 계층구조를 활용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2023.%20%ED%83%9C%EA%B7%B8%20%EB%8B%AC%EB%A6%B0%20%ED%81%B4%EB%9E%98%EC%8A%A4%EB%B3%B4%EB%8B%A4%EB%8A%94%20%ED%81%B4%EB%9E%98%EC%8A%A4%20%EA%B3%84%EC%B8%B5%EA%B5%AC%EC%A1%B0%EB%A5%BC%20%ED%99%9C%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 24. 멤버 클래스는 되도록 static으로 만들라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2024.%20%EB%A9%A4%EB%B2%84%20%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8A%94%20%EB%90%98%EB%8F%84%EB%A1%9D%20static%EC%9C%BC%EB%A1%9C%20%EB%A7%8C%EB%93%A4%EB%9D%BC.md)
  - [item 25. 톱레벨 클래스는 한 파일에 하나만 담으라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/4%EC%9E%A5%20%ED%81%B4%EB%9E%98%EC%8A%A4%EC%99%80%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/item%2025.%20%ED%86%B1%EB%A0%88%EB%B2%A8%20%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8A%94%20%ED%95%9C%20%ED%8C%8C%EC%9D%BC%EC%97%90%20%ED%95%98%EB%82%98%EB%A7%8C%20%EB%8B%B4%EC%9C%BC%EB%9D%BC.md)

- [5장 제네릭](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD)
  - [item 26. 로 타입은 사용하지 말라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2026.%20%EB%A1%9C%20%ED%83%80%EC%9E%85%EC%9D%80%20%EC%82%AC%EC%9A%A9%ED%95%98%EC%A7%80%20%EB%A7%90%EB%9D%BC.md)
  - [item 27. 비검사 경고를 제거하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2027.%20%EB%B9%84%EA%B2%80%EC%82%AC%20%EA%B2%BD%EA%B3%A0%EB%A5%BC%20%EC%A0%9C%EA%B1%B0%ED%95%98%EB%9D%BC.md)
  - [item 28. 배열보다는 리스트를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2028.%20%EB%B0%B0%EC%97%B4%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EB%A6%AC%EC%8A%A4%ED%8A%B8%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 29. 이왕이면 제네릭 타입으로 만들라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2029.%20%EC%9D%B4%EC%99%95%EC%9D%B4%EB%A9%B4%20%EC%A0%9C%EB%84%A4%EB%A6%AD%20%ED%83%80%EC%9E%85%EC%9C%BC%EB%A1%9C%20%EB%A7%8C%EB%93%A4%EB%9D%BC.md)
  - [item 30. 이왕이면 제네릭 메서드로 만들라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2030.%20%EC%9D%B4%EC%99%95%EC%9D%B4%EB%A9%B4%20%EC%A0%9C%EB%84%A4%EB%A6%AD%20%EB%A9%94%EC%84%9C%EB%93%9C%EB%A1%9C%20%EB%A7%8C%EB%93%A4%EB%9D%BC.md)
  - [item 31. 한정적 와일드카드를 사용해 API 유연성을 높이라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2031.%20%ED%95%9C%EC%A0%95%EC%A0%81%20%EC%99%80%EC%9D%BC%EB%93%9C%EC%B9%B4%EB%93%9C%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%B4%20API%20%EC%9C%A0%EC%97%B0%EC%84%B1%EC%9D%84%20%EB%86%92%EC%9D%B4%EB%9D%BC.md)
  - [item 32. 제네릭과 가변인수를 함께 쓸 때는 신중하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2032.%20%EC%A0%9C%EB%84%A4%EB%A6%AD%EA%B3%BC%20%EA%B0%80%EB%B3%80%EC%9D%B8%EC%88%98%EB%A5%BC%20%ED%95%A8%EA%BB%98%20%EC%93%B8%20%EB%95%8C%EB%8A%94%20%EC%8B%A0%EC%A4%91%ED%95%98%EB%9D%BC.md)
  - [item 33. 타입 안전 이종 컨테이너를 고려하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/5%EC%9E%A5%20%EC%A0%9C%EB%84%A4%EB%A6%AD/item%2033.%20%ED%83%80%EC%9E%85%20%EC%95%88%EC%A0%84%20%EC%9D%B4%EC%A2%85%20%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88%EB%A5%BC%20%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC.md)  
  
- [6장 열거타입과 애너테이션](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98)
  - [item 34. int 상수 대신 열거 타입을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2034.%20int%20%EC%83%81%EC%88%98%20%EB%8C%80%EC%8B%A0%20%EC%97%B4%EA%B1%B0%20%ED%83%80%EC%9E%85%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 35. ordinal 메서드 대신 인스턴스 필드를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2035.%20ordinal%20%EB%A9%94%EC%84%9C%EB%93%9C%20%EB%8C%80%EC%8B%A0%20%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4%20%ED%95%84%EB%93%9C%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 36. 비트 필드 대신 EnumSet을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2036.%20%EB%B9%84%ED%8A%B8%20%ED%95%84%EB%93%9C%20%EB%8C%80%EC%8B%A0%20EnumSet%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 37. ordinal 인덱싱 대신 EnumMap을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2037.%20ordinal%20%EC%9D%B8%EB%8D%B1%EC%8B%B1%20%EB%8C%80%EC%8B%A0%20EnumMap%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 38. 확장할 수 있는 열거 타입이 필요하면 인터페이스를 활용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2038.%20%ED%99%95%EC%9E%A5%ED%95%A0%20%EC%88%98%20%EC%9E%88%EB%8A%94%20%EC%97%B4%EA%B1%B0%20%ED%83%80%EC%9E%85%EC%9D%B4%20%ED%95%84%EC%9A%94%ED%95%98%EB%A9%B4%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%A5%BC%20%ED%99%9C%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 39. 명명 패턴보다 애너테이션을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2039.%20%EB%AA%85%EB%AA%85%20%ED%8C%A8%ED%84%B4%EB%B3%B4%EB%8B%A4%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 40. @Override 애너테이션을 일관되게 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2040.%20%40Override%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98%EC%9D%84%20%EC%9D%BC%EA%B4%80%EB%90%98%EA%B2%8C%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 41. 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/6%EC%9E%A5%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item%2041.%20%EC%A0%95%EC%9D%98%ED%95%98%EB%A0%A4%EB%8A%94%20%EA%B2%83%EC%9D%B4%20%ED%83%80%EC%9E%85%EC%9D%B4%EB%9D%BC%EB%A9%B4%20%EB%A7%88%EC%BB%A4%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)

- [7장 람다와 스트림](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC)
  - [item 42. 익명 클래스보다는 람다를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC/item%2042.%20%EC%9D%B5%EB%AA%85%20%ED%81%B4%EB%9E%98%EC%8A%A4%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EB%9E%8C%EB%8B%A4%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 43. 람다보다는 메서드 참조를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC/item%2043.%20%EB%9E%8C%EB%8B%A4%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EB%A9%94%EC%84%9C%EB%93%9C%20%EC%B0%B8%EC%A1%B0%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md#item-43-%EB%9E%8C%EB%8B%A4%EB%B3%B4%EB%8B%A4%EB%8A%94-%EB%A9%94%EC%84%9C%EB%93%9C-%EC%B0%B8%EC%A1%B0%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC)
  - [item 44. 표준 함수형 인터페이스를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC/item%2044.%20%ED%91%9C%EC%A4%80%20%ED%95%A8%EC%88%98%ED%98%95%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md#item-44-%ED%91%9C%EC%A4%80-%ED%95%A8%EC%88%98%ED%98%95-%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC)
  - [item 45. 스트림은 주의해서 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC/item%2045.%20%EC%8A%A4%ED%8A%B8%EB%A6%BC%EC%9D%80%20%EC%A3%BC%EC%9D%98%ED%95%B4%EC%84%9C%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 46. 스트림에서는 부작용 없는 함수를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC/item%2046.%20%EC%8A%A4%ED%8A%B8%EB%A6%BC%EC%97%90%EC%84%9C%EB%8A%94%20%EB%B6%80%EC%9E%91%EC%9A%A9%20%EC%97%86%EB%8A%94%20%ED%95%A8%EC%88%98%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 47. 반환 타입으로는 스트림보다 컬렉션이 낫다](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC/item%2047.%20%EB%B0%98%ED%99%98%20%ED%83%80%EC%9E%85%EC%9C%BC%EB%A1%9C%EB%8A%94%20%EC%8A%A4%ED%8A%B8%EB%A6%BC%EB%B3%B4%EB%8B%A4%20%EC%BB%AC%EB%A0%89%EC%85%98%EC%9D%B4%20%EB%82%AB%EB%8B%A4.md)
  - [item 48. 스트림 병렬화는 주의해서 적용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/7%EC%9E%A5%20%EB%9E%8C%EB%8B%A4%EC%99%80%20%EC%8A%A4%ED%8A%B8%EB%A6%BC/item%2048%20%EC%8A%A4%ED%8A%B8%EB%A6%BC%20%EB%B3%91%EB%A0%AC%ED%99%94%EB%8A%94%20%EC%A3%BC%EC%9D%98%ED%95%B4%EC%84%9C%20%EC%A0%81%EC%9A%A9%ED%95%98%EB%9D%BC.md)

- [8장 메서드](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C)
  - [item 49. 매개변수가 유효한지 검사하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2049.%20%EB%A7%A4%EA%B0%9C%EB%B3%80%EC%88%98%EA%B0%80%20%EC%9C%A0%ED%9A%A8%ED%95%9C%EC%A7%80%20%EA%B2%80%EC%82%AC%ED%95%98%EB%9D%BC.md)
  - [item 50. 적시에 방어적 복사본을 만들라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2050.%20%EC%A0%81%EC%8B%9C%EC%97%90%20%EB%B0%A9%EC%96%B4%EC%A0%81%20%EB%B3%B5%EC%82%AC%EB%B3%B8%EC%9D%84%20%EB%A7%8C%EB%93%A4%EB%9D%BC.md)
  - [item 51. 메서드 시그니처를 신중히 설계하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2051.%20%EB%A9%94%EC%84%9C%EB%93%9C%20%EC%8B%9C%EA%B7%B8%EB%8B%88%EC%B2%98%EB%A5%BC%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%EC%84%A4%EA%B3%84%ED%95%98%EB%9D%BC.md)
  - [item 52. 다중정의는 신중히 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2052.%20%EB%8B%A4%EC%A4%91%EC%A0%95%EC%9D%98%EB%8A%94%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 53. 가변인수는 신중히 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2053.%20%EA%B0%80%EB%B3%80%EC%9D%B8%EC%88%98%EB%8A%94%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 54. null이 아닌, 빈 컬렉션이나 배열을 반환하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2054.%20null%EC%9D%B4%20%EC%95%84%EB%8B%8C%2C%20%EB%B9%88%20%EC%BB%AC%EB%A0%89%EC%85%98%EC%9D%B4%EB%82%98%20%EB%B0%B0%EC%97%B4%EC%9D%84%20%EB%B0%98%ED%99%98%ED%95%98%EB%9D%BC.md)
  - [item 55. 옵셔널 반환은 신중히 하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2055.%20%EC%98%B5%EC%85%94%EB%84%90%20%EB%B0%98%ED%99%98%EC%9D%80%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%ED%95%98%EB%9D%BC.md)
  - [item 56. 공개된 API 요소에는 항상 문서화 주석을 작성하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/8%EC%9E%A5%20%EB%A9%94%EC%84%9C%EB%93%9C/item%2056.%20%EA%B3%B5%EA%B0%9C%EB%90%9C%20API%20%EC%9A%94%EC%86%8C%EC%97%90%EB%8A%94%20%ED%95%AD%EC%83%81%20%EB%AC%B8%EC%84%9C%ED%99%94%20%EC%A3%BC%EC%84%9D%EC%9D%84%20%EC%9E%91%EC%84%B1%ED%95%98%EB%9D%BC.md)

- [9장 일반적인 프로그래밍 원칙](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99)
  - [item 57. 지역변수의 범위를 최소화하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2057.%20%EC%A7%80%EC%97%AD%EB%B3%80%EC%88%98%EC%9D%98%20%EB%B2%94%EC%9C%84%EB%A5%BC%20%EC%B5%9C%EC%86%8C%ED%99%94%ED%95%98%EB%9D%BC.md)
  - [item 58. 전통적인 for문 보다는 for-each 문을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2058.%20%EC%A0%84%ED%86%B5%EC%A0%81%EC%9D%B8%20for%EB%AC%B8%20%EB%B3%B4%EB%8B%A4%EB%8A%94%20for-each%20%EB%AC%B8%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 59. 라이브러리를 익히고 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2059.%20%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC%EB%A5%BC%20%EC%9D%B5%ED%9E%88%EA%B3%A0%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 60. 정확한 답이 필요하다면 float과 double은 피하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2060.%20%EC%A0%95%ED%99%95%ED%95%9C%20%EB%8B%B5%EC%9D%B4%20%ED%95%84%EC%9A%94%ED%95%98%EB%8B%A4%EB%A9%B4%20float%EA%B3%BC%20double%EC%9D%80%20%ED%94%BC%ED%95%98%EB%9D%BC.md)
  - [item 61. 박싱된 기본 타입보다는 기본 타입을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2061.%20%EB%B0%95%EC%8B%B1%EB%90%9C%20%EA%B8%B0%EB%B3%B8%20%ED%83%80%EC%9E%85%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EA%B8%B0%EB%B3%B8%20%ED%83%80%EC%9E%85%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 62. 다른 타입이 적절하다면 문자열 사용을 피하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2062.%20%EB%8B%A4%EB%A5%B8%20%ED%83%80%EC%9E%85%EC%9D%B4%20%EC%A0%81%EC%A0%88%ED%95%98%EB%8B%A4%EB%A9%B4%20%EB%AC%B8%EC%9E%90%EC%97%B4%20%EC%82%AC%EC%9A%A9%EC%9D%84%20%ED%94%BC%ED%95%98%EB%9D%BC.md)
  - [item 63. 문자열 연결은 느리니 주의하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2063.%20%EB%AC%B8%EC%9E%90%EC%97%B4%20%EC%97%B0%EA%B2%B0%EC%9D%80%20%EB%8A%90%EB%A6%AC%EB%8B%88%20%EC%A3%BC%EC%9D%98%ED%95%98%EB%9D%BC.md)
  - [item 64. 객체는 인터페이스를 사용해 참조하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2064.%20%EA%B0%9D%EC%B2%B4%EB%8A%94%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%B4%20%EC%B0%B8%EC%A1%B0%ED%95%98%EB%9D%BC.md)
  - [item 65. 리플렉션보다는 인터페이스를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2065.%20%EB%A6%AC%ED%94%8C%EB%A0%89%EC%85%98%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 66. 네이티브 메서드는 신중히 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2066.%20%EB%84%A4%EC%9D%B4%ED%8B%B0%EB%B8%8C%20%EB%A9%94%EC%84%9C%EB%93%9C%EB%8A%94%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 67. 최적화는 신중히 하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2067.%20%EC%B5%9C%EC%A0%81%ED%99%94%EB%8A%94%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%ED%95%98%EB%9D%BC.md)
  - [item 68. 일반적으로 통용되는 명명 규칙을 따르라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/9%EC%9E%A5%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9D%B8%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%9B%90%EC%B9%99/item%2068.%20%EC%9D%BC%EB%B0%98%EC%A0%81%EC%9C%BC%EB%A1%9C%20%ED%86%B5%EC%9A%A9%EB%90%98%EB%8A%94%20%EB%AA%85%EB%AA%85%20%EA%B7%9C%EC%B9%99%EC%9D%84%20%EB%94%B0%EB%A5%B4%EB%9D%BC.md)

- [10장 예외](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8)
  - [item 69. 예외는 진짜 예외 상황에만 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2069.%20%EC%98%88%EC%99%B8%EB%8A%94%20%EC%A7%84%EC%A7%9C%20%EC%98%88%EC%99%B8%20%EC%83%81%ED%99%A9%EC%97%90%EB%A7%8C%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 70. 복구할 수 있는 상황에는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2070.%20%EB%B3%B5%EA%B5%AC%ED%95%A0%20%EC%88%98%20%EC%9E%88%EB%8A%94%20%EC%83%81%ED%99%A9%EC%97%90%EB%8A%94%20%EA%B2%80%EC%82%AC%20%EC%98%88%EC%99%B8%EB%A5%BC%2C%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%20%EC%98%A4%EB%A5%98%EC%97%90%EB%8A%94%20%EB%9F%B0%ED%83%80%EC%9E%84%20%EC%98%88%EC%99%B8%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 71. 필요 없는 검사 예외를 피하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2071.%20%ED%95%84%EC%9A%94%20%EC%97%86%EB%8A%94%20%EA%B2%80%EC%82%AC%20%EC%98%88%EC%99%B8%EB%A5%BC%20%ED%94%BC%ED%95%98%EB%9D%BC.md)
  - [item 72. 표준 예외를 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2072.%20%ED%91%9C%EC%A4%80%20%EC%98%88%EC%99%B8%EB%A5%BC%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 73. 추상화 수준에 맞는 예외를 던지라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2073.%20%EC%B6%94%EC%83%81%ED%99%94%20%EC%88%98%EC%A4%80%EC%97%90%20%EB%A7%9E%EB%8A%94%20%EC%98%88%EC%99%B8%EB%A5%BC%20%EB%8D%98%EC%A7%80%EB%9D%BC.md)
  - [item 74. 메서드가 던지는 모든 예외를 문서화하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2074.%20%EB%A9%94%EC%84%9C%EB%93%9C%EA%B0%80%20%EB%8D%98%EC%A7%80%EB%8A%94%20%EB%AA%A8%EB%93%A0%20%EC%98%88%EC%99%B8%EB%A5%BC%20%EB%AC%B8%EC%84%9C%ED%99%94%ED%95%98%EB%9D%BC.md)
  - [item 75. 예외의 상세 메시지에 실패 관련 정보를 담으라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2075.%20%EC%98%88%EC%99%B8%EC%9D%98%20%EC%83%81%EC%84%B8%20%EB%A9%94%EC%8B%9C%EC%A7%80%EC%97%90%20%EC%8B%A4%ED%8C%A8%20%EA%B4%80%EB%A0%A8%20%EC%A0%95%EB%B3%B4%EB%A5%BC%20%EB%8B%B4%EC%9C%BC%EB%9D%BC.md)
  - [item 76. 가능한 한 실패 원자적으로 만들라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2076.%20%EA%B0%80%EB%8A%A5%ED%95%9C%20%ED%95%9C%20%EC%8B%A4%ED%8C%A8%20%EC%9B%90%EC%9E%90%EC%A0%81%EC%9C%BC%EB%A1%9C%20%EB%A7%8C%EB%93%A4%EB%9D%BC.md)
  - [item 77. 예외를 무시하지 말라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/10%EC%9E%A5%20%EC%98%88%EC%99%B8/item%2077.%20%EC%98%88%EC%99%B8%EB%A5%BC%20%EB%AC%B4%EC%8B%9C%ED%95%98%EC%A7%80%20%EB%A7%90%EB%9D%BC.md)

- [11장 동시성](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1)
  - [item 78. 공유 중인 가변 데이터는 동기화해 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1/item%2078.%20%EA%B3%B5%EC%9C%A0%20%EC%A4%91%EC%9D%B8%20%EA%B0%80%EB%B3%80%20%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%8A%94%20%EB%8F%99%EA%B8%B0%ED%99%94%ED%95%B4%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 79. 과도한 동기화는 피하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1/item%2079.%20%EA%B3%BC%EB%8F%84%ED%95%9C%20%EB%8F%99%EA%B8%B0%ED%99%94%EB%8A%94%20%ED%94%BC%ED%95%98%EB%9D%BC.md)
  - [item 80. 스레드보다는 실행자, 태스크, 스트림을 애용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1/item%2080.%20%EC%8A%A4%EB%A0%88%EB%93%9C%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EC%8B%A4%ED%96%89%EC%9E%90%2C%20%ED%83%9C%EC%8A%A4%ED%81%AC%2C%20%EC%8A%A4%ED%8A%B8%EB%A6%BC%EC%9D%84%20%EC%95%A0%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 81. wait와 notify보다는 동시성 유틸리티를 애용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1/item%2081.%20wait%EC%99%80%20notify%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EB%8F%99%EC%8B%9C%EC%84%B1%20%EC%9C%A0%ED%8B%B8%EB%A6%AC%ED%8B%B0%EB%A5%BC%20%EC%95%A0%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 82. 스레드 안전성 수준을 문서화하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1/item%2082.%20%EC%8A%A4%EB%A0%88%EB%93%9C%20%EC%95%88%EC%A0%84%EC%84%B1%20%EC%88%98%EC%A4%80%EC%9D%84%20%EB%AC%B8%EC%84%9C%ED%99%94%ED%95%98%EB%9D%BC.md)
  - [item 83. 지연 초기화는 신중히 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1/item%2083.%20%EC%A7%80%EC%97%B0%20%EC%B4%88%EA%B8%B0%ED%99%94%EB%8A%94%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)
  - [item 84. 프로그램의 동작을 스레드 스케줄러에 기대지 말라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/11%EC%9E%A5%20%EB%8F%99%EC%8B%9C%EC%84%B1/item%2084.%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8%EC%9D%98%20%EB%8F%99%EC%9E%91%EC%9D%84%20%EC%8A%A4%EB%A0%88%EB%93%9C%20%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC%EC%97%90%20%EA%B8%B0%EB%8C%80%EC%A7%80%20%EB%A7%90%EB%9D%BC.md)  
- [12장 직렬화](https://github.com/tkdals2317/effective-java/tree/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/12%EC%9E%A5%20%EC%A7%81%EB%A0%AC%ED%99%94)
  - [item 85. 자바 직렬화의 대안을 찾으라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/12%EC%9E%A5%20%EC%A7%81%EB%A0%AC%ED%99%94/item%2085.%20%EC%9E%90%EB%B0%94%20%EC%A7%81%EB%A0%AC%ED%99%94%EC%9D%98%20%EB%8C%80%EC%95%88%EC%9D%84%20%EC%B0%BE%EC%9C%BC%EB%9D%BC.md)
  - [item 86. Serializable을 구현할지는 신중히 결정하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/12%EC%9E%A5%20%EC%A7%81%EB%A0%AC%ED%99%94/item%2086.%20Serializable%EC%9D%84%20%EA%B5%AC%ED%98%84%ED%95%A0%EC%A7%80%EB%8A%94%20%EC%8B%A0%EC%A4%91%ED%9E%88%20%EA%B2%B0%EC%A0%95%ED%95%98%EB%9D%BC.md)
  - [item 87. 커스텀 직렬화 형태를 고려해보라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/12%EC%9E%A5%20%EC%A7%81%EB%A0%AC%ED%99%94/item%2087.%20%EC%BB%A4%EC%8A%A4%ED%85%80%20%EC%A7%81%EB%A0%AC%ED%99%94%20%ED%98%95%ED%83%9C%EB%A5%BC%20%EA%B3%A0%EB%A0%A4%ED%95%B4%EB%B3%B4%EB%9D%BC.md)
  - [item 88. readObject 메서드는 방어적으로 작성하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/12%EC%9E%A5%20%EC%A7%81%EB%A0%AC%ED%99%94/item%2088.%20readObject%20%EB%A9%94%EC%84%9C%EB%93%9C%EB%8A%94%20%EB%B0%A9%EC%96%B4%EC%A0%81%EC%9C%BC%EB%A1%9C%20%EC%9E%91%EC%84%B1%ED%95%98%EB%9D%BC.md)
  - [item 89. 인스턴스 수를 통제해야 한다면 readResolve보다는 열거타입을 사용하라](https://github.com/tkdals2317/effective-java/blob/master/%EC%A0%95%EB%A6%AC%20%EC%9E%90%EB%A3%8C/12%EC%9E%A5%20%EC%A7%81%EB%A0%AC%ED%99%94/item%2089.%20%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4%20%EC%88%98%EB%A5%BC%20%ED%86%B5%EC%A0%9C%ED%95%B4%EC%95%BC%20%ED%95%9C%EB%8B%A4%EB%A9%B4%20readResolve%EB%B3%B4%EB%8B%A4%EB%8A%94%20%EC%97%B4%EA%B1%B0%ED%83%80%EC%9E%85%EC%9D%84%20%EC%82%AC%EC%9A%A9%ED%95%98%EB%9D%BC.md)

#### 정리 내용은 노션에서도 확인할 수 있습니다.

[이펙티브 자바 노션 정리자료](https://handy-gladiolus-4a5.notion.site/ee068f60419f41a6a2ec62f69e3599e3)
