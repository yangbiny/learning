package com.learning.datastructure;

import java.util.Arrays;
import java.util.Map;

/**
 * 最长递增子序列
 *
 * @author impassive
 */
public class LengthOfLIS {

  public static void main(String[] args) {
    LengthOfLIS lis = new LengthOfLIS();

    int[] array = {10, 9, 2, 5, 3, 7, 101, 18};
    System.out.println(lis.lengthOfLIS(array));
    System.out.println(lis.lengthOfLIS(array, 2));
  }


  public int lengthOfLIS(int[] nums, int k) {
    int[] dp = new int[nums.length];
    Arrays.fill(dp, 1);
    int result = 1;
    for (int i = 0; i < nums.length; i++) {

      for (int j = 0; j < i; j++) {
        if (nums[i] > nums[j] && nums[i] - nums[j] <= k) {
          dp[i] = Math.max(dp[i], dp[j] + 1);
        }
      }
      if (dp[i] > result) {
        result = dp[i];
      }

    }
    return result;
  }


  public int lengthOfLIS(int[] nums) {

    int[] dp = new int[nums.length];
    Arrays.fill(dp, 1);
    int result = 1;
    for (int i = 0; i < nums.length; i++) {
      for (int j = 0; j < i; j++) {
        if (nums[i] > nums[j]) {
          dp[i] = Math.max(dp[i], dp[j] + 1);
        }
      }

      if (dp[i] > result) {
        result = dp[i];
      }
    }
    return result;
  }

}
