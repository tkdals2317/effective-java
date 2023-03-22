package org.example.item44;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheLinkedHashMap<K, V> extends LinkedHashMap<K, V>{
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > 100;
    }
}
