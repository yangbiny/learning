package com.impassive.cache.core.exception

/**
 * @author impassive
 */
open class CustomCacheException(e: Exception) : RuntimeException(e) {


    companion object {

        fun fromOtherException(e: Exception): CustomCacheException {
            if (e is CustomCacheException) {
                return e
            }

            return CustomCacheException(e)
        }


    }

}
