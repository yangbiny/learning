package com.impassive.kotlin.cache.basic

/**
 * @author impassive
 */
interface CustomCache<K, V> {

    fun get(key: K): V

    fun mGet(keyList: Iterable<K>): Map<K, V> {
        val result = hashMapOf<K, V>()
        for (k in keyList) {
            result[k] = get(k)
        }
        return result
    }

    fun put(key: K, value: V)

    fun multiPut(map: Map<K, V>) {
        for (entry in map.entries) {
            put(entry.key, entry.value)
        }
    }

    fun rm(key: K)

    fun multiRm(keyList: Iterable<K>) {
        for (k in keyList) {
            rm(k)
        }
    }

}