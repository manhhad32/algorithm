package vn.codezx.algorithm.base;


/*
 * This is course Microservice Product Oriented
 * MIT No Attribution

 * Copyright (c) 2025 <Dr.JohnLe & Mr.HaNguyen>

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */


import java.util.Arrays;

public class QuickSort {

  /**
   * The main function to sort the array.
   * This is the function that a user will call.
   * @param arr The array to be sorted.
   */
  public void sort(int[] arr) {
    if(arr == null || arr.length == 0) {
      return;
    }
    quickSort(arr, 0, arr.length - 1);
  }

  private void quickSort(int[] arr, int low, int high) {
    if(low < high) {
      int pi = partition(arr, low, high);
      quickSort(arr, low, pi - 1);
      quickSort(arr, pi + 1, high);
    }
  }

  private int partition(int[] arr, int low, int high) {
    int pivot = arr[high];
    // i is the index of the last smaller element found
    // It starts at the position just before the first element of the array
    int i = (low - 1);
    for(int j = low; j < high; j++) {
      // If the current element is smaller than or equal to the pivot
      if(arr[j] <= pivot) {
        i++; // Increment the index of the smaller element
        // Swap arr[i] and arr[j]
        swap(arr, i, j);
      }
    }

    // After the loop, all elements smaller than the pivot are from low -> i.
    // Place the pivot (arr[high]) into its correct position (i + 1)
    swap(arr, i + 1, high);

    // Return the new index of the pivot
    return (i + 1);
  }

  private void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }
}
