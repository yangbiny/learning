package com.learning.sort;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class QuickSort {

  public static void main(String[] args) {

    QuickSort sort = new QuickSort();
    int[] array1 = {10, 2, 9, 4, 3, 6, 7, 5, 8, 1};
    int[] array2 = {10, 2, 9, 4, 3, 6, 7, 5, 8, 1};
    //int[] array = {3, 2, 4, 1};
    sort.sort(array1, 0, array1.length - 1);
    sort.sortWithWhile(array2, 0, array2.length - 1);

    System.out.println(Arrays.toString(array1));
    System.out.println(Arrays.toString(array2));
  }

  // 循环的方式
  private void sortWithWhile(int[] array, int begin, int end) {
    Stack<List<Integer>> stack = new Stack<>();
    stack.add(List.of(begin, end));
    while (!stack.isEmpty()) {
      List<Integer> pop = stack.pop();
      Integer min = pop.get(0);
      Integer max = pop.get(1);
      if (min >= max) {
        continue;
      }
      int index = part(array, min, max);
      stack.add(List.of(min, index - 1));
      stack.add(List.of(index + 1, max));
    }

  }

  // 递归的方式
  private void sort(int[] array, int begin, int end) {
    if (begin >= end) {
      return;
    }
    int index = part(array, begin, end);
    sort(array, begin, index - 1);
    sort(array, index + 1, end);

  }

  private int part(int[] array, int min, int max) {
    int index = min;
    int temp = array[index];
    while (min < max) {

      while (max > min && array[max] > temp) {
        max--;
      }

      swap(array, index, max);
      index = max;

      while (min < max && array[min] <= temp) {
        min++;
      }
      swap(array, index, min);
      index = min;

    }
    return index;
  }

  private void swap(int[] array, int idx1, int idx2) {
    int temp = array[idx1];
    array[idx1] = array[idx2];
    array[idx2] = temp;
  }


}
