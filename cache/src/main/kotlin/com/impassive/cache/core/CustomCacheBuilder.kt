package com.impassive.cache.core

import com.impassive.cache.config.CustomCacheCommonCfg
import com.impassive.cache.core.decorator.AsyncWriteDecorator
import com.impassive.cache.core.decorator.TailDecorator
import com.impassive.cache.impl.lettuce.CustomLettuceCache
import com.impassive.cache.impl.lettuce.CustomLettuceConnConfig
import java.time.Duration
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author impassive
 */
class CustomCacheBuilder<K, V> constructor(
    private val expireAfterWrite: Duration = Duration.ofMinutes(15),
    private val lettuceConnCfg: CustomLettuceConnConfig<K, V>,
    private val asyncWriteExecutor: ThreadPoolExecutor? = null
) : Validatable {

    fun build(): CustomCache<K, V> {
        validate()

        var cache: CustomCache<K, V> = CustomLettuceCache(
            lettuceConnConfig = lettuceConnCfg,
            customCacheCommonCfg = CustomCacheCommonCfg(expireAfterWrite)
        )
        cache = asyncWriteExecutor?.run {
            AsyncWriteDecorator(cache, this)
        } ?: run {
            cache
        }
        return TailDecorator(cache)
    }

}