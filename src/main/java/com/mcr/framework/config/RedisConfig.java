//package com.tf.framework.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurer;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//@Configuration
//@EnableCaching
//public class RedisConfig implements CachingConfigurer {
//
//    @Value("${cache.default.expire-time-minutes}")
//    private int defaultExpireTime = 5;
//
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setConnectionFactory(connectionFactory);
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory lettuceConnectionFactory) {
//        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
//        // 设置缓存管理器管理的缓存的默认过期时间
//        defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofMinutes(defaultExpireTime))
////                .entryTtl(Duration.ofMinutes(60))
//                // 设置 key为string序列化
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                // 设置value为json序列化
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//                // 不缓存空值
//                .disableCachingNullValues();
//
//        return RedisCacheManager.builder(lettuceConnectionFactory)
//                .cacheDefaults(defaultCacheConfig)
//                .build();
//    }
//}
////public class RedisConfig implements CachingConfigurer {
////        connectionFactory.setDatabase(2);
////    @Bean
////    public RedisTemplate<Object, Object> redisTemplateAuth(RedisConnectionFactory connectionFactory) {
////        RedisTemplate<Object, Object> redisTemplateAuth = new RedisTemplate<>();
////        redisTemplateAuth.setKeySerializer(new StringRedisSerializer());
//////        connectionFactory.setDatabase(1);
////        redisTemplateAuth.setConnectionFactory(connectionFactory);
////        return redisTemplateAuth;
////    }
//
