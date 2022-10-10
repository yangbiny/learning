package com.impassive.rpc.utils;

public class StringTools {

  public static boolean isEmpty(String str) {
    return str == null || str.trim().length() == 0;
  }

  public static boolean isNotEmpty(String str){
    return !isEmpty(str);
  }

}
