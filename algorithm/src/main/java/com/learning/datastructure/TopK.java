package com.learning.datastructure;

/**
 * @author impassive
 */
public class TopK {

  public static void main(String[] args) {

    int[] array = {3, 2, 1, 5, 6, 4};
    TopK topK = new TopK();

    System.out.println(topK.topK(array, 2));
  }

  private int topK(int[] array, int k) {
    return selectK(array, 0, array.length - 1, k);
  }

  private int selectK(int[] array, int start, int end, int k) {
    if (start == end) {
      return array[start];
    }

    // 先排序一次，返回的 index 前面的  都比 后面的大
    // 返回 的 index 是全 数组 里的 index
    int part = sort(array, start, end);
    int index = part + 1;
    if (index == k) {
      return array[index - 1];
    }
    // 如果 当前的 index（即 第 index+ 1 大的数） 大于 K ，那么 说明，K 在前面
    if (index > k) {
      return selectK(array, start, part - 1, k);
    }
    // 反之，K 在 后面
    return selectK(array, part + 1, end, k);
  }

  /**
   * 由大到小的排序
   */
  private int sort(int[] array, int start, int end) {
    int temp = array[start];
    while (start < end) {

      while (end > start && array[end] <= temp) {
        end--;
      }
      array[start] = array[end];

      while (start < end && array[start] > temp) {
        start++;
      }
      array[end] = array[start];
    }

    array[start] = temp;
    return start;
  }


}
