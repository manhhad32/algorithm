package vn.codezx.algorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrePostPlus {

  private static final int minSupport = 2;

  // Step 1: Count item frequency in transactions to determine F1
  private static Map<String, Integer> getItemFrequency(List<List<String>> transactions) {
    Map<String, Integer> frequencyMap = new HashMap<>();
    for (List<String> transaction : transactions) {
      for (String item : transaction) {
        frequencyMap.put(item, frequencyMap.getOrDefault(item, 0) + 1);
      }
    }
    return frequencyMap;
  }

  // Step 2: Filter items by minSupport and sort each transaction by frequency
  private static List<List<String>> getFrequentTransactions(List<List<String>> transactions,
      Map<String, Integer> frequencyMap) {
    List<List<String>> filteredTransactions = new ArrayList<>();
    for (List<String> transaction : transactions) {
      List<String> filtered = new ArrayList<>();
      for (String item : transaction) {
        if (frequencyMap.getOrDefault(item, 0) >= minSupport) {
          filtered.add(item);
        }
      }
      filtered.sort(
          (i1, i2) -> frequencyMap.get(i2) - frequencyMap.get(i1)); // Sort by frequency descending
      filteredTransactions.add(filtered);
    }
    return filteredTransactions;
  }

  public static void main(String[] args) {
    // Example transactions
    List<List<String>> transactions = Arrays.asList(
        /*Arrays.asList("A", "C", "T", "W"),
        Arrays.asList("C", "D", "W"),
        Arrays.asList("A", "C", "T", "W"),
        Arrays.asList("A", "C", "D", "W"),
        Arrays.asList("A", "C", "D", "T", "W"),
        Arrays.asList("C", "D", "T")

         */
        Arrays.asList("A", "F", "G"),
        Arrays.asList("A", "B", "C", "E"),
        Arrays.asList("B", "C", "E", "I"),
        Arrays.asList("B", "C", "E", "H"),
        Arrays.asList("B", "C", "D", "E", "F")
    );

    // Step 1: Calculate item frequency
    Map<String, Integer> frequencyMap = getItemFrequency(transactions);

    // Step 2: Filter transactions by minSupport and sort items within each transaction
    List<List<String>> filteredTransactions = getFrequentTransactions(transactions, frequencyMap);

    // Step 3: Build the PPC-Tree
    PPCTree tree = new PPCTree();
    for (List<String> transaction : filteredTransactions) {
      tree.addTransaction(transaction);
    }

    // Step 4: Assign PreOrder and PostOrder numbers
    tree.assignPrePostOrder(tree.root);

    // Step 5: Display the PPC-Tree with PreOrder and PostOrder numbers
    System.out.println("PPC-Tree with PreOrder and PostOrder:");
    tree.displayTree(tree.root, "");

    // Additional Steps: Implement N-lists and use PrePost+ to find frequent itemsets
    // This would involve creating and using N-lists to calculate intersections and patterns
    // Step 6: Generate N-lists for each frequent item
    Map<String, List<PPCNode>> nLists = tree.generateNLists();
    System.out.println("\nN-lists:");
    for (Map.Entry<String, List<PPCNode>> entry : nLists.entrySet()) {
      System.out.println("Item: " + entry.getKey());
      for (PPCNode node : entry.getValue()) {
        System.out.println(
            "    Node - PreOrder: " + node.preOrder + ", PostOrder: " + node.postOrder + ", Count: "
                + node.count);
      }
    }
  }
}
