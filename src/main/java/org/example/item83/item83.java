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
    }*/

    // 코드 83-3 정적 필드용 지연 초기화 홀더 클래스 관용구
    private static class FiledHolder {
        static final FieldType field = computeFieldValue();
    }

    private static FieldType getField() {return FiledHolder.field;}

}
