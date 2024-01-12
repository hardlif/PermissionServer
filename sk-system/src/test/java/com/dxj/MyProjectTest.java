package com.dxj;

import com.dxj.SkApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SkApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyProjectTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testone(){
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.opsForValue().set("agezw", "18");

        Object age = redisTemplate.opsForValue().get("agezw");
        System.out.println("agezw = " + age);
    }
}
