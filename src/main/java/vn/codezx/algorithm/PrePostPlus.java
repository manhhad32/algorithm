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

    // genaration N-lists for each single item -- F1
    Map<String, List<PPCNode>> nLists = tree.generateNLists();
    List<List<PPCNode>> lnList = new ArrayList<>();
    System.out.print("\nN-lists:\n");

    for (Map.Entry<String, List<PPCNode>> entry : nLists.entrySet()) {
      System.out.print("Item: " + entry.getKey() + "--->");
      List<PPCNode> nodes = new ArrayList<>();
      for (PPCNode node : entry.getValue()) {

        System.out.print(
            "<(" + node.preOrder + "," + node.postOrder + "):" + node.count + ">,"
        );
        nodes.add(node);
      }
      lnList.add(nodes);
      System.out.println();
    }


    // genaration N-lists for each single item -- F2
   List<List<PPCNode>> nLists2 = tree.generateNewPPCCode(lnList);

    System.out.print("\nN-lists F2:\n");

    for (List<PPCNode> entry : nLists2) {
      //System.out.print("Item: " + entry + "--->");
      for (PPCNode node : entry) {
        System.out.print(
            node.itemID + "-->" +"<(" + node.preOrder + "," + node.postOrder + "):"  + node.count + ">,"
        );
      }
      System.out.println();

    }


    nLists.clear();
    lnList.clear();
    System.gc();
    // genaration N-lists for each single item -- F3

    List<List<PPCNode>> nLists3 = tree.generateNewPPCCode(nLists2);

    System.out.print("\nN-lists F3:\n");
    for (List<PPCNode> entry : nLists3) {
      //System.out.print("Item: " + entry + "--->");
      for (PPCNode node : entry) {
        System.out.print(
            node.itemID + "-->" +"<(" + node.preOrder + "," + node.postOrder + "):"  + node.count + ">,"
        );
      }
      System.out.println();

    }

  }
}
