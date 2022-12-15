package com.impassive.cache.impl.lettuce

import com.impassive.cache.core.CustomCache

/**
 * @author impassive
 */
class CustomLettuceCache<K, V>(


) : CustomCache<K, V> {
    override fun get(k: K): V? {
        TODO("Not yet implemented")
    }

    override fun put(k: K, v: V) {
        TODO("Not yet implemented")
    }

    override fun rm(key: K) {
        TODO("Not yet implemented")
    }

    override fun multiGet(keys: Collection<K>): Map<K, V> {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}