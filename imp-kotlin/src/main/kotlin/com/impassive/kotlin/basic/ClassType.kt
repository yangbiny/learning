package com.impassive.kotlin.basic

/**
 * @author impassive
 */
class ClassType {
}

/**
 * 数据类：主要是 用作数据传输，会自动生成 equals 和 hashCode
 *
 * <p> equals 和 hashCode 只会使用主构造函数中的对象生成 </p>
 *
 * <p>主构造函数必须 至少 有一个变量</p>
 */
data class DataClass(val name: String, val cnt: Int) {

    // 不参与 equals 和 hashCode 的生成，
    // 执行 copy 的时候，不会 使用修改后的值，只会使用默认值
    var age: Int = 10
}

/**
 * 密封类：密封类 本身就是抽象的，不能 直接 初始化，可以存在 抽象成员
 *
 * <p>如果要继承该密封类，必须在 同一个包下（子包也不行）或者 是同一个文件下</p>
 */
sealed class SealedClass {

    /**
     * 这个是受保护的构造函数：默认即使 protected
     */
    constructor() {

    }

    /**
     * 私有构造函数
     */
    private constructor(desc: String) {

    }

    /**
     * 该方法不能被重写，默认是 final
     */
    fun test() {
        println("sealed test")
    }

    /**
     * 继承该 密封类 的 类，都必须实现该方法
     */
    abstract fun test(arg: String)

    /**
     * 可以结合 when，把 所有已知的情况都进行处理
     */
    fun test(classType: SealedClass) = when (classType) {
        is NewSealedClass -> {
            println("new sealed class")
        }

        is TestSealedClass -> {
            println("test sealed class")
        }
    }
}

// 继承自 sealedClass
class NewSealedClass : SealedClass() {
    override fun test(arg: String) {
        println(arg)
    }
}

fun main() {
    val data: DataClass = DataClass("test", 1)
    val data1: DataClass = DataClass("test", 1)
    // 返回的是  true，说明 == 使用的是 equals 进行判断
    println(data1 == data)
    // 返回 false，因为 三个 等号判断的是引用地址
    println(data1 === data)

    data.age = 10
    data1.age = 20

    val copy = data1.copy()
    // 返回 10。因为 age 不参与 copy
    println(copy.age)

    // 返回true，因为 age 不参与 equals 和 hashCode 的生成
    println(data1 == data)

    val data2 = DataClass("name", 1)
    // 返回false，因为 name 属性值 不一致
    println(data2 == data)

    // 可以获取 data 主构造函数对应的 参数信息。
    val (test1, cnt1) = data
    println("test : $test1,cnt : $cnt1")

    val sealedClass = NewSealedClass()
    sealedClass.test()
    sealedClass.test("test")

    sealedClass.test(NewSealedClass())
    sealedClass.test(TestSealedClass())

}