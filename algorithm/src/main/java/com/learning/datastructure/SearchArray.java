package com.learning.datastructure;

/**
 * @author impassive
 */
public class SearchArray {

  public static void main(String[] args) {

    SearchArray searchArray = new SearchArray();
    int[] array = {3, 1};
    boolean search = searchArray.search(array, 3);
    System.out.println(search);
  }

  private boolean search(int[] nums, int target) {
    int index = 0;

    // 找到 旋转点，就可以知道前面是 递增的，后面也是递增的。且  后面的  所有数据小于等于 第一个数据
    for (int i = 0; i < nums.length - 1; i++) {
      if (nums[i + 1] >= nums[i]) {
        continue;
      }
      index = i + 1;
      break;
    }
    if (nums[index] == target) {
      return true;
    }

    // 确定 二分查找的 位置
    int min = 0;
    int max = nums.length - 1;
    if (index > 0) {
      if (target >= nums[0]) {
        max = index - 1;
      } else {
        min = index;
      }
    }

    // 进行二分查找
    while (min <= max) {
      if (max == min) {
        return nums[min] == target;
      }
      int mid = (max + min) / 2;
      if (nums[mid] == target) {
        return true;
      }
      if (nums[mid] > target) {
        max = mid - 1;
      } else {
        min = mid + 1;
      }
    }

    return false;
  }

}

