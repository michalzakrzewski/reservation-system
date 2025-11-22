package com.zakrzewski.reservationsystem.cache.reservation;

public interface ReservationCache {
    void invalidate(String cacheName, String cacheKey);
    void invalidateAll(String cacheName);
}
