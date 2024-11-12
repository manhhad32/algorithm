package vn.codezx.algorithm;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PrePostPlus {

  private static final String REGEX_SPLIT_WORD = " ";
  private static double minSupport = 0;

  // Đếm tần suất item trong giao dịch để xác định F1
  private static Map<String, Integer> getItemFrequency(List<List<String>> transactions) {
    Map<String, Integer> frequencyMap = new HashMap<>();
    for (List<String> transaction : transactions) {
      for (String item : transaction) {
        frequencyMap.put(item, frequencyMap.getOrDefault(item, 0) + 1);
      }
    }
    return frequencyMap;
  }

  // Lọc item theo minSupport và sắp xếp từng giao dịch theo tần suất
  private static List<String> filterAndSortTransaction(List<String> transaction, Map<String, Integer> frequencyMap) {
    List<String> filtered = new ArrayList<>();
    for (String item : transaction) {
      if (frequencyMap.getOrDefault(item, 0) >= minSupport) {
        filtered.add(item);
      }
    }
    filtered.sort((i1, i2) -> frequencyMap.get(i2) - frequencyMap.get(i1)); // Sắp xếp theo tần suất giảm dần
    return filtered;
  }

  public static void main(String[] args) {
    Path filePath = Paths.get("data/data-paper.dat");

    // Khởi tạo cây PPC
    PPCTree tree = new PPCTree();

    try {
      // Đếm tần suất từng item qua toàn bộ giao dịch
      Map<String, Integer> frequencyMap = new HashMap<>();
      Files.lines(filePath).forEach(line -> {
        List<String> items = Arrays.asList(line.split(REGEX_SPLIT_WORD));
        for (String item : items) {
          frequencyMap.put(item, frequencyMap.getOrDefault(item, 0) + 1);
        }
      });

      // Cập nhật giá trị minSupport dựa trên ngưỡng
      minSupport = 0.4 * Files.lines(filePath).count();

      // Đọc từng dòng và xử lý giao dịch ngay lập tức
      Files.lines(filePath).forEach(line -> {
        List<String> items = Arrays.asList(line.split(REGEX_SPLIT_WORD));
        List<String> filteredTransaction = filterAndSortTransaction(items, frequencyMap);
        tree.addTransaction(filteredTransaction); // Xây dựng cây PPC
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    // Gán PreOrder và PostOrder cho các nút trong cây
    tree.assignPrePostOrder(tree.root);

    // Hiển thị cây PPC với PreOrder và PostOrder
    System.out.println("PPC-Tree with PreOrder and PostOrder:");
    tree.displayTree(tree.root, "");

    // Sinh N-lists cho mỗi item
    Map<String, List<PPCNode>> nLists = tree.generateNLists();
    System.out.println("\nN-lists:");
    for (Map.Entry<String, List<PPCNode>> entry : nLists.entrySet()) {
      System.out.println("Item: " + entry.getKey());
      for (PPCNode node : entry.getValue()) {
        System.out.println(
            "    Node : " + node.itemID + " - PreOrder: "+ node.preOrder + ", PostOrder: " + node.postOrder + ", Count: " + node.count
        );
      }
    }
  }

}
