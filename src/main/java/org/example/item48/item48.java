package org.example.item48;

import org.example.item39.Test;

import java.math.BigInteger;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;

public class item48 {
    // 코드 48-1 스트림을 사용해 처음 20개의 메르센 소수를 생성하는 프로그램
    public static void main(String[] args) {
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                //.parallel()
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);
    }
    static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }

    // 코드 48-3 소수 계산 스트림 파이프라인 - 병렬화 버전
    static long pi(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }

}
