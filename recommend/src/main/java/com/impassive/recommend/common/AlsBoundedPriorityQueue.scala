package com.impassive.recommend.common

/**
 * @author carl.yu
 * @date 2019-08-17
 */
class AlsBoundedPriorityQueue[A](maxSize: Int)(implicit ord: Ordering[A]) extends Iterable[A] with scala.collection.generic.Growable[A] with java.io.Serializable {
  private val underlying = new java.util.PriorityQueue[A](maxSize, ord)

  import scala.collection.JavaConverters._

  override def iterator: Iterator[A] = underlying.iterator.asScala

  override def size: Int = underlying.size

  override def ++=(xs: TraversableOnce[A]): this.type = {
    xs.foreach {
      this += _
    }
    this
  }

  override def +=(elem: A): this.type = {
    if (size < maxSize) {
      underlying.offer(elem)
    } else {
      maybeReplaceLowest(elem)
    }
    this
  }

  override def +=(elem1: A, elem2: A, elems: A*): this.type = {
    this += elem1 += elem2 ++= elems
  }

  override def clear() {
    underlying.clear()
  }

  private def maybeReplaceLowest(a: A): Boolean = {
    val head = underlying.peek()
    if (head != null && ord.gt(a, head)) {
      underlying.poll()
      underlying.offer(a)
    } else {
      false
    }
  }
}
