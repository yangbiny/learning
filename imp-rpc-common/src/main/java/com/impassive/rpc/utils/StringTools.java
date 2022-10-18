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

}
