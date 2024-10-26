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
            Set<String> uniqueItems = new HashSet<>(transaction); // Loại bỏ trùng lặp trong mỗi giao dịch
            for (String item : uniqueItems) {
                Set<String> singleItem = new HashSet<>(Collections.singletonList(item));
                L1.put(singleItem, L1.getOrDefault(singleItem, 0) + 1);
            }
        }

        // Lọc các tập phổ biến có support >= minSupportCount
        L1.entrySet().removeIf(entry -> entry.getValue() < minSupportCount);
        return L1;
    }

    // Hàm sinh ứng viên và kiểm tra độ hỗ trợ
    private static Map<Set<String>, Integer> generateAndFilterCandidates(List<Set<String>> prevFrequentItemsets, List<List<String>> transactions, int minSupportCount) {
        Map<Set<String>, Integer> frequentItem = new HashMap<>();

        // Sinh các ứng viên Ck từ Lk-1 và đếm support
        for (int i = 0; i < prevFrequentItemsets.size(); i++) {
            for (int j = i + 1; j < prevFrequentItemsets.size(); j++) {
                // Tạo ứng viên bằng cách hợp nhất hai tập phổ biến trước đó
                Set<String> candidate = new HashSet<>(prevFrequentItemsets.get(i));
                candidate.addAll(prevFrequentItemsets.get(j));

                // Kiểm tra kích thước ứng viên có tăng 1
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
                        frequentItem.put(candidate, count);
                    }
                }
            }
        }

        return frequentItem;
    }

    // Hàm chính của thuật toán Apriori
    public static Map<Set<String>, Integer> apriori(List<List<String>> transactions, int minSupportCount) {
        // Tính toán L1
        Map<Set<String>, Integer> frequentItemsets = createL1(transactions, minSupportCount);
        Map<Set<String>, Integer> allFrequentItemsets = new HashMap<>(frequentItemsets);

        List<Set<String>> currentFrequentItemsets = new ArrayList<>(frequentItemsets.keySet());
        int k = 2;

        // Vòng lặp sinh các tập phổ biến lớn hơn
        while (!currentFrequentItemsets.isEmpty()) {
            // Tích hợp sinh ứng viên và kiểm tra độ hỗ trợ
            Map<Set<String>, Integer> fItemSet = generateAndFilterCandidates(currentFrequentItemsets, transactions, minSupportCount);

            // Cập nhật tập phổ biến hiện tại
            currentFrequentItemsets = new ArrayList<>(fItemSet.keySet());
            allFrequentItemsets.putAll(fItemSet);
            k++;
        }

        return allFrequentItemsets;
    }

    public static void main(String[] args) {
        // Ví dụ dữ liệu giao dịch
        List<List<String>> transactions = Arrays.asList(
            Arrays.asList("A", "C", "T", "W"),
            Arrays.asList("C", "D", "W"),
            Arrays.asList("A", "C", "T", "W"),
            Arrays.asList("A", "C", "D", "W"),
            Arrays.asList("A", "C", "D", "T", "W"),
            Arrays.asList("C", "D", "T")
        );

        int minSupport = (50 * transactions.size() / 100); // Đặt ngưỡng support tối thiểu
        Map<Set<String>, Integer> frequentItemsets = apriori(transactions, minSupport);

        // In các tập phổ biến và số lần xuất hiện (support)
        System.out.println("Frequent Itemsets:");
        for (Set<String> itemset : frequentItemsets.keySet()) {
            System.out.print(itemset);
            System.out.println(" - Support: " + frequentItemsets.get(itemset));
        }
    }
}
