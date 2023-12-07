package com.learning.datastructure;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author impassive
 */
public class DynamicPrefix {

  public static void main(String[] args) {

    String multiply = new DynamicPrefix().multiply("5", "12");
    System.out.println(multiply);

    String[] strs = {"ab", "a"};
    System.out.println(new DynamicPrefix().longestCommonPrefix(strs));

    int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
    int[] x = new DynamicPrefix().lengthOfLIS2(nums);
    System.out.println(x);
  }


  public String multiply(String num1, String num2) {
    if ("0".equals(num1) || "0".equals(num2)) {
      return "0";
    }
    int maxSize = num1.length() + num2.length();
    int[] result = new int[maxSize];

    char[] num1CharArray = num1.toCharArray();
    char[] num2CharArray = num2.toCharArray();

    for (int i = num2CharArray.length - 1; i >= 0; i--) {
      int a = num2CharArray[i] - 48;
      for (int j = num1CharArray.length - 1; j >= 0; j--) {
        int b = num1CharArray[j] - 48;
        int tmp = a * b;
        result[i + j + 1] = tmp + result[i + j + 1];
      }
    }

    for (int i = result.length - 1; i > 0; i--) {
      if (result[i] >= 10) {
        int tmp = result[i];
        result[i] = tmp % 10;
        result[i - 1] = result[i - 1] + tmp / 10;
      }
    }
    int index = 0;
    for (int i = 0; i < result.length; i++) {
      if (result[i] != 0) {
        index = i;
        break;
      }
    }

    char[] chars = new char[result.length - index];
    boolean isFirst = true;
    int id = 0;
    for (int i = index; i < result.length; i++) {
      if (result[i] == 0 && isFirst) {
        isFirst = false;
        continue;
      }
      isFirst = false;
      chars[id++] = (char) (result[i] + 48);
    }

    return new String(chars);
  }

  public int[] lengthOfLIS2(int[] nums) {
    int[] dp = new int[nums.length];

    Arrays.fill(dp, 1);
    int[] result = new int[nums.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = i;
    }

    for (int i = 0; i < nums.length; i++) {
      for (int j = 0; j < i; j++) {
        if (nums[i] > nums[j]) {
          if (dp[i] > dp[j] + 1) {
            result[i] = i;
          } else {
            dp[i] = dp[j] + 1;
            result[i] = j;
          }
        }
      }
    }

    int max = dp[0];
    int maxIndex = 0;
    for (int i = 0; i < dp.length; i++) {
      if (dp[i] > max) {
        max = dp[i];
        maxIndex = i;
      }
    }
    int[] temp = new int[max];
    for (int i = max - 1; i >= 0; i--) {
      temp[i] = nums[maxIndex];
      maxIndex = result[maxIndex];
    }

    return temp;
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
