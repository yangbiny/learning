package com.impassive.kotlin.cache.decoration

import com.impassive.kotlin.cache.basic.CustomCache

/**
 * @author impassive
 */
sealed class BasicDecoration<K, V>(
    private val customCache: CustomCache<K, V>
) : CustomCache<K, V> {


    private fun <T> checkNotNull(value: T): T {
        assert(value != null)
        return value
    }


    override fun mGet(keyList: Iterable<K>): Map<K, V> {
        return customCache.mGet(keyList)
    }

    override fun multiPut(map: Map<K, V>) {
        customCache.multiPut(map)
    }

    override fun multiRm(keyList: Iterable<K>) {
        customCache.multiRm(keyList)
    }

    override fun get(key: K): V {
        return customCache.get(checkNotNull(key))
    }

    override fun put(key: K, value: V) {
        return customCache.put(checkNotNull(key), checkNotNull(value))
    }

    override fun rm(key: K) {
        customCache.rm(checkNotNull(key))
    }

    override fun close() {
        customCache.close()
    }
}