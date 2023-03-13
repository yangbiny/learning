package com.learning.datastructure;

import java.util.Arrays;

/**
 * @author impassive
 */
public class MergeTwoArray {

  public static void main(String[] args) {

    MergeTwoArray mergeTwoArray = new MergeTwoArray();
    int[] nums1 = {1, 2, 3, 5, 6, 7, 9, 10, 0, 0, 0, 0, 0};
    int[] nums2 = {1, 2, 4, 8, 11};

    mergeTwoArray.merge(nums1, 8, nums2, 5);

    System.out.println(Arrays.toString(nums1));
  }


  private void merge(int[] array1, int m, int[] array2, int n) {

    int writeIndex = m + n - 1;

    while (m > 0 && n > 0) {

      int value;
      if (array1[m - 1] > array2[n - 1]) {
        value = array1[m - 1];
        m--;
      } else {
        value = array2[n - 1];
        n--;
      }
      array1[writeIndex] = value;
      writeIndex--;
    }
    while (m > 0) {
      array1[writeIndex] = array1[m - 1];
      m--;
      writeIndex--;
    }

    while (n > 0) {
      array1[writeIndex] = array2[n - 1];
      n--;
      writeIndex--;
    }

  }

}
