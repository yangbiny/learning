package com.learning.datastructure;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author impassive
 */
public class ArraySolution {

  public static void main(String[] args) {
    int kthLargest = new ArraySolution().findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2);
    System.out.println(kthLargest);
    removeDuplicates();
  }

  private static void removeDuplicates() {
    int[] nums = {1, 1, 2};
    int i = new ArraySolution().removeDuplicates(nums);
    System.out.println(i);
  }

  public int findKthLargest(int[] nums, int k) {
    if (nums.length == 1) {
      return nums[0];
    }
    Queue<Integer> queue = new PriorityQueue<>(k, Comparator.comparingInt(o -> o));
    for (int num : nums) {
      queue.add(num);
      if (queue.size() > k) {
        queue.poll();
      }
    }
    int min = queue.poll();
    while (!queue.isEmpty()) {
      Integer poll = queue.poll();
      if (poll < min) {
        min = poll;
      }
    }
    return min;
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
