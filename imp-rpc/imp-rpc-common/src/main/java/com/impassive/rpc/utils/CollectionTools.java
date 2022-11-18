package com.impassive.rpc.utils;

import java.util.Collection;
import java.util.Objects;

public class CollectionTools {

  /**
   * 判断集合是否为 空。如果 集合 只有 null元素，也认为是空
   *
   * @param collection 需要判定的集合
   * @return true:集合为空
   */
  public static boolean isEmptyWithoutNull(Collection<?> collection) {
    return isEmpty(collection) || collection.stream().noneMatch(Objects::nonNull);
  }

  /**
   * 判断集合是否为空。如果集合 有null元素，也不为空
   *
   * @param collection 需要判定的集合
   * @return true:集合为空
   */
  public static boolean isEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * 判断 集合是否为不为空，如果集合 只有 null元素，也会返回 false
   *
   * @param collection 需要判定的集合
   * @return true:集合不为空，且含有非空元素
   */
  public static boolean isNotEmpty(Collection<?> collection) {
    return !isEmptyWithoutNull(collection);
  }

}
