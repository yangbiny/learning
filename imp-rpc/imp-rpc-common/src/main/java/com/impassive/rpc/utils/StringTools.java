package com.impassive.rpc.utils;

import java.util.Objects;

public class StringTools {

  public static boolean isEmpty(String str) {
    return str == null || str.trim().length() == 0;
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  public static boolean isEqual(String str1, String str2) {
    return Objects.equals(str1, str2);
  }

  public static boolean endWith(String path, String sub) {
    if (isEmpty(path)) {
      return false;
    }
    return path.endsWith(sub);
  }

  public static boolean notStartWith(String path, String sub) {
    if (isEmpty(path)) {
      return true;
    }
    return !path.startsWith(sub);
  }
}
