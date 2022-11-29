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


fun main() {
    val data: DataClass = DataClass("test", 1)
    val data1: DataClass = DataClass("test", 1)
    // 返回的是  true，说明 == 使用的是 equals 进行判断
    println(data1 == data)

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

}