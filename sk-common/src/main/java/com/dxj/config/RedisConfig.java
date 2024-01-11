package com.dxj.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.collections.RedisProperties;

@Configuration
public class RedisConfig {
    /**
     * RedisTemplate可以接收任意Object作为值写入Redis，只不过写入前会把Object序列化为字节形式，默认是采用JDK序列化，得到的一串很长的值
     * 缺点：可读性查、浪费存储空间
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//		1.创建 redisTemplate
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//		2.设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//		3.设置序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//		key 和 hashkey 采用 String 序列化
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setKeySerializer(RedisSerializer.string());
//		value 和 hashvalue 采用 json 序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        return  redisTemplate;
    };
}

//@Slf4j
//@Configuration
//@EnableCaching
//@ConditionalOnClass(RedisOperations.class)
//@EnableConfigurationProperties(RedisProperties.class)
//@SuppressWarnings("all")
//public class RedisConfig extends CachingConfigurerSupport {
//
//    /**
//     * 设置 redis 数据默认过期时间，默认2小时
//     * 设置@cacheable 序列化方式
//     */
//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration() {
//        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
//        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer)).entryTtl(Duration.ofHours(2));
//        return configuration;
//    }
//
//    @Bean(name = "redisTemplate")
//    @ConditionalOnMissingBean(name = "redisTemplate")
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        //序列化
//        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//        // value值的序列化采用fastJsonRedisSerializer
//        template.setValueSerializer(fastJsonRedisSerializer);
//        template.setHashValueSerializer(fastJsonRedisSerializer);
//        // 全局开启AutoType，这里方便开发，使用全局的方式
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//        // 建议使用这种方式，小范围指定白名单
//        // ParserConfig.getGlobalInstance().addAccept("com.dxj.domain");
//        // key的序列化采用StringRedisSerializer
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setConnectionFactory(redisConnectionFactory);
//        return template;
//    }
//
//    /**
//     * 自定义缓存key生成策略，默认将使用该策略
//     */
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return (target, method, params) -> {
//            Map<String, Object> container = new HashMap<>(3);
//            Class<?> targetClassClass = target.getClass();
//            // 类地址
//            container.put("class", targetClassClass.toGenericString());
//            // 方法名称
//            container.put("methodName", method.getName());
//            // 包名称
//            container.put("package", targetClassClass.getPackage());
//            // 参数列表
//            for (int i = 0; i < params.length; i++) {
//                container.put(String.valueOf(i), params[i]);
//            }
//            // 转为JSON字符串
//            String jsonString = JSON.toJSONString(container);
//            // 做SHA256 Hash计算，得到一个SHA256摘要作为Key
//            return DigestUtils.sha256Hex(jsonString);
//        };
//    }
//
//    @Bean
//    @Override
//    public CacheErrorHandler errorHandler() {
//        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
//        log.info("初始化 -> [{}]", "Redis CacheErrorHandler");
//        return new CacheErrorHandler() {
//            @Override
//            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
//                log.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
//            }
//
//            @Override
//            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
//                log.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
//            }
//
//            @Override
//            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
//                log.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
//            }
//
//            @Override
//            public void handleCacheClearError(RuntimeException e, Cache cache) {
//                log.error("Redis occur handleCacheClearError：", e);
//            }
//        };
//    }
//
//    /**
//     * Value 序列化
//     *
//     * @param <T>
//     * @author /
//     */
//    private static class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
//
//        private Class<T> clazz;
//
//        FastJsonRedisSerializer(Class<T> clazz) {
//            super();
//            this.clazz = clazz;
//        }
//
//        @Override
//        public byte[] serialize(T t) {
//            if (t == null) {
//                return new byte[0];
//            }
//            return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
//        }
//
//        @Override
//        public T deserialize(byte[] bytes) {
//            if (bytes == null || bytes.length <= 0) {
//                return null;
//            }
//            String str = new String(bytes, StandardCharsets.UTF_8);
//            return JSON.parseObject(str, clazz);
//        }
//
//    }
//
//    /**
//     * 重写序列化器
//     *
//     * @author /
//     */
//    private static class StringRedisSerializer implements RedisSerializer<Object> {
//
//        private final Charset charset;
//
//        StringRedisSerializer() {
//            this(StandardCharsets.UTF_8);
//        }
//
//        private StringRedisSerializer(Charset charset) {
//            Assert.notNull(charset, "Charset must not be null!");
//            this.charset = charset;
//        }
//
//        @Override
//        public String deserialize(byte[] bytes) {
//            return (bytes == null ? null : new String(bytes, charset));
//        }
//
//        @Override
//        public byte[] serialize(Object object) {
//            String string = JSON.toJSONString(object);
//            if (StringUtils.isBlank(string)) {
//                return null;
//            }
//            string = string.replace("\"", "");
//            return string.getBytes(charset);
//        }
//    }
//}
//
//
