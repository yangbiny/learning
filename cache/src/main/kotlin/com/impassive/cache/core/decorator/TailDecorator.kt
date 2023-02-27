package com.impassive.cache.core.decorator

import com.impassive.cache.core.CustomCache
import com.impassive.cache.core.exception.CustomCacheException
import java.lang.Exception
import java.util.function.Function

/**
 * @author impassive
 */
class TailDecorator<K, V>(cache: CustomCache<K, V>) : BaseDecorator<K, V>(cache) {

    override fun get(k: K): V? {
        try {
            return super.get(k)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun put(k: K, v: V) {
        try {
            super.put(k, v)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun rm(key: K) {
        try {
            super.rm(key)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun multiGet(keys: Collection<K>): Map<K, V> {
        try {
            return super.multiGet(keys)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun load(k: K, loader: Function<K, V>): V? {
        try {
            return super.load(k, loader)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun load(k: K, loader: Function<K, V>, fallback: Function<K, V>?): V? {
        try {
            return super.load(k, loader, fallback)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun multiLoad(
        keys: Collection<K>,
        multiLoader: Function<Collection<K>, Map<K, V>>,
        fallback: Function<K, V>?
    ): Map<K, V> {
        try {
            return super.multiLoad(keys, multiLoader, fallback)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun multiPut(map: Map<K, V>) {
        try {
            super.multiPut(map)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }

    override fun multiRm(keys: Collection<K>) {
        try {
            super.multiRm(keys)
        } catch (e: Exception) {
            throw CustomCacheException.fromOtherException(e)
        }
    }
}