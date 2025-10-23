package com.zakrzewski.reservationsystem.cache.config;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CacheValueModel implements Serializable {
    private final Object value;
    private final long timestamp;

    public CacheValueModel(Object value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isValueValid(int secondsValid) {
        long now = System.currentTimeMillis();
        return (now - timestamp) < (secondsValid * 1000L);
    }
}
