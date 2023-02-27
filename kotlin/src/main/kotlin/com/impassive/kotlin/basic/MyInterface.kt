package com.impassive.kotlin.basic

/**
 * @author impassive
 */
interface MyInterface {

    var prop: Int // 接口里面的属性，为抽线 类型。子类需要 实现

    /**
     * 接口里面的方法
     */
    fun bar() {
        println("MyInterface bar")
    }

    fun foo(func: MyFunInterface): Boolean
}


interface MyInterface2 {
    fun bar() {
        println("bar")
    }
}

/**
 * 实现接口
 */
class MyClass : MyInterface, MyInterface2 {
    override var prop: Int = 29

    // 重写接口的方法
    override fun bar() {
        // 如果多个 接口 有相同的方法，则可以使用 super。但是 指针对 已经 实现了 接口方法的接口
        super<MyInterface2>.bar()
        // 如果只有一个接口实现了，super 里面的 接口可以忽略不写
        super<MyInterface>.bar()
    }

    /**
     * 实现接口的方法
     */
    override fun foo(func: MyFunInterface): Boolean {
        println("foo")
        return func.invoke()
    }


}

/**
 * 函数式接口，一个 接口 只能有一个 抽象方法，可以多 0 个或者 多个 其他方法
 */
fun interface MyFunInterface {

    fun invoke(): Boolean

}

// 扩展方法。如果 MyClass 对象为空。则返回null，否则 返回 toString()的值
fun MyClass?.toString(): String {
    if (this == null) return "null"

    return toString()
}

fun main() {
    val testObj = MyClass()
    // 调用 foo 方法，并且传入 一个 MyFunInterface的实现
    testObj.foo {
        println("test")
        true
    }.let { println(it) }

    testObj.toString()
}