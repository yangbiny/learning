package com.impassive.kotlin.cache.redis

import io.lettuce.core.ReadFrom
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.codec.RedisCodec

/**
 * @author impassive
 */
data class CustomLettuceConnConfig<K, V>(
    val client: RedisClient,
    val readFrom: ReadFrom = ReadFrom.ANY,
    val masterUri: RedisURI,
    val replicaUris: List<RedisURI> = listOf(),
    val codec: RedisCodec<K, V>
) {
    fun onlySingleNode(): Boolean {
        TODO("Not yet implemented")
    }


}