package org.example.item57;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class item57 {

    public static void main(String[] args) {

        Collection<Element> c = List.of(new Element(), new Element());

        // 코드 57-1 컬렉션이나 배열을 순회하는 권장 관용구
        for (Element e : c) {
            // e로 무언가를 한다.
        }

        // 코드 57-2 반복자가 필요할 때의 관용구
        for (Iterator<Element> i = c.iterator(); i.hasNext();) {
            Element e = i.next();
            // e와 i로 무언가를 한다.
        }

        Collection<Element> c1 = List.of(new Element(), new Element());
        Collection<Element> c2 = List.of(new Element(), new Element());

        Iterator<Element> i = c1.iterator();
        while(i.hasNext()) {
            doSometing(i.next());
        }

        Iterator<Element> i2 = c2.iterator();
        while (i.hasNext()) { // 버그!
            doSometing(i2.next());
        }

        for (Iterator<Element> iter = c.iterator(); iter.hasNext();) {
            Element e = iter.next();
            // e와 i로 무언가를 한다.
        }

        // 다음 코드는 "iter를 찾을 수 없다"는 컴파일 오류를 낸다.
        //for (Iterator<Element> iter2 = c2.iterator(); iter.hasNext();) {
            //Element e = iter.next();
            // e와 i로 무언가를 한다.
        //}


        for (int index = 0, n = expensiveComputation(); index < n; index++){
            // index로 무언가를 한다.
        }
    }

    private static int expensiveComputation() {
        return 100000;
    }

    private static void doSometing(Element next) {
        
    }
}
