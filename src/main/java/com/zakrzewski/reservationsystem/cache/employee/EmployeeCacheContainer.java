package com.zakrzewski.reservationsystem.cache.employee;

import com.zakrzewski.reservationsystem.cache.CachingConfig;
import com.zakrzewski.reservationsystem.entity.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class EmployeeCacheContainer implements EmployeeCache {
    private final CacheManager cacheManager;

    @Override
    public void invalidate(final String cacheName, final String cacheKey) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
                .ifPresent(cache -> cache.evictIfPresent(cacheKey));
    }

    @Override
    public void putEmployeeToCache(final String cacheKey, final EmployeeEntity employee) {
        putToCache(CachingConfig.EMPLOYEE, cacheKey, employee);
    }

    @Override
    public void putEmployeeListToCache(final String cacheKey, final List<EmployeeEntity> employeeList) {
        putToCache(CachingConfig.EMPLOYEE_LIST, cacheKey, employeeList);
    }

    @Override
    public Optional<EmployeeEntity> getEmployeeFromCache(final String employeeCacheKey,
                                                         final Supplier<Optional<EmployeeEntity>> employeeProducer) {
        return getFromCacheWithProducer(CachingConfig.EMPLOYEE, employeeCacheKey, employeeProducer, EmployeeEntity.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<List<EmployeeEntity>> getAllEmployeesFromCache(final String employeeListCacheKey,
                                                                  final Supplier<Optional<List<EmployeeEntity>>> employeeProducer) {
        final Cache cache = cacheManager.getCache(CachingConfig.EMPLOYEE_LIST);
        List<EmployeeEntity> value = null;
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(employeeListCacheKey);
            if (wrapper != null) {
                Object cachedObject = wrapper.get();
                if (cachedObject instanceof List) {
                    value = (List<EmployeeEntity>) cachedObject;
                }
            }
            if (value == null) {
                Optional<List<EmployeeEntity>> valueOp = employeeProducer.get();
                if (valueOp.isPresent()) {
                    value = valueOp.get();
                    cache.put(employeeListCacheKey, value);
                }
            }
        }
        return Optional.ofNullable(value);
    }

    private <T> void putToCache(String cacheName, String key, T value) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
                .ifPresent(cache -> cache.put(key, value));
    }

    private <T> Optional<T> getFromCacheWithProducer(final String cacheName,
                                                     final String key,
                                                     final Supplier<Optional<T>> producer,
                                                     final Class<T> type) {
        final Cache cache = cacheManager.getCache(cacheName);
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
