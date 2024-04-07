package com.learning.sort;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class QuickSort {

  public static void main(String[] args) {

    QuickSort sort = new QuickSort();
    int[] array1 = {3,1,2};
    int[] array2 = {10, 2, 9, 4, 3, 6, 7, 5, 8, 1};
    //int[] array = {3, 2, 4, 1};
  /*  sort.sort(array1, 0, array1.length - 1);
    sort.sortWithWhile(array2, 0, array2.length - 1);*/

    sort.sort1(array1, 0, array1.length - 1);

    System.out.println(Arrays.toString(array1));
    System.out.println(Arrays.toString(array2));
  }

  private void sort1(int[] array, int begin, int end) {
    // 递归终止条件
    if (begin >= end) {
      return;
    }
    int tmp = array[begin];
    int index = begin;
    int i = begin;
    int j = end;

    while (i <= j && array[i] < tmp) {
      i++;
    }
    array[index] = array[i];
    index = i;

    while (i <= j && array[j] > tmp) {
      j--;
    }
    array[index] = array[j];
    index = j;

    array[index] = tmp;

    sort1(array, begin, index - 1);
    sort1(array, index + 1, end);
  }

}
