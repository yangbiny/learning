package com.impassive.kotlin.cache

import com.impassive.kotlin.cache.basic.CustomCache
import com.impassive.kotlin.cache.redis.CustomLettuceConnConfig
import com.impassive.kotlin.cache.redis.JsonRedisCodec
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

/**
 * @author impassive
 */
internal class CustomBuilderTest {

    private var redisCache: CustomCache<Long, Item>? = null

    @BeforeEach
    fun setUp() {
        val connConfig = CustomLettuceConnConfig<Long, Item>(
            client = RedisClient.create(),
            masterUri = RedisURI.Builder.redis("").build(),
            codec = JsonRedisCodec()
        )

        redisCache = CustomBuilder<Long, Item>(Duration.ofMinutes(15))
            .build()

        println(redisCache)
    }

    @Test
    internal fun test() {
        redisCache?.get(1L)
    }

    class Item {

    }
}