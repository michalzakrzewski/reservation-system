package com.zakrzewski.reservationsystem.cache;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record CacheEntryResponse(Object key, Object value) {
}
