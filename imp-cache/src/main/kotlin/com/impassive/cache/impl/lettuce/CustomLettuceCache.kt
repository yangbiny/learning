package com.impassive.cache.impl.lettuce

import com.impassive.cache.config.CustomCacheCommonCfg
import com.impassive.cache.core.CustomCache
import io.lettuce.core.api.sync.RedisCommands

/**
 * @author impassive
 */
class CustomLettuceCache<K, V>(
    lettuceConnConfig: CustomLettuceConnConfig<K, V>,
    private val customCacheCommonCfg: CustomCacheCommonCfg
) : CustomCache<K, V> {

    private val conn: CustomLettuceConn<K, V>

    private val redisCommands: RedisCommands<K, V>

    init {
        conn = CustomLettuceConn(lettuceConnConfig)
        redisCommands = conn.conn.sync()
    }

    override fun get(k: K): V? {
        return redisCommands.get(k)
    }

    override fun put(k: K, v: V) {
        redisCommands.setex(k, customCacheCommonCfg.ttlAfterWrite.seconds, v)
    }

    override fun rm(key: K) {
        redisCommands.del(key)
    }

    override fun multiGet(keys: Collection<K>): Map<K, V> {
        val ks: Array<K?> = collectionsToArray(keys)
        return redisCommands.mget(*ks)
            .stream()
            .filter { item -> item.hasValue() }
            .toList()
            .associate {
                Pair(it.key, it.value)
            }
    }

    override fun multiRm(keys: Collection<K>) {
        val keyArr = collectionsToArray(keys)
        redisCommands.del(*keyArr)
    }

    private fun collectionsToArray(keys: Collection<K>): Array<K?> {
        val array = keys.stream().toArray()
        @Suppress("UNCHECKED_CAST")
        return array as Array<K?>
    }

    override fun close() {
        conn.close()
    }

}