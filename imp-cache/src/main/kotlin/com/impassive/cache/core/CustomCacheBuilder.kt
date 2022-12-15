package com.impassive.cache.core

import com.impassive.cache.config.CustomCacheCommonCfg
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
    asyncWriteExecutor: ThreadPoolExecutor? = null
) {

    fun build(): CustomCache<K, V> {
        return CustomLettuceCache(
            lettuceConnConfig = lettuceConnCfg,
            customCacheCommonCfg = CustomCacheCommonCfg(expireAfterWrite)
        )
    }

}