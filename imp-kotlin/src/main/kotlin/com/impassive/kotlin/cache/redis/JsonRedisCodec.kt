package com.impassive.kotlin.cache.redis

import com.impassive.kotlin.tools.JsonTools
import io.lettuce.core.codec.RedisCodec
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.Objects

/**
 * @author impassive
 */
class JsonRedisCodec<K, V>(
    private val keyPrefix: String,
    private val keyPrefixLength: Int,
    private val keyClass: Class<K>,
    private val valueClass: Class<V>
) : RedisCodec<K, V> {

    companion object {
        private val EMPTY = byteArrayOf()
    }

    private fun getBytes(bytes: ByteBuffer?): ByteArray? {
        return bytes?.run {
            val remaining = bytes.remaining()
            if (remaining == 0) {
                return EMPTY
            }
            val byteArray = byteArrayOf()
            bytes.get(byteArray)
            byteArray
        }
    }

    override fun decodeKey(bytes: ByteBuffer?): K? {
        val bytesResult = getBytes(bytes)
        return bytesResult?.run {
            val string = String(bytesResult)
            val substring = string.substring(IntRange(0, keyPrefixLength))
            JsonTools.fromJson(substring, keyClass)
        }
    }

    override fun decodeValue(bytes: ByteBuffer?): V? {
        val bytesResult = getBytes(bytes)
        return bytesResult?.run {
            val string = String(bytesResult)
            JsonTools.fromJson(string, valueClass)
        }
    }

    override fun encodeValue(value: V): ByteBuffer {
        val jsonStr = JsonTools.toJson(value);
        return ByteBuffer.wrap(jsonStr.toByteArray(StandardCharsets.UTF_8))
    }

    override fun encodeKey(key: K): ByteBuffer {
        val keyStr = JsonTools.toJson(key)
        val finalStr = keyPrefix + keyStr;
        return ByteBuffer.wrap(finalStr.toByteArray(StandardCharsets.UTF_8))
    }
}

