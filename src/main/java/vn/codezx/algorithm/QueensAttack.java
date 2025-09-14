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

/**
 * Problem Summary
 * Tóm tắt bài toán
 * You are given a square chessboard with a queen and some obstacles. Your task is to determine how many squares the queen can attack.
 * Bạn sẽ được cho một bàn cờ vua hình vuông, trên đó có một quân hậu và một số chướng ngại vật. Nhiệm vụ của bạn là xác định có bao nhiêu ô vuông mà quân hậu có thể tấn công.
 * Detailed Description
 * Mô tả chi tiết
 * A queen is standing on a chessboard of size n x n. The rows of the board are numbered from 1 to n, going from bottom to top. The columns are numbered from 1 to n, going from left to right. Each square is identified by a coordinate pair (r, c), which describes the row r and column c where the square is located.
 * Một quân hậu đang đứng trên một bàn cờ vua kích thước n x n. Các hàng của bàn cờ được đánh số từ 1 đến n, đi từ dưới lên trên. Các cột được đánh số từ 1 đến n, đi từ trái sang phải. Mỗi ô vuông được xác định bởi một cặp tọa độ (r, c), mô tả hàng r và cột c nơi ô đó tọa lạc.
 * The queen is standing at position (r_q, c_q). In one move, she can attack any square in any of eight directions (left, right, up, down, and four diagonals).
 * Quân hậu đang đứng tại vị trí (r_q, c_q). Trong một nước đi, nó có thể tấn công bất kỳ ô nào theo một trong tám hướng (trái, phải, trên, dưới, và bốn đường chéo).
 * There are obstacles on the board, each of which prevents the queen from attacking any square behind her on that attack path. For example, an obstacle at position (3, 5) will prevent the queen from attacking squares (3, 5), (2, 6), and (1, 7) (if they are on the same diagonal).
 * Trên bàn cờ có các chướng ngại vật, mỗi chướng ngại vật sẽ ngăn quân hậu tấn công bất kỳ ô nào nằm phía sau nó trên đường tấn công đó. Ví dụ, một chướng ngại vật tại vị trí (3, 5) sẽ ngăn quân hậu tấn công các ô (3, 5), (2, 6), và (1, 7) (nếu chúng nằm trên cùng một đường chéo).
 * The task is, given the queen's position and the positions of all the obstacles, to find and print the number of squares the queen can attack from her position.
 * Nhiệm vụ là, cho vị trí của quân hậu và vị trí của tất cả các chướng ngại vật, hãy tìm và in ra số lượng ô vuông mà quân hậu có thể tấn công từ vị trí của mình.
 * --------
 * function queensAttack(int n, int k, int r_q, int c_q, List<List<Integer>> obstacles)
 * Constraints:
 * Ràng buộc:
 * 0 < n ≤ 10^5
 * 0 ≤ k ≤ 10^5
 * n: số hàng và cột của bàn cờ
 * n: number of rows and columns of the board
 * k: số lượng chướng ngại vật trên bàn cờ
 * k: number of obstacles on the board
 * r_q: số hàng của vị trí quân hậu
 * r_q: Queen's position row number
 * c_q: số cột của vị trí quân hậu
 * c_q: column number of queen position
 * obstacles[k][2]: mỗi phần tử là một mảng gồm 2 số nguyên, thể hiện hàng và cột của một chướng ngại vật
 * obstacles[k][2]: each element is an array of 2 integers, representing the row and column of an obstacle
 */
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
