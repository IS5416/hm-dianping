package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


@Component
public class RedisIdWorker {

    // 初始时间戳
    private static final long BEGIN_TIMESTAMP = 1768262400L;
    // 序列号位数
    private static final int COUNT_BITS = 32;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.of(2026, 1, 13, 0, 0, 0);
        long second = time.toEpochSecond(ZoneOffset.UTC);
        System.out.println("second: " + second);

    }

    // 生成全局唯一id
    public long nextId(String keyPrefix) {
        // 当前时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        // 31位bit时间戳
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 32位bit序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd")); // 当前日期
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date); // 自增长
        // 拼接
        return (timestamp << COUNT_BITS) | count;
    }

}
