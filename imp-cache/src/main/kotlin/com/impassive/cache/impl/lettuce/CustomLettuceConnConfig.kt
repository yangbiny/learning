package com.impassive.cache.impl.lettuce

import io.lettuce.core.ReadFrom
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.codec.RedisCodec
import java.util.Objects

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
        if (replicaUris.isEmpty()) {
            return true
        }
        for (replicaUris in replicaUris) {
            if (!Objects.equals(replicaUris, masterUri)) {
                return false
            }
        }
        return true
    }


}