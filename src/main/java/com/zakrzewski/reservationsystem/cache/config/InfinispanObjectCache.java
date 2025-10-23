package com.zakrzewski.reservationsystem.cache.config;

import org.infinispan.Cache;
import org.infinispan.CacheSet;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class InfinispanObjectCache implements Serializable {
    private final Cache<String, CacheValueModel> mainCache;
    private final Cache<String, Boolean> updatingCache;

    private static final EmbeddedCacheManager CACHE_MANAGER = new DefaultCacheManager();

    public InfinispanObjectCache(String cacheName, long maxSize, long duration, TimeUnit timeUnit) {
        ConfigurationBuilder mainCfg = new ConfigurationBuilder();

        mainCfg.memory()
                .maxCount(maxSize)
                .whenFull(EvictionStrategy.REMOVE);

        mainCfg.expiration()
                .lifespan(duration, timeUnit);

        CACHE_MANAGER.defineConfiguration(cacheName, mainCfg.build());

        ConfigurationBuilder updCfg = new ConfigurationBuilder();
        updCfg.memory()
                .maxCount(maxSize)
                .whenFull(EvictionStrategy.REMOVE);
        updCfg.expiration()
                .lifespan(1, TimeUnit.MINUTES);

        CACHE_MANAGER.defineConfiguration(cacheName + "_UPDATING", updCfg.build());

        mainCache = CACHE_MANAGER.getCache(cacheName);
        updatingCache = CACHE_MANAGER.getCache(cacheName + "_UPDATING");
    }

    public boolean isInCache(String key, int secondsValid) {
        final CacheValueModel cacheValueModel = mainCache.get(key);
        return cacheValueModel != null && cacheValueModel.isValueValid(secondsValid);
    }

    public Object get(String key) {
        final CacheValueModel cacheValueModel = mainCache.get(key);
        return cacheValueModel != null ? cacheValueModel.getValue() : null;
    }

    public void put(String key, Object value) {
        mainCache.put(key, new CacheValueModel(value));
    }

    public void remove(String key) {
        mainCache.remove(key);
    }

    public boolean isUpdatingMarkIfNo(String key) {
        return updatingCache.putIfAbsent(key, true) == null;
    }

    public void endUpdate(String key) {
        updatingCache.remove(key);
    }

    public List<Object> getAllDataFromCache() {
        return mainCache.values()
                .stream()
                .filter(Objects::nonNull)
                .map(CacheValueModel::getValue)
                .toList();
    }

    public Map<String, Object> getDataFromCache() {
        final Map<String, Object> data = new HashMap<>();
        final CacheSet<String> keySet = mainCache.keySet();
        for (final String key : keySet) {
            final CacheValueModel cacheValueModel = mainCache.get(key);
            data.put(key, cacheValueModel.getValue());
        }
        return data;
    }
}
