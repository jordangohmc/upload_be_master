package com.mcr.framework.config.cache;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties(RedissonProperties.class)
public class RedisConfig {
    @Resource
    private RedissonProperties redissonProperties;
    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public RedissonAutoConfigurationCustomizer redissonCustomizer() {
        return config -> {
            config.setThreads(redissonProperties.getThreads())
                    .setNettyThreads(redissonProperties.getNettyThreads())
                    .setCodec(new JsonJacksonCodec(objectMapper));
            RedissonProperties.SingleServerConfig singleServerConfig = redissonProperties.getSingleServerConfig();
            if (ObjectUtil.isNotNull(singleServerConfig)) {
                // 使用单机模式
                config.useSingleServer()
                        //设置redis key前缀
                        .setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()))
                        .setTimeout(singleServerConfig.getTimeout())
                        .setClientName(singleServerConfig.getClientName())
                        .setIdleConnectionTimeout(singleServerConfig.getIdleConnectionTimeout())
                        .setSubscriptionConnectionPoolSize(singleServerConfig.getSubscriptionConnectionPoolSize())
                        .setConnectionMinimumIdleSize(singleServerConfig.getConnectionMinimumIdleSize())
                        .setConnectionPoolSize(singleServerConfig.getConnectionPoolSize());
            }
            // 集群配置方式 参考下方注释
            RedissonProperties.ClusterServersConfig clusterServersConfig = redissonProperties.getClusterServersConfig();
            if (ObjectUtil.isNotNull(clusterServersConfig)) {
                config.useClusterServers()
                        //设置redis key前缀
                        .setNameMapper(new KeyPrefixHandler(redissonProperties.getKeyPrefix()))
                        .setTimeout(clusterServersConfig.getTimeout())
                        .setClientName(clusterServersConfig.getClientName())
                        .setIdleConnectionTimeout(clusterServersConfig.getIdleConnectionTimeout())
                        .setSubscriptionConnectionPoolSize(clusterServersConfig.getSubscriptionConnectionPoolSize())
                        .setMasterConnectionMinimumIdleSize(clusterServersConfig.getMasterConnectionMinimumIdleSize())
                        .setMasterConnectionPoolSize(clusterServersConfig.getMasterConnectionPoolSize())
                        .setSlaveConnectionMinimumIdleSize(clusterServersConfig.getSlaveConnectionMinimumIdleSize())
                        .setSlaveConnectionPoolSize(clusterServersConfig.getSlaveConnectionPoolSize())
                        .setReadMode(clusterServersConfig.getReadMode())
                        .setSubscriptionMode(clusterServersConfig.getSubscriptionMode());
            }
            log.info("初始化 redis 配置");
        };
    }

    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager() {
        return new PlusSpringCacheManager();
    }


    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }


//    @Value("${cache.default.expire-time-minutes}")
//    private int defaultExpireTime;
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        // 默认的缓存配置
//        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(defaultExpireTime)) // 默认过期时间
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//                .disableCachingNullValues();
//
//        // 创建缓存写入器
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
//        // 创建 RedisCacheManager Builder
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisCacheWriter)
//                .cacheDefaults(defaultCacheConfig); // 默认配置
//        // 解析并添加特定缓存配置
//        for (String cacheName : getAllCacheNames()) {
//            // 解析缓存名称和过期时间
//            String[] parts = cacheName.split("#");
//            String name = parts[0];
//            Duration ttl = defaultCacheConfig.getTtl(); // 默认 TTL
//
//            if (parts.length > 1) {
//                String timePart = parts[1];
//                ttl = parseDuration(timePart); // 解析过期时间
//            }
//            // 为特定缓存添加配置
//            builder.withCacheConfiguration(name, RedisCacheConfiguration.defaultCacheConfig()
//                    .entryTtl(ttl)
//                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//                    .disableCachingNullValues());
//        }
//        return builder.build();
//    }
//
//    private Duration parseDuration(String timePart) {
//        // 提取时间单位的最后一个字符
//        char unit = timePart.charAt(timePart.length() - 1);
//        // 提取数字部分
//        long value = Long.parseLong(timePart.substring(0, timePart.length() - 1));
//
//        return switch (unit) {
//            case 's' -> Duration.ofSeconds(value);
//            case 'm' -> Duration.ofMinutes(value);
//            case 'h' -> Duration.ofHours(value);
//            case 'd' -> Duration.ofDays(value);
//            default -> Duration.ofMinutes(defaultExpireTime); // 默认值
//        };
//    }
//
//    public static String[] getAllCacheNames() {
//        Field[] fields = CacheNames.class.getDeclaredFields();
//        List<String> cacheNames = new ArrayList<>();
//        for (Field field : fields) {
//            if (field.getType().equals(String.class)) {
//                try {
//                    cacheNames.add((String) field.get(null)); // 获取静态字段的值
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return cacheNames.toArray(new String[0]);
//    }
}
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        // 默认的缓存配置
//        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(defaultExpireTime)) // 默认过期时间
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//                .disableCachingNullValues();
//
//        // 特定缓存的过期时间为 3 小时
//        RedisCacheConfiguration tenantSettingCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofHours(3))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//                .disableCachingNullValues();
//
//        // 创建缓存写入器
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
//
//        // 创建缓存管理器
//        return RedisCacheManager.builder(redisCacheWriter)
//                .cacheDefaults(defaultCacheConfig) // 默认配置
//                .withCacheConfiguration(CacheNames.TENANT_SETTING, tenantSettingCacheConfig) // 指定 TENANT_SETTING 的配置
//                .build();
//    }


//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(defaultCacheConfig)
//                .withCacheConfiguration(CacheNames.TENANT_SETTING, longCacheConfig)
//                .build();


//public class RedisConfig implements CachingConfigurer {
//        connectionFactory.setDatabase(2);
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplateAuth(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Object, Object> redisTemplateAuth = new RedisTemplate<>();
//        redisTemplateAuth.setKeySerializer(new StringRedisSerializer());
////        connectionFactory.setDatabase(1);
//        redisTemplateAuth.setConnectionFactory(connectionFactory);
//        return redisTemplateAuth;
//    }

