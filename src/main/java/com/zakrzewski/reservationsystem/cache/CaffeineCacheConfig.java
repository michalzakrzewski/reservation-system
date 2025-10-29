package com.zakrzewski.reservationsystem.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();

        final CaffeineCache defaultCache = createCache("default", 5, 10000);
        final CaffeineCache employeeCache = createCache("employee", 10, 1000);

        cacheManager.setCaches(List.of(defaultCache, employeeCache));
        return cacheManager;
    }

    private CaffeineCache createCache(final String name,
                                      final int ttl,
                                      final int maxSize) {
        return new CaffeineCache(
                name,
                Caffeine.newBuilder()
                        .expireAfterWrite(ttl, TimeUnit.MINUTES)
                        .maximumSize(maxSize)
                        .build()
        );
    }
}
