package com.impassive.kotlin.basic

class ScopeData(
    val name: String,
    val cnt: Int
) {
}

fun main() {
    val numbers = mutableListOf("one", "two", "three", "four", "five")

    val result = numbers.map { it.length }.filter { it > 3 }
    println(result)

    //let()

    // with 函数的原始模样
    // 适用于调用同一个类的多个方法时，可以省去类名重复，直接调用类的方法即可
    /*
         with(object){
           //todo
         }

         // 最后一行 或者 指定 return 可 返回
     */
    val scopeData = ScopeData("test", 1)
    with(scopeData) {
        println("name is $name,cnt is $cnt")
        if (cnt >= 2) {
            return
        }
        cnt
    }.let {
        println(it)
    }

    // run 是 let 和 with 的集合
    listOf(ScopeData("test", 1))
        .filter { it.cnt > 2 }
        .stream()
        .limit(1)
        ?.run {
            for (scopeData in this) {
                println("name is ${scopeData.name}")
            }
        }

    ScopeData("test",3).apply {  }

}

private fun let() {
    // let 函数的 原始 模样

    /*
    object.let{
        //在函数体内使用it替代object对象去访问其公有的属性和方法
        it.todo()

    }

    //另一种用途 判断object为null的操作
    object?.let{
        //表示object不为null的条件下，才会去执行let函数体
        it.todo()
    }

     */

    // 一个内联的lambda 表达式，类似于 一个回调函数，会在前置 操作 自行完成后执行
    ScopeData("test", 1).let {
        val cnt = it.cnt
        if (cnt > 0) {
            println(cnt)
        }
    }

    // 如果 结果为null，则不会执行let，否则会执行let
    listOf(ScopeData("test", 1))
        .filter { it.cnt > 2 }
        .stream()
        .limit(1)
        ?.let {
            for (scopeData in it) {
                println(scopeData)
            }
        }
}