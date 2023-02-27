package com.impassive.cache.core

import com.impassive.cache.core.exception.CustomCacheException
import com.impassive.cache.core.tools.CacheInnerUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.lang.Exception
import java.util.Collections
import java.util.function.Function
import java.util.stream.Collectors

/**
 * @author impassive
 */
interface CustomCache<K, V> : Closeable {

    companion object {
        val log: Logger = LoggerFactory.getLogger(CustomCache::class.java)
    }

    fun load(k: K, loader: Function<K, V>): V? {
        return load(k, loader, null)
    }

    fun load(k: K, loader: Function<K, V>, fallback: Function<K, V>?): V? {
        try {
            val result: V?
            try {
                result = this.get(k)
            } catch (e: Exception) {
                log.error("load value from cache has failed, try to get from loader", e)
                return loader.apply(k)
            }
            return result?.run {
                result
            } ?: run {
                loader.apply(k)?.run {
                    put(k, this)
                    this
                }
            }
        } catch (e: Exception) {
            return fallback?.run {
                this.apply(k)
            } ?: let {
                throw CustomCacheException.fromOtherException(e)
            }
        }
    }

    fun multiLoad(
        keys: Collection<K>,
        multiLoader: Function<Collection<K>, Map<K, V>>,
        fallback: Function<K, V>?
    ): Map<K, V> {

        try {
            if (keys.isEmpty()) {
                return Collections.emptyMap()
            }
            val distinctKeys = keys.stream().distinct().toList()
            val result: Map<K, V>?

            try {
                result = multiGet(distinctKeys)
            } catch (e: Exception) {
                log.error("multi get has failed,try to use apply : ", e)
                return multiLoader.apply(distinctKeys)
            }
            val mutableMap = result.toMutableMap()
            CacheInnerUtils.filterNullValues(mutableMap)

            val missKeys = CacheInnerUtils.getMissKeys(mutableMap, distinctKeys)
            if (missKeys.isEmpty()) {
                return mutableMap
            }
            val missMap = multiLoader.apply(missKeys)
            multiPut(missMap)
            return mutableMap + missMap
        } catch (e: Exception) {
            return fallback?.run {
                keys.stream().collect(Collectors.toMap(Function.identity(), this::apply))
            } ?: let {
                throw CustomCacheException(e)
            }
        }
    }

    fun get(k: K): V?

    fun put(k: K, v: V)

    fun rm(key: K)

    fun multiGet(keys: Collection<K>): Map<K, V>

    fun multiPut(map: Map<K, V>) {
        map.forEach { item -> put(item.key, item.value) }
    }

    fun multiRm(keys: Collection<K>) {
        keys.forEach { item -> rm(item) }
    }

}