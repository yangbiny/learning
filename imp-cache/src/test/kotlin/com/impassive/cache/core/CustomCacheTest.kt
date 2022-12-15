package com.impassive.cache.core

import com.impassive.cache.impl.lettuce.CustomLettuceConnConfig
import com.impassive.cache.impl.lettuce.JsonRedisCodec
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.junit.jupiter.api.Test
import java.time.Duration

/**
 * @author impassive
 */
class CustomCacheTest {

    @Test
    fun test() {
        val build = CustomCacheBuilder(
            expireAfterWrite = Duration.ofMinutes(1),
            lettuceConnCfg = CustomLettuceConnConfig(
                client = RedisClient.create(),
                masterUri = RedisURI.Builder.redis("10.200.68.3", 6379).build(),
                codec = JsonRedisCodec(
                    "prefix", 6, String::class.java, String::class.java
                )
            ),
        ).build()

        val keys = listOf("123", "456")
        build.multiGet(keys)

    }
}