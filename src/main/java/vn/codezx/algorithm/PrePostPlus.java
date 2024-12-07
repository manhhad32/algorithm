package vn.codezx.algorithm;


import java.io.BufferedReader;
import java.io.FileReader;
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

  private static final double THRESHOLD_XI = 0.4; // Ngưỡng tần suất tối thiểu
  private static final String DATA_FILE = "data/mushroom.dat";
  public static class PPCNode {
    String itemID;
    int count;
    int preOrder;
    int postOrder;
    List<PPCNode> children = new ArrayList<>();

    public PPCNode(String itemID) {
      this.itemID = itemID;
      this.count = 0;
    }

    public PPCNode addChild(String itemID) {
      PPCNode child = new PPCNode(itemID);
      this.children.add(child);
      return child;
    }
  }

  public static void main(String[] args) throws IOException {
    // Bước 1: Scan database lần 1
    List<List<String>> transactions = readTransactions(DATA_FILE);

    double startTime = System.nanoTime();
    Map<String, Integer> itemSupport = new HashMap<>();
    int totalTransactions = 0;

    for (List<String> transaction : transactions) {
      totalTransactions++;
      for (String item : transaction) {
        itemSupport.put(item, itemSupport.getOrDefault(item, 0) + 1);
      }
    }
    int minSupport = (int) Math.ceil(THRESHOLD_XI * totalTransactions);
    // Loại bỏ các item không thỏa mãn minsupport và sắp xếp transaction
    for (List<String> transaction : transactions) {
      transaction.removeIf(item -> itemSupport.get(item) < minSupport);
      transaction.sort((a, b) -> itemSupport.get(b) - itemSupport.get(a)); // Giảm dần
    }

    // Bước 2: xây dựng PPC-Tree
    PPCNode root = new PPCNode("null");
    for (List<String> transaction : transactions) {
      buildPPCTree(root, transaction);
    }

    // Bước 3: Tạo NLists cho F1
    Map<String, List<int[]>> nLists = new HashMap<>();
    buildNLists(root, nLists, new int[]{0});

    // Bước 4: Tìm tập phổ biến F1
    List<String> frequentItems = new ArrayList<>();
    for (String item : itemSupport.keySet()) {
      if (itemSupport.get(item) >= minSupport) {
        frequentItems.add(item);
      }
    }

    // Bước 5: Xây dựng Pattern Tree và tìm các tập phổ biến
    List<String> finalFrequentItems;
    finalFrequentItems = buildPatternTree(frequentItems, nLists, minSupport);
    double endTime = System.nanoTime();
    double total = (endTime - startTime)/1000_000_000.00;
    // In kết quả
    System.out.println("Frequent Itemsets:");
    for (String itemset : finalFrequentItems) {
      System.out.println(itemset);
    }
    System.out.println("total time: "+ total);
  }

  private static List<List<String>> readTransactions(String filePath) throws IOException {
    List<List<String>> transactions = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        transactions.add(new ArrayList<>(Arrays.asList(line.split(" "))));
      }
    }
    return transactions;
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

  private static void buildNLists(PPCNode node, Map<String, List<int[]>> nLists, int[] preOrderCounter) {
    node.preOrder = preOrderCounter[0]++;
    for (PPCNode child : node.children) {
      buildNLists(child, nLists, preOrderCounter);
    }
    node.postOrder = preOrderCounter[0];
    nLists.putIfAbsent(node.itemID, new ArrayList<>());
    nLists.get(node.itemID).add(new int[]{node.preOrder, node.postOrder, node.count});
  }

  private static List<int[]> NLintersection(List<int[]> nl1, List<int[]> nl2) {
    List<int[]> intersection = new ArrayList<>();
    int i = 0, j = 0;
    while (i < nl1.size() && j < nl2.size()) {
      int[] n1 = nl1.get(i);
      int[] n2 = nl2.get(j);

      if (n1[0] <= n2[0]) { // preOrder matches
        if (n1[1] >= n2[1]) { // postOrder matches
          intersection.add(new int[]{n1[0], n1[1], n2[2]});
          i++;
          j++;
        } else if (n1[1] < n2[1]) {
          i++;
        } else {
          j++;
        }
      } else if (n1[0] < n2[0]) {
        i++;
      } else {
        j++;
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
        int supportCount  = 0;
        for(int[] item : intersection) {
          supportCount += item[2];
        }
        if (supportCount >= minSupport) { // Kiểm tra support có thỏa mãn minSupport
          String newPattern = item1 + "," + item2;
          List<String> arrNewPattern = new ArrayList<>();
          arrNewPattern.add(item1);
          arrNewPattern.add(item2);
          arrNewPattern.sort(String::compareTo);
          if(uniqueTran.add(arrNewPattern)) {
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
            for(int[] ints : newIntersection){
              supportCount += ints[2];
            }
            if (supportCount >= minSupport) { // Kiểm tra support
              String newPattern = node.itemID + "," + item;
              List<String> arrNewPattern = new ArrayList<>();
              arrNewPattern.addAll(Arrays.asList(node.itemID.split(",")));
              arrNewPattern.add(item);
              arrNewPattern.sort(String::compareTo);
              if(uniqueTran.add(arrNewPattern)) {
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



}
