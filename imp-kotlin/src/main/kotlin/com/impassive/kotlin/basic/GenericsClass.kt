package com.impassive.kotlin.basic

/**
 * @author impassive
 */
// 定义 范型类
class GenericsClass<T> {

    // 定义一个可变的List。  直接 是 List 是不可变的
    private var value: MutableList<T> = mutableListOf()

    fun add(args: T): Boolean {
        return value.add(args)
    }
}

// vararg 是可变数量的参数
// 范型方法
fun <T> genericsMethod(args: Class<T>): T {
    val constructor = args.getConstructor()
    return constructor.newInstance()
}

fun main() {

    // 需要指定 范型类型。如果有主构造函数，可以推断，则无需指定
    val testClass = GenericsClass<String>()

    // 也可以这样指定 范型类型。后面构造 函数 处的范型 可写 可忽略
    val testClass2: GenericsClass<String> = GenericsClass();

    genericsMethod(testClass.javaClass)

}