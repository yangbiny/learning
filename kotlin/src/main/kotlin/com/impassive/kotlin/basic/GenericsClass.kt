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

    // 这里 有 @UnsafeVariance 是因为 T 标记为 in。in 是只写 不读，这里返回 返回了 T 就可能会出现类型 转换异常
    // in 表示的是 该类或者 该类的父类，如果读 返回 是 Object，无法确定 具体的类型
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

class TestStringV2 : CharSequence, Comparable<TestStringV2> {

    private var char: Char = 'a'


    override val length: Int
        get() = TODO("Not yet implemented")

    override fun get(index: Int): Char {
        TODO("Not yet implemented")
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: TestStringV2): Int {
        return char.compareTo(other.char)
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

// 指定 List 的类型 必须是 Number类型。out 可以省略
fun addNumbers(args: List<out Number>?): Int {
    var result = 0
    args?.let {
        for (number in it) {
            result += number.toInt()
        }
    }
    return result
}

/**
 * 接受任意类型的 List
 */
fun addNumbersV2(args: List<*>?): Int {
    var result = 0
    args?.let {
        for (number in it) {
            // 判断是不是 number类型
            if (number !is Number) {
                continue
            }
            result += number.toInt()
        }
    }
    return result
}

// 定义了一个范型方法
// where 标识 该范型 必须同时 继承自 CharSequence 和 Comparable
fun <T> testMulti(list: List<T>, threshold: T): List<T>
        where T : CharSequence,
              T : Comparable<T> {
    return list.filter { it > threshold }
        .map { it }
}


fun main() {

    // 需要指定 范型类型。如果有主构造函数，可以推断，则无需指定
    val testClass = GenericsClass<CharSequence>()
    testClass.add("asd")
    // 这个可以加入，因为 他继承了 CharSequence
    testClass.add(TestString())
    // 这个不能加入。因为 定义的 testClass 的 类型 上线 是 String
    //testClass.add(123)

    val listStr = listOf(TestString())
    val listStrV2 = listOf(TestStringV2())
    // 这个 会报错，因为  这个 方法 要求 必须要 继承自 Comparable 和 CharSequence，只有一个会报错
    //testMulti(listStr,TestString())
    testMulti(listStrV2, TestStringV2())


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


    val list1 = mutableListOf(1, 2, 3)
    val result = addNumbers(list1)

    val list3 = mutableListOf("123")
    // 会报错，因为必须要 number 类型
    //val result2 = addNumbers(list3)

    val list2 = listOf(1.2, 1, 3, 4.555, "asd")
    println(result)
    // 接受 任意类型的 list
    val numbersV2 = addNumbersV2(list2)
    println(numbersV2)

}