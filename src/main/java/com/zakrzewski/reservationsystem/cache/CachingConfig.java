package com.zakrzewski.reservationsystem.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CachingConfig {

    public static final String DEFAULT = "default";
    public static final String ROOM = "room";
    public static final String ROOM_LIST = "roomList";
    public static final String EMPLOYEE = "employee";
    public static final String EMPLOYEE_LIST = "employeeList";
    public static final String RESERVATION = "reservation";
    public static final String RESERVATION_LIST = "reservationList";

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    @Bean
    @Primary
    public CacheManager cacheManager(Ticker ticker) {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        final List<CaffeineCache> caffeineCaches = new ArrayList<>();
        caffeineCaches.add(new CaffeineCache(DEFAULT,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(3000)
                        .ticker(ticker)
                        .build()));

        caffeineCaches.add(new CaffeineCache(ROOM,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(3000)
                        .ticker(ticker)
                        .build()));

        caffeineCaches.add(new CaffeineCache(EMPLOYEE,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(3000)
                        .ticker(ticker)
                        .build()));

        caffeineCaches.add(new CaffeineCache(RESERVATION,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(3000)
                        .ticker(ticker)
                        .build()));
        caffeineCaches.add(new CaffeineCache(ROOM_LIST,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(3000)
                        .ticker(ticker)
                        .build()));
        caffeineCaches.add(new CaffeineCache(EMPLOYEE_LIST,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .ticker(ticker)
                        .build()));
        caffeineCaches.add(new CaffeineCache(RESERVATION_LIST,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .ticker(ticker)
                        .build()));

        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }
}
