package com.impassive.kotlin.basic

/**
 * @author impassive
 */
class ClassConstructor(
    name: String
) {
    val test: MutableCollection<ClassConstructor> = mutableListOf()

    // 初始化代码块。会 比 二级构造函数先执行，比 主构造函数 后执行
    init {
        println("init : $name")
    }

    // 二级构造函数。
    // 二级构造函数 必须 直接或者间接（通过其他构造函数） 调用 主构造函数，使用 this 的方式调用主构造函数
    constructor(name: String, age: Int) : this(name) {
        println("constructor : $age, $name")
        test.add(this)
    }

    // 二级构造函数，通过 this(name,age)调用了 主构造函数
    constructor(name: String, age: Int, cnt: Int) : this(name, age) {

    }
}

fun main() {
    ClassConstructor("test", 1).let {
        println(it.test)
    }
}