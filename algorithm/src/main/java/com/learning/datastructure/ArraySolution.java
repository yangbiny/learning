package com.learning.datastructure;

/**
 * @author impassive
 */
public class ArraySolution {

  public static void main(String[] args) {
    removeDuplicates();
  }

  private static void removeDuplicates() {
    int[] nums = {1, 1, 2};
    int i = new ArraySolution().removeDuplicates(nums);
    System.out.println(i);
  }

  public int removeDuplicates(int[] nums) {
    if (nums.length == 0) {
      return 0;
    }
    int index = 1;
    int prev = nums[0];
    for (int i = 1; i < nums.length; i++) {
      if (nums[i] != prev) {
        nums[index] = nums[i];
        index++;
        prev = nums[i];
      }
    }
    return index;
  }
}
