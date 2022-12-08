package com.impassive.kotlin.tools

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author impassive
 */
class JsonTools {

    companion object {

        private val objectMapper = ObjectMapper()

        fun <T> fromJson(jsonString: String, classType: Class<T>): T {
            return objectMapper.readValue(jsonString, classType)
        }

        fun <T> toJson(value: T): String {
            return objectMapper.writeValueAsString(value)
        }
    }
}