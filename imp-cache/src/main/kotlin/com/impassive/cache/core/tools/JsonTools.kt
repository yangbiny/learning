package com.impassive.cache.core.tools

import com.fasterxml.jackson.databind.ObjectMapper

object JsonTools {


    private val objectMapper = ObjectMapper()

    fun <T> fromJson(jsonString: String, classType: Class<T>): T {
        return objectMapper.readValue(jsonString, classType)
    }

    fun <T> toJson(value: T): String {
        return objectMapper.writeValueAsString(value)
    }
}