package com.example.doctorappointment.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> template;

    public synchronized String getValue(final String key) {

        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template.opsForValue().get(key);
    }

    public void setValue(final String key, final String value, TimeUnit unit, long timeout) {
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.opsForValue().set(key, value);

        template.expire(key, timeout, unit);
    }
}
