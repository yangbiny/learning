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

    // 根据 sender 变量进行分组
    val key = message.groupBy(Message::isRead)
        .maxByOrNull { (_, value) -> value.size } // 根据 分组 后的 数量进行比较。前面 的 _ 表示的是 忽略了 对应的值，此处代表的是map的key
        ?.key // 获取 对应的 key。 如果没有对应的 值，则返回null
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

