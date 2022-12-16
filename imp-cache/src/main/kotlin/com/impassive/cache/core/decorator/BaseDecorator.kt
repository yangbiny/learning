package com.impassive.cache.core.decorator

import com.impassive.cache.core.CustomCache
import com.impassive.cache.core.tools.ApiParamValidateToos

/**
 * @author impassive
 */
sealed class BaseDecorator<K, V>(
    private val cache: CustomCache<K, V>
) : CustomCache<K, V> {

    private fun checkCollectionNotNull(k: Collection<K>): Collection<K> {
        ApiParamValidateToos.checkNotNull(k, "key can not be null")
        k.run {
            for (k in this) {
                ApiParamValidateToos.checkNotNull(k, "key can not be null")
            }
        }
        return k
    }

    private fun checkKeyNotNull(k: K): K {
        return ApiParamValidateToos.checkNotNull(k, "key can not be null")
    }

    private fun checkValueNotNull(v: V): V {
        return ApiParamValidateToos.checkNotNull(v, "value can not be null")
    }

    override fun get(k: K): V? {
        return cache.get(checkKeyNotNull(k))
    }

    override fun put(k: K, v: V) {
        cache.put(checkKeyNotNull(k), checkValueNotNull(v))
    }

    override fun rm(key: K) {
        cache.rm(checkKeyNotNull(key))
    }

    override fun multiGet(keys: Collection<K>): Map<K, V> {
        return cache.multiGet(checkCollectionNotNull(keys))
    }

    override fun close() {
        cache.close()
    }
}
