package org.example.item39;

import java.lang.annotation.*;

/**
 * 코드 39-8 반복 가능한 애너테이션 타입
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
public @interface ExceptionRepeatableTest {
    Class<? extends Throwable> value();
}

