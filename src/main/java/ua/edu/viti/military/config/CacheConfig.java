package ua.edu.viti.military.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Objects;

/**
 * Redis cache configuration.
 */
@Configuration
public class CacheConfig {

    @Bean
        public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
                Objects.requireNonNull(redisConnectionFactory, "redisConnectionFactory must not be null");
                Duration ttl = Duration.ofMinutes(10);
                Objects.requireNonNull(ttl, "ttl must not be null");
                RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(ttl)  // Default TTL 10 minutes
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()));

                return RedisCacheManager.builder(redisConnectionFactory)
                                                .cacheDefaults(cacheConfiguration)
                                                .build();
    }
}
