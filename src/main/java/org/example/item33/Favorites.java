package org.example.item33;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// 코드 33-3 타입 안전 이종 컨테이너 패턴 - 구현
public class Favorites {
    private Map<Class<?>, Object> favorites = new HashMap<>();

    // 코드 33-4 동적 형변환으로 런타임 타입 안전성 확보
    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), type.cast(instance));
    }

    public <T> T getFavorite(Class<T> type) {
        return type.cast(favorites.get(type));
    }
}
