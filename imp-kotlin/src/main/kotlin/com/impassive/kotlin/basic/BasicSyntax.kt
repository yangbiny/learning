package com.impassive.kotlin.basic

/**
 * @author impassive
 */

data class Message(
    val sender: String,
    val body: String,
    val isRead: Boolean = false
)

val message = listOf(
    Message("Ma", "Hey!Where are you"),
    Message("Adam", "Every thing Hey!Where are you"),
    Message("Ma", "Please"),
)

fun main() {

    val key = message.groupBy(Message::sender)
        .maxByOrNull { (_, messages) -> messages.size }
        ?.key
    println(key)

}

