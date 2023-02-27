package com.impassive.kotlin.cache.redis

import com.impassive.kotlin.cache.basic.CustomCache
import io.lettuce.core.api.sync.RedisCommands
import java.time.Duration

/**
 * @author impassive
 */
class CustomRedisCache<K, V>(
    redisConfig: CustomLettuceConnConfig<K, V>,
    private val expireAfterWrite: Duration
) : CustomCache<K, V> {

    private val customLettuceConn = CustomLettuceConn(redisConfig)

    private val sync: RedisCommands<K, V> = customLettuceConn.conn.sync()


    override fun get(key: K): V {
        return sync.get(key)
    }

    override fun put(key: K, value: V) {
        key?.let {
            sync.setex(key, expireAfterWrite.toSeconds(), value)
        }
    }

    override fun rm(key: K) {
        key?.let {
            sync.del(key)
        }
    }

    override fun close() {
        customLettuceConn.close()
    }


}
