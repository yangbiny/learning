package com.learning.datastructure;

/**
 * 最长递增子序列
 *
 * @author impassive
 */
public class LengthOfLIS {

  public static void main(String[] args) {
    LengthOfLIS lis = new LengthOfLIS();

    int[] array = {1, 3, 6, 7, 9, 4, 10, 5, 6};
    System.out.println(lis.lengthOfLIS(array));
  }


  private int lengthOfLIS2(int[] nums) {
    if (nums.length == 0) {
      return 0;
    }

    int[] result = new int[nums.length];
    result[0] = nums[0];

    int end = 0;

    for (int i = 1; i < nums.length; i++) {
      if (nums[i] > result[end]) {
        result[++end] = nums[i];
        continue;
      }

      int pos = binarySearch(nums, 0, end, nums[i]);
      // 这是因为，找到的这个位置，连接上当前的这个值，可以生成一个新的  最长 递增子序列
      nums[pos] = nums[i];
    }
    return end + 1;
  }

  private int binarySearch(int[] nums, int start, int end, int value) {
    int min = start;
    int max = end;
    while (min < max) {
      int mid = (max + min) / 2;
      if (nums[mid] == value) {
        return mid;
      }
      if (nums[mid] > value) {
        max = mid - 1;
      } else {
        min = mid + 1;
      }
    }
    return max;
  }


  /**
   * 动态规划的 方式，时间复杂度为 n^2
   */
  private int lengthOfLIS(int[] nums) {
    if (nums.length == 0) {
      return 0;
    }

    int[] result = new int[nums.length];
    result[0] = 1;

    for (int i = 1; i < nums.length; i++) {
      for (int j = i - 1; j >= 0; j--) {
        if (result[i] == 0) {
          result[i] = 1;
        }
        if (nums[i] > nums[j]) {
          result[i] = Math.max(result[i], result[j] + 1);
        }
      }
    }

    int max = -1;
    for (int num : result) {
      if (num > max) {
        max = num;
      }
    }
    return max;
  }

}
