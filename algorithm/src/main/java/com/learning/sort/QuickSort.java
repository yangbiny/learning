package com.learning.sort;

import java.lang.reflect.Array;
import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {

        QuickSort sort = new QuickSort();
        int[] array = {10,2,9,4,3,6,7,5,8,1};

        //int[] array = {3, 2, 4, 1};

        sort.sort(array);

        System.out.println(array);
    }


    public void sort(int[] array) {
        if (array.length == 0) {
            return;
        }
        sort(array, 0, array.length - 1);
    }

    public void sort(int[] array, int begin, int end) {
        if (begin >= end) {
            return;
        }

        int min = begin;
        int max = end;

        int index = begin;

        int temp = array[index];
        while (min < max) {

            while (max > min && array[max] > temp) max--;

            swap(array, index, max);
            index = max;

            while (min < max && array[min] <= temp) min++;
            swap(array, index, min);
            index = min;

        }

        sort(array, begin, index - 1);
        sort(array, index + 1, end);

    }

    private void swap(int[] array, int idx1, int idx2) {
        int temp = array[idx1];
        array[idx1] = array[idx2];
        array[idx2] = temp;
    }


}
