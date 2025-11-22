package com.zakrzewski.reservationsystem.cache;

import com.zakrzewski.reservationsystem.entity.EmployeeEntity;
import com.zakrzewski.reservationsystem.entity.RoomEntity;
import com.zakrzewski.reservationsystem.entity.RoomReservationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CacheContainer {

    private final CacheManager cacheManager;

    public void putRoomToCache(final String cacheKey, RoomEntity room) {
        putToCache(CachingConfig.ROOM, cacheKey, room);
    }

    public void putRoomReservationToCache(final String cacheKey,
                                          final RoomReservationEntity roomReservation) {
        putToCache(CachingConfig.RESERVATION, cacheKey, roomReservation);
    }

    public Optional<RoomEntity> getRoomFromCache(final String cacheKey,
                                                 final Supplier<Optional<RoomEntity>> roomProducer) {
        return getFromCacheWithProducer(CachingConfig.ROOM, cacheKey, roomProducer, RoomEntity.class);
    }

    public Optional<RoomReservationEntity> getRoomReservationFromCache(final String cacheKey,
                                                                       final Supplier<Optional<RoomReservationEntity>> roomReservationProducer) {
        return getFromCacheWithProducer(CachingConfig.RESERVATION, cacheKey, roomReservationProducer, RoomReservationEntity.class);
    }



    @SuppressWarnings("unchecked")
    public Optional<List<RoomEntity>> getAllRoomsFromCache(final String roomCacheKey,
                                                           final Supplier<Optional<List<RoomEntity>>> roomProducer) {
        final Cache cache = cacheManager.getCache(CachingConfig.ROOM_LIST);
        List<RoomEntity> value = null;
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(roomCacheKey);
            if (wrapper != null) {
                Object cachedObject = wrapper.get();
                if (cachedObject instanceof List) {
                    value = (List<RoomEntity>) cachedObject;
                }
            }
            if (value == null) {
                Optional<List<RoomEntity>> valueOp = roomProducer.get();
                if (valueOp.isPresent()) {
                    value = valueOp.get();
                    cache.put(roomCacheKey, value);
                }
            }
        }
        return Optional.ofNullable(value);
    }

    @SuppressWarnings("unchecked")
    public Optional<List<RoomReservationEntity>> getAllRoomReservationsFromCache(final String roomReservationCacheKey,
                                                                                 final Supplier<Optional<List<RoomReservationEntity>>> roomReservationProducer) {
        final Cache cache = cacheManager.getCache(CachingConfig.RESERVATION_LIST);
        List<RoomReservationEntity> value = null;
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(roomReservationCacheKey);
            if (wrapper != null) {
                Object cachedObject = wrapper.get();
                if (cachedObject instanceof List) {
                    value = (List<RoomReservationEntity>) cachedObject;
                }
            }
            if (value == null) {
                Optional<List<RoomReservationEntity>> valueOp = roomReservationProducer.get();
                if (valueOp.isPresent()) {
                    value = valueOp.get();
                    cache.put(roomReservationCacheKey, value);
                }
            }
        }
        return Optional.ofNullable(value);
    }

    private <T> void putToCache(String cacheName, Object key, T value) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
                .ifPresent(cache -> cache.put(key, value));
    }

    private void invalidate(String cacheName, Object key) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
                .ifPresent(cache -> cache.evictIfPresent(key));
    }

    private <T> Optional<T> getFromCacheWithProducer(String cacheName, Object key, Supplier<Optional<T>> producer, Class<T> type) {
        Cache cache = cacheManager.getCache(cacheName);
        T value = null;
        if (cache != null) {
            value = cache.get(key, type);
            if (value == null) {
                Optional<T> valueOp = producer.get();
                if (valueOp.isPresent()) {
                    value = valueOp.get();
                    cache.put(key, value);
                }
            }
        }
        return Optional.ofNullable(value);
    }
}
