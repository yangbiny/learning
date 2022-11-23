package com.impassive.kotlin.basic

class ScopeData(
    val name: String,
    val cnt: Int
) {
    fun changeCnt() {
    }
}

fun main() {
    val numbers = mutableListOf("one", "two", "three", "four", "five")

    val result = numbers.map { it.length }.filter { it > 3 }
    println(result)

    ScopeData("test", 1).let {
        it.changeCnt()
    }
}