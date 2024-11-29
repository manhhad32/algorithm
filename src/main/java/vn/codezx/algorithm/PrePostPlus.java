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
  private static int minSupport = 0;

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

    // init Tree.
    PPCTree tree = new PPCTree();

    try {
      // count frequent each item set
      Map<String, Integer> frequencyMap = new HashMap<>();
      Files.lines(filePath).forEach(line -> {
        List<String> items = Arrays.asList(line.split(REGEX_SPLIT_WORD));
        for (String item : items) {
          frequencyMap.put(item, frequencyMap.getOrDefault(item, 0) + 1);
        }
      });

      // calculate minSupport
      minSupport = (int) (0.4 * Files.lines(filePath).count());
      tree.setMinSupport(minSupport);
      // Read each transaction and build PPCTree.
      Files.lines(filePath).forEach(line -> {
        List<String> items = Arrays.asList(line.split(REGEX_SPLIT_WORD));
        List<String> filteredTransaction = filterAndSortTransaction(items, frequencyMap);
        tree.addTransaction(filteredTransaction); // Xây dựng cây PPC
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    // assign preOrder, postOrder in tree.
    tree.assignPrePostOrder(tree.root);

    // Display PPCTree
    //System.out.println("PPC-Tree with PreOrder and PostOrder:");
    //tree.displayTree(tree.root, "");

    List<List<String>> itemFrequencies = new ArrayList<>();
    // genaration N-lists for each single item -- F1
    List<List<PPCNode>> nList = tree.genrateNList(itemFrequencies);
    System.out.print("\nN-lzists:\n");

    printNList(nList);

    // genaration N-lists for each single item -- F2
   List<List<PPCNode>> nLists2 = tree.generateNewPPCCode(nList, itemFrequencies);

    System.out.print("\nN-lists F2:\n");

    printNList(nLists2);

    nList.clear();
    System.gc();
    // genaration N-lists for each single item -- F3
    List<List<PPCNode>> nLists3 = tree.generateNewPPCCode(nLists2, itemFrequencies);
    System.out.print("\nN-lists F3:\n");
    printNList(nLists3);
    for(List<String> itemFrequency : itemFrequencies) {
      System.out.println(itemFrequency);
    }

  }
  private static void printNList(List<List<PPCNode>> nList) {
    for (List<PPCNode> node : nList) {
      int size = node.size();
      for (int i = 0; i < size; i ++) {
        String info = "<(" + node.get(i).preOrder + ","
            + node.get(i).postOrder + "):" + node.get(i).count + ">";
        if(i == 0) {
          info = node.get(i).itemID + "-->" + info;
        }
        System.out.print(info);

      }

      System.out.println();
    }
  }
}
