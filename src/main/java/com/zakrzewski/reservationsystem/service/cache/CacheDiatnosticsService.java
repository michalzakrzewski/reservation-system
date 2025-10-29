package com.zakrzewski.reservationsystem.service.cache;

import com.zakrzewski.reservationsystem.dto.response.CacheEntryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CacheDiatnosticsService {

    private final CacheManager cacheManager;

    @SuppressWarnings("unused")
    public CacheDiatnosticsService() {
        this(null);
    }

    @Autowired
    public CacheDiatnosticsService(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Map<String, List<CacheEntryResponse>> getAllCacheContent() {
        final Map<String, List<CacheEntryResponse>> cacheContent = new HashMap<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                List<CacheEntryResponse> entries = nativeCache.asMap().entrySet().stream()
                        .map(entry -> new CacheEntryResponse(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());

                cacheContent.put(cacheName, entries);
            }
        }
        return cacheContent;
    }

    public Map<String, Long> getCacheSizes() {
        final Map<String, Long> sizes = new HashMap<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                sizes.put(cacheName, caffeineCache.getNativeCache().estimatedSize());
            }
        }
        return sizes;
    }
}
