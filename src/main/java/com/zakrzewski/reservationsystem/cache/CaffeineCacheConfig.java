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
        final CaffeineCache roomCache = createCache("room", 10, 10);
        final CaffeineCache employeeCache = createCache("employee", 10, 1000);
        final CaffeineCache reservationByIdCache = createCache("reservation-by-id", 15, 20000);
        final CaffeineCache reservationListAllCache = createCache("reservation-list-all", 5, 1);
        final CaffeineCache reservationListByRoomCache = createCache("reservation-list-by-room", 5, 500);
        cacheManager.setCaches(List.of(
                defaultCache,
                roomCache,
                employeeCache,
                reservationByIdCache,
                reservationListAllCache,
                reservationListByRoomCache
        ));

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
