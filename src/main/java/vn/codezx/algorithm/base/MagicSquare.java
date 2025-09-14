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

/**
 * Ma Trận Ma Thuật là gì?
 * Ma trận ma thuật (Magic Square) là một ma trận vuông (ví dụ: 3x3) thỏa mãn các điều kiện sau:
 * Chứa các số nguyên riêng biệt (thường là từ 1 đến 9 đối với ma trận 3x3).
 * Tổng các số trên mỗi hàng, mỗi cột, và hai đường chéo chính đều bằng nhau.
 * Giá trị tổng chung này được gọi là hằng số ma thuật. Đối với ma trận 3x3 chứa các số từ 1 đến 9, hằng số (SUM_MAGIC) này luôn là 15.
 * SUM_MAGIC = N*(N*N +1)/2
 * ---
 * What is a Magic Square?
 * A magic square is a square matrix (e.g., 3x3) that satisfies the following conditions:
 * Contains distinct integers (usually 1 through 9 for a 3x3 matrix).
 * The sum of the numbers in each row, each column, and the two main diagonals is the same.
 * This overall value is called the magic constant. For a 3x3 matrix containing the numbers 1 through 9, this constant(SUM_MAGIC) is always 15.
 * SUM_MAGIC = N*(N*N +1)/2
 * ---
 * Mục tiêu của bài toán
 * Bạn được cho một ma trận 3x3 chứa các số nguyên bất kỳ. Nhiệm vụ của bạn là biến đổi ma trận này thành một ma trận ma thuật hợp lệ bằng cách thay đổi các giá trị trong ô.
 * Mục tiêu cuối cùng là tìm ra chi phí tối thiểu để thực hiện việc biến đổi này.
 * ---
 * Problem Objective
 * You are given a 3x3 matrix containing arbitrary integers. Your task is to transform this matrix into a valid magic matrix by changing the values ​​in the cells.
 * The ultimate goal is to find the minimum cost to perform this transformation.
 * ---
 * Cách tính chi phí
 * Chi phí được tính như sau:
 * Chi phí để thay đổi một số a (trong ma trận ban đầu) thành một số b (trong ma trận ma thuật) là |a - b| (giá trị tuyệt đối của hiệu).
 * Tổng chi phí là tổng của tất cả các chi phí thay đổi ở mỗi ô trong 9 ô của ma trận.
 * ---
 * Calculating Cost
 * The cost is calculated as follows:
 * The cost of changing a number a (in the original matrix) to a number b (in the magic matrix) is |a - b| (the absolute value of the difference).
 * The total cost is the sum of all the costs of changes in each of the 9 cells of the matrix.
 */
public class MagicSquare {
  private static final int N = 3;
  private static final int MAGIC_SUM = N*((N*N) + 1)/2;

  private static final List<int[][]> listMagicSquare = new ArrayList<>();
  private static final int[][] square = new int[N][N];
  private static final boolean[] useNumbers = new boolean[N*N +1];

  private static void createMagicSquare(int row, int col) {
    if(row == N){
      if(isDiagMagic()) {
        int[][] tmpMagicSquare = new int[N][];
        for(int i = 0; i < N; i++) {
          tmpMagicSquare[i] = Arrays.copyOf(square[i], N);
        }
        listMagicSquare.add(tmpMagicSquare);
      }
      return;
    }
    int nextRow = (col == N - 1) ? row + 1 : row;
    int nextCol = (col == N -1) ? 0 : col + 1;

    for(int num = 1; num <= N*N; num++) {
      if (!useNumbers[num]) {
        square[row][col] = num;
        useNumbers[num] = true;

        if (col == N - 1) {
          if (getSumRow(row) != MAGIC_SUM) {
            useNumbers[num] = false;
            continue;
          }
        }
        if (row == N - 1) {
          if (getSumCol(col) != MAGIC_SUM) {
            useNumbers[num] = false;
            continue;
          }
        }
        createMagicSquare(nextRow, nextCol);
        useNumbers[num] = false;
      }
    }
  }

  private static int getSumRow(int row) {
    int sumRow = 0;
    for(int j = 0; j < N; j++) {
      sumRow += square[row][j];
    }
    return sumRow;
  }
  private static int getSumCol(int col) {
    int sumCol = 0;
    for(int i = 0; i < N; i++) {
      sumCol += square[i][col];
    }
    return sumCol;
  }

  private static boolean isDiagMagic() {
    int mainDiagSum = 0;
    for(int i = 0; i < N; i++){
      mainDiagSum += square[i][i];
    }
    if(mainDiagSum != MAGIC_SUM) {
      return false;
    }
    int diagSum = 0;
    for(int i = 0; i < N; i++){
      diagSum += square[i][N-1 -i];
    }
    if(diagSum != MAGIC_SUM) {
      return false;
    }
    return true;
  }


  private static int forminMagicSquare(List<List<Integer>> s) {
    createMagicSquare(0, 0);
    int minCost = Integer.MAX_VALUE;

    for(int[][] magicSquare : listMagicSquare) {
      int currentCost = 0;
      for(int i = 0; i < N; i++) {
        for(int j = 0; j < N; j++) {
          currentCost += Math.abs(s.get(i).get(j) - magicSquare[i][j]);
        }
      }
      minCost = Math.min(minCost, currentCost);
    }
    return minCost;
  }


  public static void main(String[] args) {
    List<List<Integer>> sq = List.of(
        List.of(8, 3, 5),
        List.of(1, 5, 9),
        List.of(6, 7, 2)
    );

    int minCost = forminMagicSquare(sq);

    for(int[][] magicSquare : listMagicSquare){
      for(int i = 0; i < N; i++) {
        for(int j = 0; j < N; j++) {
          System.out.print(magicSquare[i][j] + " ");
        }
        System.out.println();
      }
      System.out.println("--------------------");
    }

    System.out.println("Min Cost: "+ minCost);
  }

}
