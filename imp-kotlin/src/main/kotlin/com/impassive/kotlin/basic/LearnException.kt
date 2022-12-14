package com.impassive.kotlin.basic

import com.impassive.kotlin.basic.Slf4j.Companion.log
import org.slf4j.Logger
import org.slf4j.LoggerFactory


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

class LoggerTester {

    companion object {
        val log: Logger = LoggerFactory.getLogger(LoggerTester::class.java)
    }



}

fun main() {
    LoggerTester.log.error("error")
    val entity = LearnException()
    entity.test()
}
