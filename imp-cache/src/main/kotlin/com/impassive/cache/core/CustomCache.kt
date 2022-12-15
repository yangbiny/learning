package com.impassive.cache.core

import com.impassive.cache.core.exception.CustomCacheException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.function.Function

/**
 * @author impassive
 */
interface CustomCache<K, V> : Cloneable {

    companion object {
        val log: Logger = LoggerFactory.getLogger(CustomCache::class.java)
    }

    fun load(k: K, loader: Function<K, V>): V? {
        return load(k, loader, null)
    }

    fun load(k: K, loader: Function<K, V>, fallback: Function<K, V>?): V? {
        try {
            var result: V?
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

    fun get(k: K): V?

    fun put(k: K, v: V)

}