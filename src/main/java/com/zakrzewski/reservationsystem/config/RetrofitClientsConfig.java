package com.zakrzewski.reservationsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zakrzewski.reservationsystem.api.UserServiceApi;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitClientsConfig {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unused")
    public RetrofitClientsConfig() {
        this(null, null);
    }

    @Autowired
    public RetrofitClientsConfig(final OkHttpClient httpClient, final ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Value("${service.user.base.url:https://www.localhost.pl}")
    private String userServiceApiUrl;

    @Bean
    public UserServiceApi userServiceApi() {
        return new Retrofit.Builder()
                .baseUrl(userServiceApiUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(httpClient)
                .build()
                .create(UserServiceApi.class);
    }
}
