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

    fun foo()
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
    override fun foo() {
        TODO("Not yet implemented")
    }

}