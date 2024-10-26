package vn.codezx.algorithm;
import java.util.*;
/*
 * L1 ={item don sau cho thuoc (i) >= }}
 * 
 */


public class Apriori {

    private List<Set<String>> transactions;
    private int minSupport;

    public Apriori(List<Set<String>> transactions, int minSupport) {
        this.transactions = transactions;
        this.minSupport = minSupport;
    }

    public List<Set<String>> run() {
        List<Set<String>> frequentItemsets = new ArrayList<>();
        int k = 1;

        // Khởi tạo tập itemset đầu tiên
        List<Set<String>> currentItemsets = generateInitialItemsets();

        while (!currentItemsets.isEmpty()) {
            // Lấy itemset phổ biến
            List<Set<String>> frequentItemset = getFrequentItemsets(currentItemsets);
            frequentItemsets.addAll(frequentItemset);
            // Tạo itemset tiếp theo
            currentItemsets = generateNextItemsets(frequentItemset, k);
            k++;
        }

        return frequentItemsets;
    }

    private List<Set<String>> generateInitialItemsets() {
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

    private List<Set<String>> getFrequentItemsets(List<Set<String>> itemsets) {
        Map<Set<String>, Integer> itemsetCount = new HashMap<>();
        for (Set<String> transaction : transactions) {
            for (Set<String> itemset : itemsets) {
                if (transaction.containsAll(itemset)) {
                    itemsetCount.put(itemset, itemsetCount.getOrDefault(itemset, 0) + 1);
                }
            }
        }

        List<Set<String>> frequentItemsets = new ArrayList<>();
        for (Map.Entry<Set<String>, Integer> entry : itemsetCount.entrySet()) {
            if (entry.getValue() >= minSupport) {
                frequentItemsets.add(entry.getKey());
            }
        }
        return frequentItemsets;
    }

    private List<Set<String>> generateNextItemsets(List<Set<String>> frequentItemsets, int k) {
        List<Set<String>> newItemsets = new ArrayList<>();
        for (int i = 0; i < frequentItemsets.size(); i++) {
            for (int j = i + 1; j < frequentItemsets.size(); j++) {
                Set<String> first = frequentItemsets.get(i);
                Set<String> second = frequentItemsets.get(j);
                Set<String> union = new HashSet<>(first);
                union.addAll(second);

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

        int minSupport = (50*transactions.size()/100);
        Apriori apriori = new Apriori(transactions, minSupport);
        List<Set<String>> frequentItemsets = apriori.run();

        System.out.println("Frequent Itemsets:");
        for (Set<String> itemset : frequentItemsets) {
            System.out.println(itemset);
        }
    }
}
