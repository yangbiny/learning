package com.impassive.cache.core.tools

/**
 * @author impassive
 */
object ApiParamValidateToos {

    fun <T> checkNotNull(t: T, message: String): T {
        t ?: run {
            throw RuntimeException(message)
        }
        return t
    }

}