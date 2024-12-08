package vn.codezx.algorithm;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PrePostPlus {
  private static final double THRESHOLD_XI = 0.01;
  private static final String DATA_FILE = "kosarak.dat";
  private static final String PATH_FILE = "data/" + DATA_FILE;
  private static final String PATH_OUTPUT = "output/" + DATA_FILE;
  private static int preOrderCounter = 0;
  private static int postOrderCounter = 0;
  private static int minSupport = 0;



  public static void main(String[] args) throws IOException {

    double startTime = System.nanoTime();

    Path filePath = Paths.get(PATH_FILE);
    // count frequent each item set
    Map<String, Integer> frequencyMap = new HashMap<>();
    // init Tree.
    PPCNode root = new PPCNode("null");
    try {
      // step 1: Scan database first time
      Files.lines(filePath).forEach(line -> {
        List<String> items = Arrays.asList(line.split(" "));
        for (String item : items) {
          frequencyMap.put(item, frequencyMap.getOrDefault(item, 0) + 1);
        }
      });
      // calculate minSupport
      minSupport = (int) (THRESHOLD_XI * Files.lines(filePath).count());
      System.out.println("minSupport:" + minSupport);

      // step 2: build PPC-Tree
      // Read each transaction and build PPCTree.
      Files.lines(filePath).forEach(line -> {
        List<String> items = Arrays.asList(line.split(" "));
        List<String> filteredTransaction = filterAndSortTransaction(items, frequencyMap, minSupport);
        buildPPCTree(root, filteredTransaction);
      });

    } catch (IOException e) {
      e.printStackTrace();
    }
    // Set the preOrder and postOrder values for the tree
    assignPrePostOrder(root);

    // step 3: build NLists for F1
    Map<String, List<int[]>> nLists = new HashMap<>();
    buildNLists(root, nLists);

    // step 4: build F1
    List<String> frequentItems = new ArrayList<>();
    for (String item : frequencyMap.keySet()) {
      if (frequencyMap.get(item) >= minSupport) {
        frequentItems.add(item);
      }
    }

    // step 5: build Pattern Tree and frequentItems
    List<String> finalFrequentItems;
    finalFrequentItems = buildPatternTree(frequentItems, nLists, minSupport);
    double endTime = System.nanoTime();
    double total = (endTime - startTime) / 1_000_000_000.0;

    // result
    System.out.println("Frequent Itemsets:");
    try (FileWriter writer = new FileWriter(PATH_OUTPUT)) {
      for (String itemset : finalFrequentItems) {

        writer.write(itemset + "\n");
        //writer.append(itemset + "\n");
      }
      writer.write("\nTHRESHOLD_XI: " + THRESHOLD_XI);
      writer.write("\nTotal time: " + total);
      //System.out.println(itemset);
    } catch (Exception e) {
      e.fillInStackTrace();
  }
    System.out.println("Total time: " + total);
  }

  private static void buildPPCTree(PPCNode root, List<String> transaction) {
    PPCNode currentNode = root;
    for (String item : transaction) {
      PPCNode childNode = currentNode.children.stream()
          .filter(child -> child.itemID.equals(item))
          .findFirst()
          .orElse(null);
      if (childNode == null) {
        childNode = currentNode.addChild(item);
      }
      childNode.count++;
      currentNode = childNode;
    }
  }

  private static void buildNLists(PPCNode node, Map<String, List<int[]>> nLists) {
    for (PPCNode child : node.children) {
      // Add the node's information (preOrder, postOrder, and count) to nLists
      nLists.putIfAbsent(child.itemID, new ArrayList<>());
      nLists.get(child.itemID).add(new int[]{child.preOrder, child.postOrder, child.count});

      // Recursively build the NLists for child nodes
      buildNLists(child, nLists);
    }
  }

  private static List<int[]> NLintersection(List<int[]> nl1, List<int[]> nl2) {
    List<int[]> intersection = new ArrayList<>();
    int i = 0, j = 0;
    while (i < nl1.size() && j < nl2.size()) {
      int[] n1 = nl1.get(i);
      int[] n2 = nl2.get(j);

      if (n1[0] < n2[0]) { // preOrder matches
        if (n1[1] > n2[1]) { // postOrder matches
          intersection.add(new int[]{n1[0], n1[1], n2[2]});
          j++;
          i++;
        } else {
          i++;
        }
      } else {
        j++;
      }
      if((n1[0] == n2[0]) && (n1[1] == n2[1])) {
        for(int k = 0 ; k < intersection.size(); k++) {
          if((intersection.get(k)[0] == n1[0]) && (intersection.get(k)[1] == n1[1])) {
            intersection.add(new int[]{n1[0], n1[1], n1[2] + intersection.get(k)[2]});
            intersection.remove(k);
          }
        }
      }
    }

    return intersection;
  }

  private static List<String> buildPatternTree(List<String> frequentItems, Map<String, List<int[]>> nLists, int minSupport) {
    List<String> finalFrequentItems = new ArrayList<>(frequentItems);
    Set<List<String>> uniqueTran = new HashSet<>();
    // Duyệt qua tất cả cặp items trong frequentItems
    for (int i = 0; i < frequentItems.size(); i++) {
      String item1 = frequentItems.get(i);
      for (int j = i + 1; j < frequentItems.size(); j++) {
        String item2 = frequentItems.get(j);
        List<int[]> nl1 = nLists.get(item1);
        List<int[]> nl2 = nLists.get(item2);
        List<int[]> intersection = NLintersection(nl1, nl2);

        // Tính toán support của itemset {item1, item2} từ intersection
        int supportCount = 0;
        for (int[] item : intersection) {
          supportCount += item[2];
        }
        if (supportCount >= minSupport) { // Kiểm tra support có thỏa mãn minSupport
          String newPattern = item1 + "," + item2;
          List<String> arrNewPattern = new ArrayList<>();
          arrNewPattern.add(item1);
          arrNewPattern.add(item2);
          arrNewPattern.sort(String::compareTo);
          if (uniqueTran.add(arrNewPattern)) {
            nLists.put(newPattern, intersection);
            finalFrequentItems.add(newPattern);
            PPCNode patternRoot = new PPCNode(newPattern);
            buildPatternTreeHelper(patternRoot, intersection, nLists, frequentItems, finalFrequentItems, minSupport, uniqueTran);
          }
        }
      }
    }
    return finalFrequentItems;
  }

  private static void buildPatternTreeHelper(PPCNode node, List<int[]> intersection, Map<String, List<int[]>> nLists,
      List<String> frequentItems, List<String> finalFrequentItems, int minSupport, Set<List<String>> uniqueTran) {
    // Duyệt qua từng item trong frequentItems để xây dựng cây mẫu
    for (String item : frequentItems) {
      if (!node.itemID.contains(item)) {  // Đảm bảo item không bị trùng
        List<int[]> itemNList = nLists.get(item);
        if (itemNList != null) {
          // Tính giao nhau của intersection và itemNList
          List<int[]> newIntersection = NLintersection(intersection, itemNList);

          // Kiểm tra support của itemset mới
          if (!newIntersection.isEmpty()) {
            int supportCount = 0; // Số lượng phần tử giao nhau
            for (int[] ints : newIntersection) {
              supportCount += ints[2];
            }
            if (supportCount >= minSupport) { // Kiểm tra support
              String newPattern = node.itemID + "," + item;
              List<String> arrNewPattern = new ArrayList<>();
              arrNewPattern.addAll(Arrays.asList(node.itemID.split(",")));
              arrNewPattern.add(item);
              arrNewPattern.sort(String::compareTo);
              if (uniqueTran.add(arrNewPattern)) {
                nLists.put(newPattern, newIntersection);
                finalFrequentItems.add(newPattern);
                PPCNode childNode = node.addChild(newPattern);
                buildPatternTreeHelper(childNode, newIntersection, nLists, frequentItems, finalFrequentItems, minSupport, uniqueTran);
              }
            }
          }
        }
      }
    }
  }
  private static void assignPrePostOrder(PPCNode node) {
    if (node == null) {
      return;
    }
    node.preOrder = preOrderCounter++;
    for (PPCNode child : node.children) {
      assignPrePostOrder(child);
    }
    node.postOrder = postOrderCounter++;
  }
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
}
