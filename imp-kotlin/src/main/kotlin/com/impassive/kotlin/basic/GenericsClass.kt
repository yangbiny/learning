package com.impassive.kotlin.basic

/**
 * @author impassive
 */
// 定义 范型类
class GenericsClass<in T> {

    // 定义一个可变的List。  直接 是 List 是不可变的
    private var value: MutableList<T> = mutableListOf()

    fun add(args: T): Boolean {
        return value.add(args)
    }

    /**
     * T 后面的 ? 表示可能会返回 null
     */
    fun get(index: Int): @UnsafeVariance T? {
        if (value.size <= index) {
            return null
        }
        return value[index]
    }
}

class TestComparable : Comparable<TestComparable> {
    override fun compareTo(other: TestComparable): Int {
        TODO("Not yet implemented")
    }

}

class TestString : CharSequence {
    override val length: Int
        get() = TODO("Not yet implemented")

    override fun get(index: Int): Char {
        TODO("Not yet implemented")
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        TODO("Not yet implemented")
    }

}

// vararg 是可变数量的参数
// 范型方法
fun <T> genericsMethod(args: Class<T>): T {
    val constructor = args.getConstructor()
    return constructor.newInstance()
}

// 可以根据 范型类型区分，所以 和 上面的方法 没有冲突
// 表示 T 类型的参数 必须是 可以进行比较的
fun <T : Comparable<T>> genericsMethod(args: T): T {
    return args
}


fun addNumbers(args: List<Number>?): Int {
    var result = 0
    args?.let {
        for (number in it) {
            result += number.toInt()
        }
    }
    return result
}


fun main() {

    // 需要指定 范型类型。如果有主构造函数，可以推断，则无需指定
    val testClass = GenericsClass<CharSequence>()
    testClass.add("asd")
    // 这个可以加入，因为 他继承了 CharSequence
    testClass.add(TestString())
    // 这个不能加入。因为 定义的 testClass 的 类型 上线 是 String
    //testClass.add(123)

    // 也可以这样指定 范型类型。后面构造 函数 处的范型 可写 可忽略
    // Any 是kotlin的 根对象，类似于 Java 的Object
    // Any 后面的 ? 表示  是否可以接受 null
    val testClass2: GenericsClass<Any?> = GenericsClass()
    testClass2.add("123")
    testClass2.add(123)
    testClass2.add(null)

    val testClass3: GenericsClass<Any> = GenericsClass()
    testClass3.add(123)
    testClass3.add("123")
    // 这个不能传null
    //testClass3.add(null)

    println(testClass2)
    println(testClass3.get(1))
    genericsMethod(testClass.javaClass)
    genericsMethod(1)
    val genericsMethod = genericsMethod(TestComparable())
    println(genericsMethod)


    val list = listOf(1, 2, 3)
    val result = addNumbers(list)
    println(result)

}