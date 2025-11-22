package com.zakrzewski.reservationsystem.cache.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationCacheContainer implements ReservationCache {
    private final CacheManager cacheManager;

    @Override
    public void invalidate(final String cacheName,
                           final String cacheKey) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
                .ifPresent(cache -> cache.evictIfPresent(cacheKey));
    }

    @Override
    public void invalidateAll(final String cacheName) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
                .ifPresent(Cache::clear);
    }
}
