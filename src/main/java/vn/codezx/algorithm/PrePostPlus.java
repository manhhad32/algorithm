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
  private static final String NAME_DATA_TEST = "data/data-paper.dat";
  private static final double THRESHOLD_XI = 0.4;
  private static int F_LEVEL = 3;
  private static final boolean DISPLAY_PPC_TREE = true;


  // Lọc item theo minSupport và sắp xếp từng giao dịch theo tần suất
  private static List<String> filterAndSortTransaction(List<String> transaction, Map<String, Integer> frequencyMap, int minSupport) {
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
    Path filePath = Paths.get(NAME_DATA_TEST);

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
      int minSupport = (int) (THRESHOLD_XI * Files.lines(filePath).count());
      tree.setMinSupport(minSupport);
      // Read each transaction and build PPCTree.
      Files.lines(filePath).forEach(line -> {
        List<String> items = Arrays.asList(line.split(REGEX_SPLIT_WORD));
        List<String> filteredTransaction = filterAndSortTransaction(items, frequencyMap, minSupport);
        tree.addTransaction(filteredTransaction); // Xây dựng cây PPC
      });

    } catch (IOException e) {
      e.printStackTrace();
    }

    // assign preOrder, postOrder in tree.
    tree.assignPrePostOrder(tree.root);

    // Display PPCTree
    if(DISPLAY_PPC_TREE) {
      System.out.println("PPC-Tree with PreOrder and PostOrder:");
      tree.displayTree(tree.root, "");
    }

    List<List<String>> itemFrequencies = new ArrayList<>();
    genNListFnFrequency(tree, F_LEVEL, itemFrequencies);
    System.out.print("\nFrequency Items:\n");
    for(List<String> itemFrequency : itemFrequencies) {
      System.out.println(itemFrequency);
    }
  }

  private static void genNListFnFrequency(PPCTree tree, int fn, List<List<String>> itemFrequencies) {
    List<List<PPCNode>> nListsFn;
    nListsFn = tree.genrateNList(itemFrequencies);
    System.out.print("\nN-lists F1:\n");
    printNList(nListsFn);
    int count = 0;
    while (fn > 1) {
      nListsFn = tree.generateNewPPCCode(nListsFn, itemFrequencies);
      int idx = count + 2;
      System.out.print("\nN-lists F"+idx+ ":" + "\n");
      printNList(nListsFn);
      count++;
      fn--;
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
