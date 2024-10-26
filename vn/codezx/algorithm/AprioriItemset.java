package vn.codezx.algorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AprioriItemset {
 private List<Set<String>> transactions;
 private int minSupport;

 public AprioriItemset(List<Set<String>> transactions, int minSupport) {
  this.transactions = transactions;
  this.minSupport = minSupport;
 }

 public List<ItemSet> run() {
  List<ItemSet> frequent = new ArrayList<>();
  List<ItemSet> initFrequent = new ArrayList<>();
  List<ItemSet> candidate = new ArrayList<>();
  int k = 2;

  // Khởi tạo tập itemset đầu tiên
  List<Set<String>> initItemSets = initialItemsets(); //L
  initFrequent =  getFrequentItemsets(initItemSets);

  while (!initFrequent.isEmpty()) {
   // Lấy itemset phổ biến
   //List<ItemSet> frequentItemset = getFrequentItemsets(currentItemsets);
   //frequent.addAll(frequentItemset);
   // Tạo itemset tiếp theo
   //currentItemsets = generateNextItemsets(frequentItemset, k);
   //k++;
  }

  return frequent;
 }

 private List<Set<String>> initialItemsets() {
  Set<String> uniqueItems = new HashSet<>();
  for (Set<String> transaction : transactions) {
   uniqueItems.addAll(transaction);
  }

  List<Set<String>> itemsets = new ArrayList<>();
  for (String item : uniqueItems) {
   itemsets.add(new HashSet<>(Collections.singletonList(item)));
  }
  return itemsets;
 }

 private List<ItemSet> getFrequentItemsets(List<Set<String>> itemsets) {
  Map<Set<String>, Integer> itemsetCount = new HashMap<>();
  List<ItemSet> frequentItemsets = new ArrayList<>();
  // Đếm số lần xuất hiện của từng itemset
  for (Set<String> transaction : transactions) {
   for (Set<String> itemset : itemsets) {
    if (transaction.containsAll(itemset)) {
     itemsetCount.put(itemset, itemsetCount.getOrDefault(itemset, 0) + 1);
     System.out.print(itemset);
     System.out.println(itemsetCount.getOrDefault(itemset, 0));
    }
   }
  }

  //int minCount = minSupport;
  for (Map.Entry<Set<String>, Integer> entry : itemsetCount.entrySet()) {
   if (entry.getValue() >= minSupport) {
    // Tính độ support
    //double support = (double) entry.getValue() / totalTransactions;
    frequentItemsets.add(new ItemSet(entry.getKey(), entry.getValue()));
   }
  }
  return frequentItemsets;
 }

 private List<Set<String>> generateNextItemsets(List<ItemSet> frequentItemsets, int k) {
  List<Set<String>> newItemsets = new ArrayList<>();
  for (int i = 0; i < frequentItemsets.size(); i++) {
   for (int j = i + 1; j < frequentItemsets.size(); j++) {
    Set<String> first = frequentItemsets.get(i).getItems();
    Set<String> second = frequentItemsets.get(j).getItems();
    Set<String> union = new HashSet<>(first);
    union.addAll(second);

    // Kiểm tra xem kích thước của union có bằng k + 1 hay không
    if (union.size() == k + 1) {
     newItemsets.add(union);
    }
   }
  }
  return newItemsets;
 }

 public static void main(String[] args) {
  // Ví dụ dữ liệu giao dịch
  List<Set<String>> transactions = Arrays.asList(
      new HashSet<>(Arrays.asList("A", "C", "T", "W")),
      new HashSet<>(Arrays.asList("C", "D", "W")),
      new HashSet<>(Arrays.asList("A", "C", "T", "W")),
      new HashSet<>(Arrays.asList("A", "C", "D", "W")),
      new HashSet<>(Arrays.asList("A", "C", "D", "T", "W")),
      new HashSet<>(Arrays.asList("C", "D", "T"))
  );

  int minSupport = (50 * transactions.size()) /100 ;
  AprioriItemset apriori = new AprioriItemset(transactions, minSupport);
  List<ItemSet> frequentItemsets = apriori.run();

  System.out.println("Frequent Itemsets with Support:");
  for (ItemSet itemset : frequentItemsets) {
   System.out.println(itemset.getItems() + " - Support: " + itemset.getSupport());
  }
 }
}
