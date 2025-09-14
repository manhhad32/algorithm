package vn.codezx.algorithm;/*
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

public class MagicSquareV1 {

  private static final int N = 3;
  private static final int MAGIC_SUM = N*((N*N) +1)/2;

  private static List<int[][]> listMagicSquare = new ArrayList<>();
  private static int[][] square = new int[N][N];
  private static boolean[] useNumers = new boolean[N * N + 1];

  private static void createMaicSquare(int row, int col) {
    if(row == N) {
      if (isDiagMagic()) {
        int[][] tmpListMagicSquare = new int[N][];
        for(int i = 0; i < N; i++) {
          tmpListMagicSquare[i] = Arrays.copyOf(square[i], N);
        }
        listMagicSquare.add(tmpListMagicSquare);
      }
      return;
    }
    int nextRow = (col == N - 1) ? row + 1 : row;
    int nextCol = (col == N - 1) ? 0 : col + 1;

    for(int num  = 1; num <= N*N ; num++) {
      if(!useNumers[num]) {
        square[row][col] = num;
        useNumers[num] = true;

        if(col == N - 1) {
          if(getSumRow(row) !=MAGIC_SUM) {
            useNumers[num] = false;
            continue;
          }
        }
        if(row == N - 1) {
          if(getSumCol(col) != MAGIC_SUM) {
            useNumers[num] = false;
            continue;
          }
        }
        createMaicSquare(nextRow, nextCol);
        useNumers[num] = false;
      }
    }
  }

  private static int getSumRow(int row) {
    int sum = 0;
    for(int j = 0; j < N; j++) {
      sum += square[row][j];
    }
    return sum;
  }
  private static int getSumCol(int col) {
    int sum = 0;
    for(int i = 0; i < N; i++) {
      sum += square[i][col];
    }
    return sum;
  }
  private static boolean isDiagMagic(){
    int mainDiagSum = 0;
    for(int i = 0; i < N; i++) {
      mainDiagSum += square[i][i];
    }
    if( mainDiagSum != MAGIC_SUM) {
      return false;
    }
    int antiDiagSum = 0;
    for(int i = 0; i < N; i++) {
      antiDiagSum += square[i][N - 1 - i];
    }
    if(antiDiagSum != MAGIC_SUM) {
      return false;
    }
    return true;
  }
  public static int formingMagicSquare(List<List<Integer>> s) {
    createMaicSquare(0, 0);
    int minCost = Integer.MAX_VALUE;

    for(int[][] magicSquare : listMagicSquare){
      int currentCost = 0;
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          currentCost += Math.abs(s.get(i).get(j) - magicSquare[i][j]);
        }
      }
      minCost = Math.min(minCost, currentCost);
    }
    return minCost;
  }

  public static void main(String[] args) {

    List<List<Integer>> sq = List.of(
        List.of(1, 1, 2),
        List.of(3, 5, 3),
        List.of(8, 1, 5)
    );

    int x = formingMagicSquare(sq);

    for (int[][] magic : listMagicSquare) {
      for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          System.out.print(magic[i][j] + " ");
        }
        System.out.println();
      }
      System.out.println("--------------------");
    }

    System.out.println("Min Cost: "+ x);
  }

}
