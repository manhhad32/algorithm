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


public class Sort {

  /**
   * Bubble Sort
   * 💡 Idea: Repeatedly step through the list, compare adjacent elements, and swap them if they are in the wrong order. Passes through the list are repeated until no swaps are needed.
   * Complexity:
   * Time: O(n*n) in all cases (can be optimized to O(n) in the best case if the array is already sorted).
   * Space: O(1) (in-place).
   * Stable: Yes.
   * When to use: Almost never in practice. Used for educational purposes or in interviews to check basic knowledge.
   * @param arr
   */
  private static void bubbleSort(int[] arr) {
    int n = arr.length;
    boolean swapped;
    for(int i = 0; i < n-1; i++) {
      swapped = false;
      for(int j = 0; j < n - 1 - i; j++) {
        if(arr[j] > arr[j + 1]) {
          // swap
          int temp =  arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
          swapped = true;
        }
      }
      if(!swapped) {
        break;
      }
    }
  }

  /**
   * Selection Sort
   * 💡 Idea: Find the minimum element in the unsorted part of the array and swap it with the first element of that unsorted part. Repeat until the entire array is sorted.
   * Complexity:
   * Time: O(n*n) in all cases.
   * Space: O(1) (in-place).
   * Stable: No (but can be implemented to be stable, though it's more complex).
   * When to use: Similar to Bubble Sort, it's inefficient. Its main advantage is that it makes a minimal number of swaps (at most n−1).
   * @param arr
   */
  private static void selectionSort(int[] arr) {
    int n = arr.length;
    for(int i = 0; i < n; i++) {
      int min_idx = i;
      for(int j = i + 1; j < n; j++) {
        if(arr[j] < arr[min_idx]) {
          min_idx = j;
        }
      }
      // Swap the found minimum element with the first element
      int temp = arr[min_idx];
      arr[min_idx] = arr[i];
      arr[i] = temp;
    }
  }

  /**
   * Insertion Sort
   * 💡 Idea: Similar to how you sort a hand of playing cards. Take one element and insert it into its correct position in the already sorted sub-array.
   * Complexity:
   * Time: O(n*n) (worst/average), O(n) (best-case, when the array is already sorted).
   * Space: O(1) (in-place).
   * Stable: Yes.
   * When to use: Very efficient for arrays that are nearly sorted or have a small size.
   * @param arr
   */
  private static void insertSort(int[] arr) {
    int n = arr.length;
    for(int i = 1; i < n; i++) {
      int key = arr[i];
      int j = i - 1;
      /* Move elements of arr[0..i-1], that are
      greater than key, to one position ahead
      of their current position */
      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j = j - 1;
      }
      arr[j + 1] = key;
    }
  }


  public static void main(String[] args) {
    int[] testArr = {3,2,4,5,9,8,6};
    //bubbleSort(testArr);
    //selectionSort(testArr);
    //insertSort(testArr);
    QuickSort quickSort = new QuickSort();
    quickSort.sort(testArr);
    for(int i = 0; i < testArr.length; i++) {
      System.out.print(testArr[i] + " ");
    }




  }


}
