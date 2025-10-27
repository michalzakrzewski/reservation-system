package com.zakrzewski.reservationsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class CacheEntryResponse {
    private final Object key;
    private final Object value;
}
