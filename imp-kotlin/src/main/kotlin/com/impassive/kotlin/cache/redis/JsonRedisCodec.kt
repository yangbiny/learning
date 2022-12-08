package com.impassive.kotlin.cache.redis

import io.lettuce.core.codec.RedisCodec
import java.nio.ByteBuffer

/**
 * @author impassive
 */
class JsonRedisCodec<K, V> : RedisCodec<K, V> {
    override fun decodeKey(bytes: ByteBuffer?): K {
        TODO("Not yet implemented")

    }

    override fun decodeValue(bytes: ByteBuffer?): V {
        TODO("Not yet implemented")
    }

    override fun encodeValue(value: V): ByteBuffer {
        TODO("Not yet implemented")
    }

    override fun encodeKey(key: K): ByteBuffer {
        TODO("Not yet implemented")
    }
}