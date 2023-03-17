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

  private void sort(int[] array, int begin, int end) {
    if (begin >= end) {
      return;
    }

    int temp = array[begin];
    int index = begin;
    int min = begin;
    int max = end;

    while (min < max) {

      while (array[max] >= temp && max > min) {
        max--;
      }
      array[index] = array[max];
      index = max;

      while (array[min] < temp && min < max) {
        min++;
      }
      array[index] = array[min];
      index = min;
    }

    array[index] = temp;
    sort(array, begin, index - 1);
    sort(array, index + 1, end);
  }


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

      int temp = array[min];
      int index = min;
      int min1 = min;
      int max1 = max;

      while (min < max) {

        while (max > min && array[max] >= temp) {
          max--;
        }
        array[index] = array[max];
        index = max;

        while (min < max && array[min] < temp) {
          min++;
        }
        array[index] = array[min];
        index = min;
      }
      array[index] = temp;
      stack.add(List.of(min1, index - 1));
      stack.add(List.of(index + 1, max1));

    }

  }

}
