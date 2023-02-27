package com.impassive.kotlin.cache

import com.impassive.kotlin.cache.basic.CustomCache
import com.impassive.kotlin.cache.redis.CustomLettuceConnConfig
import com.impassive.kotlin.cache.redis.CustomRedisCache
import java.time.Duration
import java.util.function.Function

/**
 * @author impassive
 */
class CustomBuilder<K, V> constructor(
    private val expireAfterWrite: Duration = Duration.ofMinutes(15)
) {

    private var cfg: CustomLettuceConnConfig<K, V>? = null

    /**
     * 执行失败的 回调函数
     */
    var fallback: Function<K, V>? = null

    /**
     * 如果从缓存获取失败，如何加载数据
     */
    var loader: Function<K, V>? = null

    fun cfg(cfg: CustomLettuceConnConfig<K, V>): CustomBuilder<K, V> {
        this.cfg = cfg
        return this
    }

    fun build(): CustomCache<K, V> {
        val redisCache = cfg?.let {
            CustomRedisCache(cfg!!, expireAfterWrite)
        }
        return redisCache!!
    }
}