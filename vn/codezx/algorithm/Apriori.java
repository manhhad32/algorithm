package vn.codezx.algorithm;

import java.util.*;
/*
 * L1 ={item don sau cho thuoc (i) >= }}
 *
 */


public class Apriori {

  // Hàm tạo tập phổ biến L1 từ giao dịch
  private static Map<Set<String>, Integer> createL1(List<List<String>> transactions, int minSupportCount) {
      Map<Set<String>, Integer> L1 = new HashMap<>();

      for (List<String> transaction : transactions) {
          Set<String> uniqueItems = new HashSet<>(transaction);
          for (String item : uniqueItems) {
              Set<String> singleItem = new HashSet<>(Collections.singletonList(item));
              L1.put(singleItem, L1.getOrDefault(singleItem, 0) + 1);
          }
      }

      L1.entrySet().removeIf(entry -> entry.getValue() < minSupportCount);
      return L1;
  }

    private static Map<Set<String>, Integer> generateAndFilterCandidates(List<Set<String>> prevFrequentItemsets, List<List<String>> transactions, int minSupportCount) {
        Map<Set<String>, Integer> candidateCount = new HashMap<>();

        for (int i = 0; i < prevFrequentItemsets.size(); i++) {
            for (int j = i + 1; j < prevFrequentItemsets.size(); j++) {
                Set<String> candidate = new HashSet<>(prevFrequentItemsets.get(i));
                candidate.addAll(prevFrequentItemsets.get(j));

                // Chỉ xét các tập phổ biến có kích thước tăng dần k
                if (candidate.size() == prevFrequentItemsets.get(0).size() + 1) {
                    int count = 0;

                    // Đếm số lần xuất hiện của ứng viên trong các giao dịch
                    for (List<String> transaction : transactions) {
                        Set<String> uniqueTransaction = new HashSet<>(transaction);
                        if (uniqueTransaction.containsAll(candidate)) {
                            count++;
                        }
                    }

                    // Lưu ứng viên nếu support >= minSupportCount
                    if (count >= minSupportCount) {
                        candidateCount.put(candidate, count);
                    }
                }
            }
        }

        return candidateCount;
    }

    public static Map<Integer, Map<Set<String>, Integer>> apriori(List<List<String>> transactions, int minSupportCount) {
        Map<Set<String>, Integer> frequentItemsets = createL1(transactions, minSupportCount);
        Map<Integer, Map<Set<String>, Integer>> allFrequentItemsets = new HashMap<>();

        int k = 1;
        allFrequentItemsets.put(k, new HashMap<>(frequentItemsets));

        List<Set<String>> currentFrequentItemsets = new ArrayList<>(frequentItemsets.keySet());

        while (!currentFrequentItemsets.isEmpty()) {
            Map<Set<String>, Integer> candidateCount = generateAndFilterCandidates(currentFrequentItemsets, transactions, minSupportCount);

            k++;
            if (!candidateCount.isEmpty()) {
                allFrequentItemsets.put(k, new HashMap<>(candidateCount));
            }

            currentFrequentItemsets = new ArrayList<>(candidateCount.keySet());
        }

        return allFrequentItemsets;
    }

    public static void main(String[] args) {
        List<List<String>> transactions = Arrays.asList(
            Arrays.asList("A", "C", "T", "W"),
            Arrays.asList("C", "D", "W"),
            Arrays.asList("A", "C", "T", "W"),
            Arrays.asList("A", "C", "D", "W"),
            Arrays.asList("A", "C", "D", "T", "W"),
            Arrays.asList("C", "D", "T")
        );

        int minSupport = (50 * transactions.size() / 100);
        Map<Integer, Map<Set<String>, Integer>> frequentItemsetsByLevel = apriori(transactions, minSupport);

        System.out.println("Frequent Itemsets by Level:");
        for (Integer level : frequentItemsetsByLevel.keySet()) {
            System.out.println("Level " + level + ":");
            for (Set<String> itemset : frequentItemsetsByLevel.get(level).keySet()) {
                System.out.println(itemset + " - Support: " + frequentItemsetsByLevel.get(level).get(itemset));
            }
        }
    }
}
