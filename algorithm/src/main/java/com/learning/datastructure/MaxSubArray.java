package com.learning.datastructure;

/**
 * @author impassive
 */
public class MaxSubArray {

  public static void main(String[] args) {
    MaxSubArray maxSubArray = new MaxSubArray();
    int[] array = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    System.out.println(maxSubArray.maxSubArray(array));
  }


  public int maxSubArray(int[] nums) {

    int sum = nums[0];
    int n = nums[0];

    for (int i = 1; i < nums.length; i++) {

      // 只要  n > 0 就 继续累加。即使 加的是 负数。因为下面会有  sum  判断，sum 一定是 每一次 最大的
      if (n > 0) {
        n += nums[i];
      } else {
        // 如果 n < 0,则 无论 加 正数还是负数，都会让 数据 变小，所以，直接清空
        n = nums[i];
      }

      if (sum < n) {
        sum = n;
      }

    }
    return sum;
  }
}
