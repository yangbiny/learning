package com.learning.datastructure;

/**
 * @author impassive
 */
public class DynamicPrefix {

  public static void main(String[] args) {
    String[] strs = {"ab", "a"};
    System.out.println(new DynamicPrefix().longestCommonPrefix(strs));
  }

  public String longestCommonPrefix(String[] strs) {
    if (strs.length == 0) {
      return "";
    }

    String minStr = strs[0];
    for (String str : strs) {
      if (str.length() < minStr.length()) {
        minStr = str;
      }
    }

    int index = 0;
    boolean con = true;
    for (int i = 0; i < minStr.length() && con; i++) {
      char c = minStr.charAt(i);
      for (String str : strs) {
        if (str.charAt(i) != c) {
          con = false;
          break;
        }
      }
      index = i + 1;
    }
    return minStr.substring(0, index);
  }

}
