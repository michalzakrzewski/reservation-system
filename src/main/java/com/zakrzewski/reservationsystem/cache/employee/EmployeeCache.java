package com.zakrzewski.reservationsystem.cache.employee;

import com.zakrzewski.reservationsystem.entity.EmployeeEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface EmployeeCache {
    void invalidate(String cacheName, String cacheKey);
    void putEmployeeToCache(final String cacheKey, final EmployeeEntity employee);
    void putEmployeeListToCache(final String cacheKey, final List<EmployeeEntity> employeeList);
    Optional<EmployeeEntity> getEmployeeFromCache(final String employeeCacheKey, final Supplier<Optional<EmployeeEntity>> employeeProducer);
    Optional<List<EmployeeEntity>> getAllEmployeesFromCache(final String employeeListCacheKey, final Supplier<Optional<List<EmployeeEntity>>> employeeProducer);
}
