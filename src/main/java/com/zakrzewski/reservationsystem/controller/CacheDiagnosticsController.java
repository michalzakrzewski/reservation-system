package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.cache.CacheEntryResponse;
import com.zakrzewski.reservationsystem.service.cache.CacheDiatnosticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cache")
public class CacheDiagnosticsController {

    private final CacheDiatnosticsService cacheDiatnosticsService;

    @SuppressWarnings("unused")
    public CacheDiagnosticsController() {
        this(null);
    }

    @Autowired
    public CacheDiagnosticsController(final CacheDiatnosticsService cacheDiatnosticsService) {
        this.cacheDiatnosticsService = cacheDiatnosticsService;
    }

    @GetMapping("/content")
    public ResponseEntity<Map<String, List<CacheEntryResponse>>> getAllCacheContent() {
        final Map<String, List<CacheEntryResponse>> allCacheContent = cacheDiatnosticsService.getAllCacheContent();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allCacheContent);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getCacheSizes() {
        final Map<String, Long> cacheSizes = cacheDiatnosticsService.getCacheSizes();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cacheSizes);
    }
}
