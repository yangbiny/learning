package com.impassive.kotlin.cache.decoration

/**
 * @author impassive
 */
class FallbackDecoration(
    var value: FallbackDecoration?
) {

    var cnt = 1
        private set


    fun failed() {
        this.value?.let {
            this.cnt = this.cnt + 1
        }
    }


}