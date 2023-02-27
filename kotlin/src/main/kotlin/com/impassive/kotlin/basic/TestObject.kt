package com.impassive.kotlin.basic

/**
 * @author impassive
 */
// 使用object，该对象就变成了一个单例对象
// 使用object 关键字的时候，不能有 构造函数，包括 主构造函数 和 次级构造函数
object TestObject {

    // 单例对象的方法
    fun method() {

    }

}

/*

public final class com.impassive.kotlin.basic.TestCompanionObject {
  public static final com.impassive.kotlin.basic.TestCompanionObject$TestObject TestObject;

  public com.impassive.kotlin.basic.TestCompanionObject();
    Code:
       0: aload_0
       1: invokespecial #8                  // Method java/lang/Object."<init>":()V
       4: return

  static {};
    Code:
       0: new           #13                 // class com/impassive/kotlin/basic/TestCompanionObject$TestObject
       3: dup
       4: aconst_null
       5: invokespecial #16                 // Method com/impassive/kotlin/basic/TestCompanionObject$TestObject."<init>":(Lkotlin/jvm/internal/DefaultConstructorMarker;)V
       8: putstatic     #20                 // Field TestObject:Lcom/impassive/kotlin/basic/TestCompanionObject$TestObject;
      11: return
}
 */
class TestCompanionObject {

    // 伴生对象。对象名 可以忽略不写。如果忽略不写，则默认的名字是 Companion
    // 每一个 class 只能有最多 一个 伴生 对象
    companion object TestObject {

        fun method() {

        }

    }

}

/*
public final class com.impassive.kotlin.basic.TestObjectKt {
  public static final void main();
    Code:
       0: getstatic     #12                 // Field com/impassive/kotlin/basic/TestObject.INSTANCE:Lcom/impassive/kotlin/basic/TestObject;
       3: invokevirtual #15                 // Method com/impassive/kotlin/basic/TestObject.method:()V
       6: getstatic     #21                 // Field com/impassive/kotlin/basic/TestCompanionObject.TestObject:Lcom/impassive/kotlin/basic/TestCompanionObject$TestObject;
       9: invokevirtual #24                 // Method com/impassive/kotlin/basic/TestCompanionObject$TestObject.method:()V
      12: return

  public static void main(java.lang.String[]);
    Code:
       0: invokestatic  #27                 // Method main:()V
       3: return
}
 */
fun main() {
    // 实际上调用的 是这个单例对象的方法。（kotlin中没有 静态方法）
    TestObject.method()

    // 调用的 是伴生对象的 方法 。忽略了 对象名
    TestCompanionObject.method()
}