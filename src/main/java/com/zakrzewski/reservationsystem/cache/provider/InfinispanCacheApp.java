package com.zakrzewski.reservationsystem.cache.provider;

import java.util.List;
import java.util.Map;

public interface InfinispanCacheApp {
    Object get(final String key);
    void remove(final String key);
    void endUpdate(final String key);
    void put(final String key, final Object value);
    Object retrieveDataFromCache(final String cacheKey, final int secondsValid);
    List<Object> getAllDataFromCache();
    Map<String, Object> getDataFromCache();
}
