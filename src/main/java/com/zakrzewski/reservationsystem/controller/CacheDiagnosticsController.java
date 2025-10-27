package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.dto.response.CacheEntryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cache")
public class CacheDiagnosticsController {

    private final CacheManager cacheManager;

    @Autowired
    public CacheDiagnosticsController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/content")
    public Map<String, List<CacheEntryResponse>> getAllCacheContent() {
        Map<String, List<CacheEntryResponse>> cacheContent = new HashMap<>();
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

    @GetMapping("/stats")
    public Map<String, Long> getCacheSizes() {
        Map<String, Long> sizes = new HashMap<>();
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                sizes.put(cacheName, caffeineCache.getNativeCache().estimatedSize());
            }
        }
        return sizes;
    }
}
