package com.learning.datastructure;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author impassive
 */
public class DynamicPrefix {

  public static void main(String[] args) {
    String[] strs = {"ab", "a"};
    System.out.println(new DynamicPrefix().longestCommonPrefix(strs));

    int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
    int[] x = new DynamicPrefix().lengthOfLIS2(nums);
    System.out.println(x);
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
