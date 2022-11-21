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

    var s1 = 1
    val s2 = 2
    var test = "test is ${s1 + s2}"
    println(test)

    val key = message.groupBy(Message::sender)
        .maxByOrNull { (_, messages) -> messages.size }
        ?.key
    println(key)

    val names = message
        .asSequence()
        .filter { it.body.isNotBlank() && !it.isRead }
        .map(Message::sender)
        .distinct()
        .sorted()
        .toList()
    println(names)

    val obj = RestaurantCustomer("test", "dish")
    obj.greet()
    obj.order()
    obj.pay(10)
    obj.eat()

}

class RestaurantCustomer(name: String, val dish: String) : Person(name), FoodConsumer {

    fun order() = println("$dish, please")

    override fun greet() {
        println("greet")
    }

    override fun eat() {
        println("eat")
    }

}

abstract class Person(val name: String) {
    abstract fun greet()
}

interface FoodConsumer {
    fun eat()

    fun pay(amount: Int) = println("default")
}

