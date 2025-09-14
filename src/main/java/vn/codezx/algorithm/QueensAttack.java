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


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueensAttack {
  public static int queensAttack(int n, int k, int r_q, int c_q, List<List<Integer>> obstacles) {
    // Tọa độ của các vật cản gần nhất trên 8 hướng
    // [0] là hàng, [1] là cột
    int[] closestObstacleUp = null;
    int[] closestObstacleDown = null;
    int[] closestObstacleLeft = null;
    int[] closestObstacleRight = null;
    int[] closestObstacleUpLeft = null;
    int[] closestObstacleUpRight = null;
    int[] closestObstacleDownLeft = null;
    int[] closestObstacleDownRight = null;

    // Duyệt qua tất cả các chướng ngại vật để tìm ra vật cản gần nhất trên mỗi hướng
    for (List<Integer> obstacleCoords : obstacles) {
      int r_o = obstacleCoords.get(0);
      int c_o = obstacleCoords.get(1);

      // 1. Cùng cột với hậu
      if (c_o == c_q) {
        if (r_o > r_q) { // Hướng Lên (Up)
          if (closestObstacleUp == null || r_o < closestObstacleUp[0]) {
            closestObstacleUp = new int[]{r_o, c_o};
          }
        } else { // Hướng Xuống (Down)
          if (closestObstacleDown == null || r_o > closestObstacleDown[0]) {
            closestObstacleDown = new int[]{r_o, c_o};
          }
        }
      }
      // 2. Cùng hàng với hậu
      else if (r_o == r_q) {
        if (c_o > c_q) { // Hướng Phải (Right)
          if (closestObstacleRight == null || c_o < closestObstacleRight[1]) {
            closestObstacleRight = new int[]{r_o, c_o};
          }
        } else { // Hướng Trái (Left)
          if (closestObstacleLeft == null || c_o > closestObstacleLeft[1]) {
            closestObstacleLeft = new int[]{r_o, c_o};
          }
        }
      }
      // 3. Nằm trên đường chéo
      else if (Math.abs(r_o - r_q) == Math.abs(c_o - c_q)) {
        if (r_o > r_q && c_o > c_q) { // Hướng Chéo Lên-Phải (Up-Right)
          if (closestObstacleUpRight == null || r_o < closestObstacleUpRight[0]) {
            closestObstacleUpRight = new int[]{r_o, c_o};
          }
        } else if (r_o > r_q && c_o < c_q) { // Hướng Chéo Lên-Trái (Up-Left)
          if (closestObstacleUpLeft == null || r_o < closestObstacleUpLeft[0]) {
            closestObstacleUpLeft = new int[]{r_o, c_o};
          }
        } else if (r_o < r_q && c_o < c_q) { // Hướng Chéo Xuống-Trái (Down-Left)
          if (closestObstacleDownLeft == null || r_o > closestObstacleDownLeft[0]) {
            closestObstacleDownLeft = new int[]{r_o, c_o};
          }
        } else { // Hướng Chéo Xuống-Phải (Down-Right)
          if (closestObstacleDownRight == null || r_o > closestObstacleDownRight[0]) {
            closestObstacleDownRight = new int[]{r_o, c_o};
          }
        }
      }
    }

    int attackableSquares = 0;

    // Tính số ô tấn công được cho mỗi hướng
    // Nếu có vật cản, khoảng cách = (tọa độ vật cản - tọa độ hậu - 1)
    // Nếu không, khoảng cách = (tọa độ cạnh bàn cờ - tọa độ hậu)

    // Hướng Lên
    attackableSquares += (closestObstacleUp != null) ? (closestObstacleUp[0] - r_q - 1) : (n - r_q);
    // Hướng Xuống
    attackableSquares += (closestObstacleDown != null) ? (r_q - closestObstacleDown[0] - 1) : (r_q - 1);
    // Hướng Phải
    attackableSquares += (closestObstacleRight != null) ? (closestObstacleRight[1] - c_q - 1) : (n - c_q);
    // Hướng Trái
    attackableSquares += (closestObstacleLeft != null) ? (c_q - closestObstacleLeft[1] - 1) : (c_q - 1);

    // Hướng Chéo Lên-Phải
    attackableSquares += (closestObstacleUpRight != null) ? (closestObstacleUpRight[0] - r_q - 1) : Math.min(n - r_q, n - c_q);
    // Hướng Chéo Lên-Trái
    attackableSquares += (closestObstacleUpLeft != null) ? (closestObstacleUpLeft[0] - r_q - 1) : Math.min(n - r_q, c_q - 1);
    // Hướng Chéo Xuống-Trái
    attackableSquares += (closestObstacleDownLeft != null) ? (r_q - closestObstacleDownLeft[0] - 1) : Math.min(r_q - 1, c_q - 1);
    // Hướng Chéo Xuống-Phải
    attackableSquares += (closestObstacleDownRight != null) ? (r_q - closestObstacleDownRight[0] - 1) : Math.min(r_q - 1, n - c_q);

    return attackableSquares;
  }

  // --- Hàm main để đọc input và chạy thử ---
  public static void main(String[] args) throws IOException {
    // n: kích thước bàn cờ
    int n = 5;

    // r_q: hàng của hậu, c_q: cột của hậu
    int r_q = 4;
    int c_q = 3;

    // Danh sách các chướng ngại vật
    List<List<Integer>> obstacles = new ArrayList<>();
    obstacles.add(List.of(5, 5));
    obstacles.add(List.of(4, 2));
    obstacles.add(List.of(2, 3));

    // k: số lượng vật cản, có thể lấy từ kích thước của list
    int k = obstacles.size();

    // Gọi hàm và in kết quả
    int result = queensAttack(n, k, r_q, c_q, obstacles);

    System.out.println("Result: " + result); // Kết quả mong đợi là 10

  }
}
