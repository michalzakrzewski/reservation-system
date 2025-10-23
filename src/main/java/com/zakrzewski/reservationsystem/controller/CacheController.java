package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.cache.provider.InfinispanCacheApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cache/information")
public class CacheController {
    private final InfinispanCacheApp defaultCache;

    @SuppressWarnings("unused")
    public CacheController() {
        this(null);
    }

    @Autowired
    public CacheController(final InfinispanCacheApp defaultCache) {
        this.defaultCache = defaultCache;
    }

    @GetMapping
    public Map<String, Map<String, Object>> getCache() {
        return Map.of(
                "default-cache", defaultCache.getDataFromCache()
        );
    }
}
