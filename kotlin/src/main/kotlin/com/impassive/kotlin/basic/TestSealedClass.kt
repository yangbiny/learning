package com.impassive.kotlin.basic

/**
 * @author impassive
 */
class TestSealedClass : SealedClass() {
    override fun test(arg: String) {
        println(arg)
    }
}