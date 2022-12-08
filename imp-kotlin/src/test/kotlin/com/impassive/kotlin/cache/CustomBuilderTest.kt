package com.impassive.kotlin.cache

import com.impassive.kotlin.cache.redis.CustomLettuceConnConfig
import com.impassive.kotlin.cache.redis.JsonRedisCodec
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.junit.jupiter.api.BeforeEach
import java.time.Duration

/**
 * @author impassive
 */
internal class CustomBuilderTest {

    @BeforeEach
    fun setUp() {
        val connConfig = CustomLettuceConnConfig<Long, Item>(
            client = RedisClient.create(),
            masterUri = RedisURI.Builder.redis("").build(),
            codec = JsonRedisCodec()
        )

        val customCache = CustomBuilder<Long, Item>(Duration.ofMinutes(15))
            .cfg(connConfig)
            .build()


    }


    class Item {

    }
}