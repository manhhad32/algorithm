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


public class KMP {
  // KMP substring search function
  public int search(String text, String pattern) {
    int n = text.length();
    int m = pattern.length();
    if (m == 0) return 0;

    int[] lps = computeLPSArray(pattern);
    int i = 0; // pointer for text
    int j = 0; // pointer for pattern

    while (i < n) {
      if (pattern.charAt(j) == text.charAt(i)) {
        i++;
        j++;
      }
      if (j == m) {
        return i - j; // Found pattern at index i - j
      } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
        if (j != 0) {
          j = lps[j - 1];
        } else {
          i++;
        }
      }
    }
    return -1; // Not found
  }

  // Function to build the LPS array
  private int[] computeLPSArray(String pattern) {
    int m = pattern.length();
    int[] lps = new int[m];
    int length = 0; // length of the previous longest prefix suffix
    int i = 1;

    while (i < m) {
      if (pattern.charAt(i) == pattern.charAt(length)) {
        length++;
        lps[i] = length;
        i++;
      } else {
        if (length != 0) {
          length = lps[length - 1];
        } else {
          lps[i] = 0;
          i++;
        }
      }
    }
    return lps;
  }
}
