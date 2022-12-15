package com.impassive.cache.core.tools

import java.util.*

/**
 * @author impassive
 */
object CacheInnerUtils {

    fun <K, V> filterNullValues(kvMap: MutableMap<K, V>?) {
        kvMap?.let {
            it.entries.removeIf { entry -> isNull(entry.value) }
        }
    }

    private fun isNull(obj: Any?): Boolean {
        return obj?.run {
            Objects.equals(this.toString().lowercase(), "null")
        } ?: run {
            true
        }
    }

    fun <K, V> getMissKeys(hitMap: MutableMap<K, V>?, keys: Collection<K>?): Set<K> {
        val hitSize = hitMap?.size ?: 0
        if (hitSize == 0) {
            return keys?.toSet() ?: emptySet()
        }
        val keySize = keys?.size ?: 0
        if (keySize == hitSize) {
            return emptySet()
        }
        return keys?.run {
            this.stream().filter { item -> hitMap?.contains(item) ?: false }.toList().toSet()
        } ?: emptySet()
    }

}