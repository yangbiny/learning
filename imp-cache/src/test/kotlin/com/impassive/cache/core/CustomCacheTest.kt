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
                    keyPrefix = "prefix_",
                    keyClass = String::class.java,
                    valueClass = String::class.java
                )
            ),
        ).build()



        val valueMap = mapOf(Pair("123", "123"), Pair("456", "456"), Pair("789", "789"))

        build.multiPut(valueMap)

        val keys = mutableListOf("123", "456", "101")
        val multiGet = build.multiGet(keys)
        println(multiGet)

        build.multiRm(keys)

        val load = build.load("101", this::func)
        println(load)

        keys.add("102")
        val multiLoad = build.multiLoad(keys, this::func, null)
        println(multiLoad)

    }

    private fun func(key: String): String {
        return key
    }

    private fun func(key: Collection<String>): Map<String, String> {
        return key.associateWith { it }
    }
}