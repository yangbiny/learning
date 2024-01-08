package com.learning.datastructure;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * @author impassive
 */
public class DynamicPrefix {

  public static void main(String[] args) {

    int i6 = new DynamicPrefix().longestValidParentheses("())");
    System.out.println(i6);

    int i5 = new DynamicPrefix().coinChange(new int[]{186, 419, 83, 408}, 6249);
    System.out.println(i5);

    int[][] minPathSum = {
        {1, 3, 1},
        {1, 5, 1},
        {1, 4, 1}
    };
    int i4 = new DynamicPrefix().minPathSum(minPathSum);
    System.out.println(i4);

    int i3 = new DynamicPrefix().climbStairs(10);
    System.out.println(i3);

    int i2 = new DynamicPrefix().longestCommonSubsequence("abcde", "ace");
    System.out.println(i2);

    int i1 = new DynamicPrefix().maxProfit(new int[]{7, 1, 5, 3, 6, 4});
    System.out.println(i1);

    int length = new DynamicPrefix().findLength(new int[]{1, 2, 3, 2, 1}, new int[]{1, 2, 3, 2, 1});
    System.out.println(length);

    String resultStr = new DynamicPrefix().longestPalindrome("abbab");
    System.out.println(resultStr);

    int[] maxSubArrayNums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    int result = new DynamicPrefix().maxSubArray(maxSubArrayNums);
    System.out.println(result);

    int i = new DynamicPrefix().lengthOfLongestSubstring("bbbbb");
    System.out.println(i);

    String multiply = new DynamicPrefix().multiply("5", "12");
    System.out.println(multiply);

    String[] strs = {"ab", "a"};
    System.out.println(new DynamicPrefix().longestCommonPrefix(strs));

    int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
    int[] x = new DynamicPrefix().lengthOfLIS2(nums);
    System.out.println(x);
  }

  public int longestValidParentheses(String s) {
    if (s.isEmpty()) {
      return 0;
    }
    char[] chars = s.toCharArray();
    int[] dp = new int[chars.length + 1];
    dp[0] = 0;
    dp[1] = 0;

    int result = 0;
    for (int i = 1; i < chars.length; i++) {
      if (chars[i] == '(') {
        continue;
      }
      if (chars[i - 1] == '(') {
        if (i - 2 >= 0) {
          dp[i] = dp[i - 2] + 2;
        } else {
          dp[i] = 2;
        }
      } else {
        if (i - dp[i -1] -1 >= 0 && chars[i - dp[i - 1] - 1] == '(' && dp[i - 1] > 0) {
          int tmp = 0;
          if (i - dp[i - 1] - 2 >= 0) {
            tmp = dp[i - dp[i - 1] - 2];
          }
          dp[i] = dp[i - 1] + tmp + 2;
        }
      }
      if (dp[i] > result) {
        result = dp[i];
      }
    }

    return result;
  }

  public int coinChange(int[] coins, int amount) {
    if (amount == 0) {
      return -1;
    }
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);
    dp[0] = 0;
    for (int i = 1; i <= amount; i++) {
      for (int coin : coins) {
        if (i >= coin) {
          dp[i] = Math.min(dp[i - coin] + 1, dp[i]);
        }
      }
    }

    return dp[amount] > amount ? -1 : dp[amount];
  }

  public int minPathSum(int[][] grid) {
    int[][] dp = new int[grid.length][grid[0].length];
    dp[0][0] = grid[0][0];
    for (int i = 1; i < grid[0].length; i++) {
      dp[0][i] = dp[0][i - 1] + grid[0][i];
    }

    for (int i = 1; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (j > 0) {
          dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
        } else {
          dp[i][j] = dp[i - 1][j] + grid[i][j];
        }

      }
    }

    return dp[grid.length - 1][grid[0].length - 1];
  }

  public int climbStairs(int n) {
    if (n == 1 || n == 2) {
      return n;
    }
    int[] dp = new int[n + 1];
    dp[1] = 1;
    dp[2] = 2;
    for (int i = 3; i <= n; i++) {
      dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
  }

  public int longestCommonSubsequence(String text1, String text2) {
    if (text1.isEmpty() || text2.isEmpty()) {
      return 0;
    }
    char[] char1 = text1.toCharArray();
    char[] char2 = text2.toCharArray();
    int[][] dp = new int[char1.length + 1][char2.length + 1];
    for (int i = 1; i <= char1.length; i++) {

      for (int j = 1; j <= char2.length; j++) {
        if (char1[i - 1] == char2[j - 1]) {
          dp[i][j] = dp[i - 1][j - 1] + 1;
        } else {
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }

    return dp[char1.length][char2.length];
  }

  public int maxProfit(int[] prices) {

    int minPrice = Integer.MAX_VALUE;
    int result = -1;

    for (int price : prices) {
      if (price < minPrice) {
        minPrice = price;
      } else if (price - minPrice > result) {
        result = price - minPrice;
      }
    }
    return result;
  }

  public int findLength(int[] nums1, int[] nums2) {
    if (nums1.length == 0 || nums2.length == 0) {
      return 0;
    }

    int[][] dp = new int[nums1.length][nums2.length];
    for (int[] ints : dp) {
      Arrays.fill(ints, 0);
    }

    int result = 0;
    for (int i = 0; i < nums1.length; i++) {
      for (int j = 0; j < nums2.length; j++) {
        if (nums1[i] == nums2[j]) {
          if (i - 1 >= 0 && j - 1 >= 0) {
            dp[i][j] = dp[i - 1][j - 1] + 1;
          } else {
            dp[i][j] = 1;
          }
        }

        if (dp[i][j] > result) {
          result = dp[i][j];
        }
      }
    }
    return result;
  }

  public String longestPalindrome(String s) {
    boolean[][] dp = new boolean[s.length()][s.length()];
    char[] charArray = s.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      dp[i][i] = true;
    }

    int begin;
    int end;

    String result = "";

    for (int i = 0; i < charArray.length; i++) {
      end = i + 1;
      begin = i;
      for (int j = i - 1; j >= 0; j--) {
        if (j == i - 1) {
          dp[j][i] = charArray[j] == charArray[i];
        } else {
          dp[j][i] = charArray[i] == charArray[j] && dp[j + 1][i - 1];
        }
        if (dp[j][i]) {
          begin = j;
        }
      }
      if (end - begin > result.length()) {
        result = s.substring(begin, end);
      }
    }

    return result;
  }

  public int maxSubArray(int[] nums) {
    int n = nums[0];
    int result = n;
    for (int i = 1; i < nums.length; i++) {
      n = Math.max(nums[i] + n, nums[i]);
      if (n > result) {
        result = n;
      }
    }
    return result;
  }

  public int lengthOfLongestSubstring(String s) {
    if (s.isEmpty()) {
      return 0;
    }
    String result = s.charAt(0) + "";
    int startIndex = 0;
    for (int i = 1; i < s.length(); i++) {
      String tmp = s.substring(startIndex, i);
      char ch = s.charAt(i);

      int index = tmp.indexOf(ch);
      if (index > -1) {
        startIndex = s.indexOf(ch, startIndex) + 1;
        i = startIndex;
      } else {
        tmp = s.substring(startIndex, i + 1);
        if (tmp.length() > result.length()) {
          result = tmp;
        }
      }
    }
    return result.length();
  }

  public String multiply(String num1, String num2) {
    if ("0" .equals(num1) || "0" .equals(num2)) {
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
