package com.zakrzewski.reservationsystem.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {

    @Value("${http.client.service.client.write.timeout:10}")
    private long writeTimeout;
    @Value("${http.client.service.client.call.timeout:10}")
    private long callTimeout;
    @Value("${http.client.service.client.read.timeout:5}")
    private long readTimeout;
    @Value("${http.client.service.client.connection.timeout:2}")
    private long connectionTimeout;
    @Value("${http.client.service.client.connection.pool.size:10}")
    private int connectionPoolSize;
    @Value("${http.client.service.client.connection.per.route.size:10}")
    private int connectionPerRouteSize;

    public RetrofitConfig() {
    }

    @Bean
    public OkHttpClient httpClient() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .callTimeout(callTimeout, TimeUnit.SECONDS)
                .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(connectionPoolSize, readTimeout, TimeUnit.SECONDS))
                .addInterceptor(interceptor)
                .build();
    }
}
