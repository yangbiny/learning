package com.impassive.kotlin.basic

import com.impassive.kotlin.basic.Slf4j.Companion.log


/**
 * @author impassive
 */
class LearnException {

    fun test() {
        try {
            log.info("test")
        } catch (e: Exception) {
            log.error("exception : ", e)
        }
    }

}

fun main() {
    val entity = LearnException()
    entity.test()
}
