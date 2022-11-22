package com.impassive.kotlin.basic

fun main() {
    val numbers = mutableListOf("one", "two", "three", "four", "five")

    val result = numbers.map { it.length }.filter { it > 3 }
    println(result)

    numbers.map { it.length }.filter { it > 3 }.forEach { println(it) }
    numbers.map { it.length }.filter { it > 6 }?.let(::println)

}