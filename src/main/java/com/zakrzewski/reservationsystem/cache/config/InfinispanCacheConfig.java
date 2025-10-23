package com.zakrzewski.reservationsystem.cache.config;

import com.zakrzewski.reservationsystem.cache.provider.InfinispanCacheApp;
import com.zakrzewski.reservationsystem.cache.provider.InfinispanProviderCacheApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class InfinispanCacheConfig {
    private final static Logger LOG = LoggerFactory.getLogger(InfinispanCacheConfig.class);

    @Bean
    @Primary
    public InfinispanCacheApp provideInfinispanDefaultCache() {
        LOG.info("Init default infinispan cache");
        return new InfinispanProviderCacheApp("default", 20000, 5, TimeUnit.MINUTES);
    }
}
