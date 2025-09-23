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

public class StringProgress {

  /**
   * Palindrome
   * @param str
   * @return
   */
  private static boolean isPalindrome(String str) {
    if(str == null) {
      return false;
    }
    int left = 0;
    int right = str.length() - 1;
    while (left < right) {
      if(str.charAt(left) != str.charAt(right)) {
        return false;
      }
      left++;
      right--;
    }
    return true;
  }

  private static boolean isAnagramSort(String s1, String s2) {
    if(s1 == null || s2 == null || s1.length() != s2.length()) {
      return false;
    }
    char[] arr1 = s1.toCharArray();
    char[] arr2 = s2.toCharArray();
    Arrays.sort(arr1);
    Arrays.sort(arr2);
    return Arrays.equals(arr1, arr2);
  }

  public static void main(String[] args) {
    String strTest = "abdbat";
    boolean isPalindromeStr = isPalindrome(strTest);
    System.out.print("result check Palindrome: " + isPalindromeStr);
    System.out.println();
    String s1 = "listen";
    String s2 = "silent";
    boolean isAnagramSort2Str = isAnagramSort(s1, s2);
    System.out.print("result check isAnagramSort2Str: " + isAnagramSort2Str);
  }

}
