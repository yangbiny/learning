package com.impassive.kotlin.basic

import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * @author impassive
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Slf4j {

    companion object {
        val <reified T> T.log: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }

}
