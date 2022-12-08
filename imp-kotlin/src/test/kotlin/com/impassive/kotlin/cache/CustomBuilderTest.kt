package com.impassive.kotlin.cache

import com.impassive.kotlin.cache.basic.CustomCache
import com.impassive.kotlin.cache.redis.CustomLettuceConnConfig
import com.impassive.kotlin.cache.redis.JsonRedisCodec
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate

/**
 * @author impassive
 */
internal class CustomBuilderTest {

    private var redisCache: CustomCache<Long, Item>? = null

    @BeforeEach
    fun setUp() {
        val connConfig = CustomLettuceConnConfig(
            client = RedisClient.create(),
            masterUri = RedisURI.Builder.redis("10.200.68.3", 6379).build(),
            codec = JsonRedisCodec(
                "prefix", 6, Long::class.java, Item::class.java
            )
        )

        redisCache = CustomBuilder<Long, Item>(Duration.ofMinutes(15))
            .cfg(connConfig)
            .build()

    }

    @Test
    internal fun test() {
        val item = redisCache?.get(1L)
        println(item)
    }

    @Test
    internal fun testSave() {
        redisCache?.put(1L, Item("test"))
    }

    class Item(
        val key: String
    ) {

    }
}