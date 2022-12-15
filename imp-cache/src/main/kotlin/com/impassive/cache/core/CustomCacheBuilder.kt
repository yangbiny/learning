package com.impassive.cache.core

import com.impassive.cache.impl.lettuce.CustomLettuceCache
import com.impassive.cache.impl.lettuce.CustomLettuceConnConfig
import java.time.Duration
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author impassive
 */
class CustomCacheBuilder<K, V>(
    expireAfterWrite: Duration = Duration.ofMinutes(15),
    lettuceConnCfg: CustomLettuceConnConfig<K, V>,
    asyncWriteExecutor: ThreadPoolExecutor?
) {

    fun build(): CustomCache<K, V> {
        return CustomLettuceCache()
    }

}