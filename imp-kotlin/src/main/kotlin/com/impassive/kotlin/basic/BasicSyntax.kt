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

