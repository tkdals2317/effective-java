package org.example.item22;

import static org.example.item22.PhysicalConstantsUtilityClass.AVOGADROS_NUMBER;

// 정적 임포트를 사용하여 상수 이름만으로 사용하기
public class Test {
    double atoms(double mols) {
        return AVOGADROS_NUMBER * mols; // 빈번히 사용한다면 정적 임포트가 값어치를 한다.
    }
}
