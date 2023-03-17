package com.learning.datastructure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author impassive
 */
public class LongestPalindrome {

  public static void main(String[] args) {
    LongestPalindrome longestPalindrome = new LongestPalindrome();
    String text = longestPalindrome.longestPalindrome("aacabdkacaa");
    System.out.println(text);
  }

  private String longestPalindrome(String s) {
    char[] chars = s.toCharArray();

    boolean[][] dp = new boolean[chars.length][chars.length];

    int maxLength = 1;
    int beginIndex = 0;

    for (int i = 1; i < chars.length; i++) {
      for (int j = i - 1; j >= 0; j--) {

        if (i == j) {
          dp[i][i] = true;
        }

        // 如果 当前的两个字符不想等，则说明，从 i 到 j 一定不是 回文
        if (chars[i] != chars[j]) {
          continue;
        }

        // 下面 是两个 相等的情况

        // 如果  往里收 一个 位置，还有字符，则需要 判断 里面 是不是 回文

        // acca  在判断到 两边的 a 相等的 时候，只要 两个 c的位置 是相等，就可以是回文
        if (j + 1 < i - 1) {
          dp[j][i] = dp[j + 1][i - 1];
        } else {
          // aca 中间只有一个 （j + 1 == i - 1 的情况）或者 aa (j + 1 > i - 1 的 情况)
          dp[j][i] = true;
        }

        if (dp[j][i] && i - j + 1 > maxLength) {
          maxLength = i - j + 1;
          beginIndex = j;
        }
      }
    }
    return s.substring(beginIndex, beginIndex + maxLength);
  }

}
