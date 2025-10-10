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


public class MaximizingRepeatWord {
  public static int maxRepeating(String sequence, String word) {
    int result = 0;
    int currentCount = 0;
    int wordLen = word.length();
    int seqLen = sequence.length();
    if (wordLen > seqLen) {
      return 0;
    }
    int iFirstIdx = sequence.indexOf(word);
    for (int i = iFirstIdx; i <= seqLen - wordLen; i++) {
      String sub = sequence.substring(i, i + wordLen);
      if (sub.equalsIgnoreCase(word)) {
        currentCount++;
        result = Math.max(result, currentCount);
        i += wordLen - 1;
      } else {
        currentCount = 0;
      }
    }
    return result;
  }

  public static void main(String[] arg) {
    String sequence = "aaaaaaaabaaaabaaaabaaaabaaaaba";
    String word = "aaaba";
    int numRepeatW = maxRepeating(sequence, word);
    System.out.println("result: " + numRepeatW);
  }

}
