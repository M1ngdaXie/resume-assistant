package com.assistant.doitou.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.caffeine.CaffeineProxyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public ProxyManager<String> proxyManager() {
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
            .maximumSize(10000);

        return new CaffeineProxyManager<>(builder, Duration.ofMinutes(10));
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        // Allow 3 requests per minute per IP (fixed window - resets every minute)
        Bandwidth limit = Bandwidth.builder()
            .capacity(20)
            .refillIntervally(20, Duration.ofMinutes(1))
            .initialTokens(20)
            .build();

        return BucketConfiguration.builder()
            .addLimit(limit)
            .build();
    }
}