package org.example.item83;

public class item83 {
    // 코드 83-1 인스턴스 필드를 초기화하는 일반적인 방법
    //private final FieldType filed = computeFieldValue();

    private static FieldType computeFieldValue() {
        return null;
    }

    /*
    // 코드 83-2 인스턴스 필드의 지연 초기화 - synchronized 접근자 방식
    private FieldType field;

    private synchronized FieldType getField(){
        if (field == null)
            field = computeFieldValue();
        return field;
    }
    */

    /*
    // 코드 83-3 정적 필드용 지연 초기화 홀더 클래스 관용구
    private static class FiledHolder {
        static final FieldType field = computeFieldValue();
    }

    private static FieldType getField() {return FiledHolder.field;}
    */

    /*
    // 코드 83-4 인스턴스 필드 지연 초기화용 이중검사 관용구
    private volatile FieldType field;

    private FieldType getField() {
        FieldType result = field;
        if (result != null) { // 첫 번째 검사 (락 사용 안함)
            return result;
        }

        synchronized (this) {
            if (field == null) { // 두 번째 검사 (락 사용)
                field = computeFieldValue();
            }
            return field;
        }
    }
    */


    // 코드 83-5 단일검사 관용구 - 초기화가 중복해서 일어날 수 있다.
    private volatile FieldType field;

    private FieldType getField() {
        FieldType result = field;
        if (result == null) {
            field = result = computeFieldValue();
        }
        return field;
    }
}

