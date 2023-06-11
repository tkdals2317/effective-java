# item 9. try-finally보다는 try-with-resources를 사용하라

<aside>
💡 자바 라이브러리에는 close 메서드를 호출하여 직접 닫아줘야 하는 자원이 많다 (InputStream, OutputStream, java.sql.Connection)
이런 자원들을 닫기위한 방법인 **try-with-resources**의 사용법과 장점을 알아보자

</aside>

### 전통적인 방법 try-finally

보통 자원이 제대로 닫힘을 보장하는 수단으로 예외가 발생하거나 메서드에서 반환되는 경우를 포함해서 `**try-finally**`를 사용했다.

자원 하나를 회수하는 **`try-finally`** 코드

```java
// 코드 9-1) try-finally - 더 이상 자원을 회수하는 최선의 방책이 아니다!
static String firstLineOfFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		try {
				return br.readLine();
		} finally {
				br.close();
		}
}
```

자원이 하나 이상의  **`try-finally`** 코드

```java
// 코드 9-2) 자원이 둘 이상이면 try-finally 방식은 너무 지저분하다!
static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
```

음…일단 보기는 지저분해보이긴 한다..

위 두 코드 예제에는 미묘한 결점이 있다.

예외는 `try` 블록과 `finally` 블록 모두에서 발생할 수 있다.

예컨데 기기에 물리적인 문제가 생긴다면 

**(1) firstLineOfFile 메서드 안의 readLine 메서드가 예외를 던지고,**

**(2) 같은 이유로 close 메서드도 실패하여 예외를 던진다.**

이런 상황에서 (2)번의 예외가 (1)번의 예외를 완전히 집어 삼켜버린다. 

그렇게 되면 스택 추적 내역에 (1)번의 예외는 남지 않게 되어 실제 시스템에서의 디버깅을 매우 어렵게 만든다.

### 자바 7부터 등장한 try-with-resources

위 문제를 자바 7부터 생긴 **`try-with-resources`** 로 해결 할 수 있게 되었다.

**사용법**

**`try-with-resources`** 를 사용하려면 해당 자원이 `AutoCloseable` 인터페이스를 구현해야 한다.

- `AutoCloseable` 는 단순히 void를 반환하는 close 메서드 하나만 정의한 인터페이스다

그렇다면 **`try-with-resources`** 를 사용하여 9-1과 9-2 코드 예제를 고쳐보자

```java
// 코드 9-3) try-with-resources - 자원을 회수하는 최선책(9-1 코드에 적용한 예)
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
```

```java
// 코드 9-4) 복수 자원을 처리하는 try-with-resources - 짧고 매혹적이다(9-2 코드에 적용한 예) 
static void copy(String src, String dst) throws IOException {
    try (InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(dst)) {
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = in.read(buf)) >= 0) {
            out.write(buf, 0, n);
        }
    }
}
```

### 짧고 읽기 편할 뿐만 아니라 문제를 진단하기도 편한 try-with-resources

위 코드는 readLine과 (코드에는 나타나지 않는) close 호출 양쪽에서 예외가 발생하면, close에서 발생한 예외는 숨겨지고 readLine에서 발생한 예외가 기록된다.

실전에서는 프로그래머에게 보여줄 예외 하나만 보존되고 여러개의 다른 예외가 숨겨질 수 있다.

9-1 소스 코드와 달리 이렇게 숨겨진 예외는 단순히 버려지는 것이 아니라, 

**스택 추적 내역에 ‘숨겨졌다(suppressed)’는 꼬리표를 달고 출력된다. (디버깅 용이)** 

또한 자바 7에서 Throwable에 추가된 getSuppressed 메서드를 사용하면 프로그램 코드에서 가져올 수도 있다.

---

### try-catch도 사용할 수 있는 try-with-resources

보통의 try-finally 처럼 try-with-resources에서도 catch 절을 사용할 수 있다.

catch 절 덕분에 try 문을 중첩하지 않고도 다수의 예외를 처리할 수 있다.

```java
// 코드 9-5) try-with-resources를 catch 절과 함께 쓰는 모습
static String firstLineOfFile(String path, String defaultVal) {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    } catch (IOException e) {
        return defaultVal;
    }
}
```

위 코드에서는 catch 절을 사용하여 파일을 열거나 데이터를 읽지 못했을 때 예외를 던지는 대신 기본값을 반환하도록 한 예제이다.

### 정리

try-with-resources의 장점을 정리하자면 다음과 같다.

1. 코드가 읽기 편하고 간단하다.
2. 문제(에러)를 추적하기 편하다.
3. try-catch 절을 사용할 수 있다.

꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resources를 사용하자!

예외는 없다!