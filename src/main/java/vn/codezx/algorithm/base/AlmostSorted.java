package vn.codezx.algorithm.base;/*
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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlmostSorted {
  public static void main(String[] args) {

    List<Integer> arr = List.of(4,2);
    almostSorted(arr);
  }

  public static void almostSorted(List<Integer> iputArr) {
    int[] arr = iputArr.stream().mapToInt(Integer::intValue).toArray();
    int n = arr.length;

    // 1. Tạo một bản sao đã sắp xếp để so sánh
    int[] sortedArr = Arrays.copyOf(arr, n);
    Arrays.sort(sortedArr);

    // 2. Tìm các chỉ số có giá trị không khớp
    List<Integer> diffIndices = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      if (arr[i] != sortedArr[i]) {
        diffIndices.add(i);
      }
    }

    // 3. Phân tích các trường hợp dựa trên số lượng chỉ số khác biệt

    // Trường hợp 1: Mảng đã được sắp xếp
    if (diffIndices.isEmpty()) {
      System.out.println("yes");
      return;
    }

    // Trường hợp 2: Có đúng 2 phần tử không khớp -> Kiểm tra hoán đổi (swap)
    if (diffIndices.size() == 2) {
      int l = diffIndices.get(0);
      int r = diffIndices.get(1);

      // Thử hoán đổi
      int temp = arr[l];
      arr[l] = arr[r];
      arr[r] = temp;

      // Nếu sau khi hoán đổi mảng được sắp xếp
      if (Arrays.equals(arr, sortedArr)) {
        System.out.println("yes");
        // In chỉ số theo quy ước 1-based index
        System.out.println("swap " + (l + 1) + " " + (r + 1));
      } else {
        System.out.println("no");
      }
      return;
    }

    // Trường hợp 3: Có nhiều hơn 2 phần tử không khớp -> Kiểm tra đảo ngược (reverse)
    int l = diffIndices.get(0);
    int r = diffIndices.get(diffIndices.size() - 1);

    // Tạo một mảng con từ arr để đảo ngược
    int[] subArray = Arrays.copyOfRange(arr, l, r + 1);

    // Đảo ngược mảng con
    for (int i = 0; i < subArray.length / 2; i++) {
      int temp = subArray[i];
      subArray[i] = subArray[subArray.length - 1 - i];
      subArray[subArray.length - 1 - i] = temp;
    }

    // Thay thế đoạn đã đảo ngược vào mảng gốc
    for (int i = 0; i <= (r - l); i++) {
      arr[l + i] = subArray[i];
    }

    // Nếu sau khi đảo ngược mảng được sắp xếp
    if (Arrays.equals(arr, sortedArr)) {
      System.out.println("yes");
      // In chỉ số theo quy ước 1-based index
      System.out.println("reverse " + (l + 1) + " " + (r + 1));
    } else {
      System.out.println("no");
    }
  }

}
