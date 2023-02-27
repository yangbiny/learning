package com.impassive.recommend.common

import org.apache.commons.lang3.StringUtils

import scala.util.control.Breaks.{break, breakable}

/**
 * @author carl.yu
 * @date 2019/11/10
 */
@SerialVersionUID(1L)
object HBaseTool extends Serializable {

  /**
   * HBase 经常用到 定长字符串倒转的技巧
   *
   * @param s
   * @return
   */
  def unnormalize(s: String): Int = {
    val src = StringUtils.reverse(s)
    var offset = -1
    breakable(
      for (i <- 0 until src.length) {
        val aChar = src.charAt(i)
        if (aChar != '0') {
          offset = i
          break()
        }
      }
    )
    var fromUid = 0
    if (offset != -1) {
      fromUid = src.substring(offset).toInt
    }
    fromUid
  }

  def normalize(target: String, length: Int): String = {
    StringUtils.reverse(fillZeroPrefix(target, length))
  }

  def fillZeroPrefix(target: String, length: Int): String = {
    if (target.length > length) throw new IllegalArgumentException("长度超过了11位:" + target.length)
    if (target.length == length) return target
    val prefixLen = length - target.length()
    val sb = new StringBuilder
    for (i <- 0 until prefixLen) {
      sb.append("0")
    }
    sb.toString() + target
  }

}
