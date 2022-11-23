package com.impassive.kotlin.basic

/**
 * @author impassive
 */
// 如果构造器 有 修饰符，就需要写明constructor ，如果 没有或者为 public，可以直接忽略
class ClassConstructor private constructor(
    name: String
) {

    // 初始化 属性。 also 见 ScopeFunction
    val firstProperty = "First property: $name".also { println(it) }

    // 初始化代码块。会 比 二级构造函数先执行，比 主构造函数 后执行
    init {
        println("init1 : $name")
    }

    val secondProperty = "Second property: ${name.length}".also { println(it) }

    // 初始化代码块，如果有多个 初始化代码块，则执行顺序是 从上往下 依次执行
    init {
        println("init 2 : $name")
    }

    // 标志 延迟 初始化 该属性的值
    lateinit var thirdProperties: String

    // 二级构造函数。
    // 二级构造函数 必须 直接或者间接（通过其他构造函数） 调用 主构造函数，使用 this 的方式调用主构造函数
    constructor(name: String, age: Int) : this(name) {
        println("constructor : $age, $name")
    }

    // 二级构造函数，通过 this(name,age)调用了 主构造函数
    // 可以 给 构造函数的参数 指定一个默认值：cnt 如果不传就是默认为 0
    constructor(name: String, lastName: String, cnt: Int = 0) : this(name) {
        this.lastName = lastName
    }

    // 标识 可能为空 String 后面的问号标识 可能为空
    // 下面的 set 标识，在设置 值的时候，如果 value 不为null，并且 长度大于 3 才会设置值
    var lastName: String? = null
        set(value) {
            thirdProperties = ""
            value?.let {
                if (it.length > 3) {
                    field = it
                }
            }
        }
}

fun main() {
    val first = ClassConstructor("test", 1)

    // 此时 由于 他的 值 为null，所以不会打印
    first.lastName?.let { println(it) }

    val classConstructor = ClassConstructor("testName", "la")

    // 此时 不为null，所以会打印
    classConstructor.lastName?.let {
        println(it)
    }
}